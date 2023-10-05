package com.a406.horsebit.repository.redis;

import com.a406.horsebit.aop.DistributedLock;
import com.a406.horsebit.cache.CandleCache;
import com.a406.horsebit.constant.CandleConstant;
import com.a406.horsebit.domain.redis.Candle;
import com.a406.horsebit.domain.redis.CandleType;
import com.a406.horsebit.dto.CandleDTO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class CandleRepository {
    private final RedissonClient redissonClient;
    private final CandleCache candleCache;

    private static final String CANDLE_PREFIX = "CANDLE_";
    private static final String CANDLE_INITIAL_TIME_PREFIX = "CANDLE_INITIAL_TIME:";

    @Autowired
    public CandleRepository(RedissonClient redissonClient, CandleCache candleCache) {
        this.redissonClient = redissonClient;
        this.candleCache = candleCache;
    }

    private String listNameGenerator(Long tokenNo, String candleType) {
        return CANDLE_PREFIX + candleType + ":" + tokenNo;
    }

    public CandleDTO findOneByTokenNo(Long tokenNo, Integer index, CandleType candleType) {
        RList<Candle> candleRList = redissonClient.getList(listNameGenerator(tokenNo, candleType.getCandleType()));
        Candle candle = candleRList.get(Math.min(candleRList.size(), index));
        if(candleRList.size() < index) {
            return new CandleDTO(candle.getStartTime().plusMinutes(candleType.getCandleMinuteTime() * (index - candleRList.size() + 1)), candle.getClose(), candle.getClose(), candle.getClose(), candle.getClose(), 0.0);
        }
        return new CandleDTO(candle.getStartTime(), candle.getOpen(), candle.getClose(), candle.getHigh(), candle.getLow(), candle.getVolume());
    }

    public List<CandleDTO> findRangeByTokenNo(Long tokenNo, Integer startIndex, Integer quantity, CandleType candleType) {
        RList<Candle> candleRList = redissonClient.getList(listNameGenerator(tokenNo, candleType.getCandleType()));
        List<CandleDTO> candleDTOList = new ArrayList<CandleDTO>(quantity);
        if (candleRList.size() >= startIndex + quantity) {
            for (int index = startIndex; index < startIndex + quantity; ++index) {
                Candle candle = candleRList.get(index);
                candleDTOList.add(new CandleDTO(candle.getStartTime(), candle.getOpen(), candle.getClose(), candle.getHigh(), candle.getLow(), candle.getVolume()));
            }
            return candleDTOList;
        }
        int midIndex = candleRList.size();
        for (int index = startIndex; index < midIndex; ++index) {
            Candle candle = candleRList.get(index);
            candleDTOList.add(new CandleDTO(candle.getStartTime(), candle.getOpen(), candle.getClose(), candle.getHigh(), candle.getLow(), candle.getVolume()));
        }
        Candle candle = candleRList.get(midIndex - 1);
        for (int index = midIndex; index < startIndex + quantity; ++index) {
            candleDTOList.add(new CandleDTO(candle.getStartTime().plusMinutes(candleType.getCandleMinuteTime() * (index - midIndex + 1)), candle.getClose(), candle.getClose(), candle.getClose(), candle.getClose(), 0.0));
        }
        return candleDTOList;
    }

    public LocalDateTime findCandleInitialTime(Long tokenNo) {
        RBucket<LocalDateTime> initialTimeRBucket = redissonClient.getBucket(CANDLE_INITIAL_TIME_PREFIX + tokenNo);
        return initialTimeRBucket.get();
    }

    public void setCandleInitialTime(Long tokenNo, LocalDateTime initialTime) {
        RBucket<LocalDateTime> initialTimeRBucket = redissonClient.getBucket(CANDLE_INITIAL_TIME_PREFIX + tokenNo);
        initialTimeRBucket.set(initialTime);
    }

    public void setInitialCandle(Long tokenNo, LocalDateTime initialTime, Long price) {
        CandleConstant.CANDLE_TYPE_LIST.forEach(candleType -> {
            RList<Candle> candleRList = redissonClient.getList(listNameGenerator(tokenNo, candleType.getCandleType()));
            candleRList.clear();
            Candle candle = new Candle();
            candle.setOpen(price);
            candle.setClose(price);
            candle.setHigh(price);
            candle.setLow(price);
            candle.setVolume(0.0);
            candle.setStartTime(initialTime);
            candleRList.add(candle);
        });
    }

    public void updateCandle(Long tokenNo, Long price, Double volume) {
        CandleConstant.CANDLE_TYPE_LIST.forEach(candleType -> {
            updateCandle(tokenNo, price, volume, candleType);
        });
    }

    public void updateCandle(Long tokenNo, Long price, Double volume, CandleType candleType) {
        LocalDateTime initialTime = candleCache.getInitialTime(tokenNo);
        LocalDateTime updateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        int index = (int) (ChronoUnit.MINUTES.between(initialTime, updateTime) / candleType.getCandleMinuteTime());
        RList<Candle> candleRList = redissonClient.getList(listNameGenerator(tokenNo, candleType.getCandleType()));
        int candleRListSize = candleRList.size();
        if(candleRListSize <= index) {
            generateNewCandle(tokenNo, candleRList, index, candleType.getCandleMinuteTime());
        }
        Candle candle = candleRList.get(candleRListSize - 1);
        if (candle.getClose() < price) {
            candle.setHigh(Math.max(candle.getHigh(), price));
        }
        else if (price < candle.getClose()) {
            candle.setLow(Math.min(candle.getLow(), price));
        }
        else {
            candle.setVolume(candle.getVolume() + volume);
            return;
        }
        candle.setVolume(candle.getVolume() + volume);
        candle.setClose(price);
        candleRList.fastSet(index, candle);
    }

//    @DistributedLock(key = "'CANDLE_UPDATE:' + #tokenNo.toString() + #candleMinuteTime.toString")
    private void generateNewCandle(Long tokenNo, RList<Candle> candleRList, Integer targetIndex, Long candleMinuteTime) {
        int index = candleRList.size();

        log.info("--------------------------------------size: " + index);
        Candle lastCandle = candleRList.get(index - 1);
        Candle newCandle = new Candle();
        newCandle.setStartTime(lastCandle.getStartTime());
        newCandle.setOpen(lastCandle.getClose());
        newCandle.setClose(lastCandle.getClose());
        newCandle.setHigh(lastCandle.getClose());
        newCandle.setLow(lastCandle.getClose());
        newCandle.setVolume(0.0);
        while (index++ <= targetIndex) {
            newCandle.setStartTime(newCandle.getStartTime().plusMinutes(candleMinuteTime));
            candleRList.add(newCandle);
        }
    }
}
