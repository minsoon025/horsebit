package com.a406.horsebit.repository.redis;

import com.a406.horsebit.dto.CandleDTO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Slf4j
@Repository
public class CandleRepository {
    private final RedissonClient redissonClient;

    @Autowired
    public CandleRepository(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public CandleDTO findRangeByTokenNoAndEndTimeAndQuantity(Long tokenNo, LocalDateTime endTime, Long quantity) {

    }
}
