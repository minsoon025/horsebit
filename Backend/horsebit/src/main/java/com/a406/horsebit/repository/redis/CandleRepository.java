package com.a406.horsebit.repository.redis;

import com.a406.horsebit.domain.redis.Candle;
import com.a406.horsebit.dto.CandleDTO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RList;
import org.redisson.api.RMap;
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

    public CandleDTO findOneByTokenNoAndIndexAndCandleType(Long tokenNo, Integer index, String candleType) {
        RList<Candle> candleRList = redissonClient.getList(listNameGenerator(tokenNo, candleType));
        Candle candle = candleRList.get(indexFinder(candleRList.size(), index));
        return new CandleDTO(candle.getStartTime(), candle.getOpen(), candle.getClose(), candle.getHigh(), candle.getLow(), candle.getVolume());
    }

    public List<CandleDTO> findRangeByTokenNoAndIndexAndQuantityAndCandleType(Long tokenNo, Integer startIndex, Integer quantity, String candleType) {
        RList<Candle> candleRList = redissonClient.getList(listNameGenerator(tokenNo, candleType));
        List<CandleDTO> candleDTOList = new ArrayList<CandleDTO>(quantity);
        for (Integer index = startIndex; index < startIndex + quantity; ++index) {
            Candle candle = candleRList.get(indexFinder(candleRList.size(), index));
            candleDTOList.add(new CandleDTO(candle.getStartTime(), candle.getOpen(), candle.getClose(), candle.getHigh(), candle.getLow(), candle.getVolume()));
        }
        return candleDTOList;
    }
}
