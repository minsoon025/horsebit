package com.a406.horsebit.repository.redis;

import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PriceRepositoryTest {
    private final RedissonClient redissonClient;
    private String CURRENT_PRICE_PREFIX = "CURRENT_PRICE:";
    private String START_PRICE_PREFIX = "START_PRICE:";

    @Autowired
    public PriceRepositoryTest(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Test
    void saveData() {
        for(Long tokenNo = 1L; tokenNo < 11; tokenNo++) {
            RBucket<Long> currentPrice = redissonClient.getBucket(CURRENT_PRICE_PREFIX + tokenNo);
            currentPrice.set(((tokenNo * 3L) % 10L) * 1000L);
            RBucket<Long> startPrice = redissonClient.getBucket(START_PRICE_PREFIX + tokenNo);
            startPrice.set(((tokenNo * 7L) % 10L) * 1000L);
        }
    }

    @Test
    void getData() {
    }
}