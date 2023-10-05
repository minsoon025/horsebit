package com.a406.horsebit.repository.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
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
        for(long tokenNo = 1L; tokenNo <= 24L; tokenNo++) {
            RBucket<Long> currentPrice = redissonClient.getBucket(CURRENT_PRICE_PREFIX + tokenNo);
            currentPrice.set(((tokenNo * 3L) % 25L) * 1000L);
            RBucket<Long> startPrice = redissonClient.getBucket(START_PRICE_PREFIX + tokenNo);
            startPrice.set(((tokenNo * 7L) % 25L) * 1000L);
        }
        RBucket<Long> currentPrice25 = redissonClient.getBucket(CURRENT_PRICE_PREFIX + 25L);
        currentPrice25.set(30000L);
        RBucket<Long> startPrice25 = redissonClient.getBucket(START_PRICE_PREFIX + 25L);
        startPrice25.set(1000L);
        RBucket<Long> currentPrice2 = redissonClient.getBucket(CURRENT_PRICE_PREFIX + 2L);
        currentPrice2.set(30000L);
        RBucket<Long> startPrice2 = redissonClient.getBucket(START_PRICE_PREFIX + 2L);
        startPrice2.set(1000L);

    }

    @Test
    void getData() {
        for(long tokenNo = 1L; tokenNo <= 25L; tokenNo++) {
            RBucket<Long> currentPrice = redissonClient.getBucket(CURRENT_PRICE_PREFIX + tokenNo);
            RBucket<Long> startPrice = redissonClient.getBucket(START_PRICE_PREFIX + tokenNo);
            log.info("--- current price: " + currentPrice.get() + "   startPrice: " + startPrice.get() + " ---");
        }
    }
}