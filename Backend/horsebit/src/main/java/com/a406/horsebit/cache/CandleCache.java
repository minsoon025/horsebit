package com.a406.horsebit.cache;

import com.a406.horsebit.repository.redis.CandleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class CandleCache {
    private final CandleRepository candleRepository;

    @Autowired
    CandleCache(CandleRepository candleRepository) {
        this.candleRepository = candleRepository;
    }
    private Map<Long, LocalDateTime> tokenCandleInitialTimeMap;

    public LocalDateTime getInitialTime(Long tokenNo) {
        if (!tokenCandleInitialTimeMap.containsKey(tokenNo)) {
            tokenCandleInitialTimeMap.put(tokenNo, candleRepository.findCandleInitialTime(tokenNo));
        }
        return tokenCandleInitialTimeMap.get(tokenNo);
    }
}
