package com.a406.horsebit.cache;

import com.a406.horsebit.repository.redis.CandleRepository;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class CandleCache {
    private final RedissonClient redissonClient;
    private static final String CANDLE_INITIAL_TIME_PREFIX = "CANDLE_INITIAL_TIME:";
    private Map<Long, LocalDateTime> tokenCandleInitialTimeMap;

    @Autowired
    CandleCache(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
        tokenCandleInitialTimeMap = new HashMap<Long, LocalDateTime>();
    }

    public LocalDateTime getInitialTime(Long tokenNo) {
        if (!tokenCandleInitialTimeMap.containsKey(tokenNo)) {
            tokenCandleInitialTimeMap.put(tokenNo, findCandleInitialTime(tokenNo));
        }
        return tokenCandleInitialTimeMap.get(tokenNo);
    }

    public LocalDateTime findCandleInitialTime(Long tokenNo) {
        RBucket<LocalDateTime> initialTimeRBucket = redissonClient.getBucket(CANDLE_INITIAL_TIME_PREFIX + tokenNo);
        return initialTimeRBucket.get();
    }
}
