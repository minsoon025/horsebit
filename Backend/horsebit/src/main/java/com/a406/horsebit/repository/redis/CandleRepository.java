package com.a406.horsebit.repository.redis;

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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class CandleRepository {
    private final RedissonClient redissonClient;

    private static final String CANDLE_PREFIX = "CANDLE_";
    private static final String CANDLE_INITIAL_TIME_PREFIX = "CANDLE_INITIAL_TIME:";

    @Autowired
    public CandleRepository(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    private String listNameGenerator(Long tokenNo, String candleType) {
        return CANDLE_PREFIX + candleType + ":" + tokenNo;
    }

    private int indexFinder(int listSize, int index) {
        return Math.min(listSize, index);
    }

    public CandleDTO findOneByTokenNo(Long tokenNo, Integer index, CandleType candleType) {
        RList<Candle> candleRList = redissonClient.getList(listNameGenerator(tokenNo, candleType.getCandleType()));
        Candle candle = candleRList.get(indexFinder(candleRList.size(), index));
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
}
