package com.a406.horsebit.repository.redis;

import com.a406.horsebit.dto.PriceDTO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class PriceRepository {
    private final RedissonClient redissonClient;
    private static final String CURRENT_PRICE_PREFIX = "CURRENT_PRICE:";
    private static final String START_PRICE_PREFIX = "START_PRICE:";

    @Autowired
    public PriceRepository(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public PriceDTO findCurrentPrice(Long tokenNo) {
        RBucket<Long> currentPriceRBucket = redissonClient.getBucket(CURRENT_PRICE_PREFIX + tokenNo);
        currentPriceRBucket.setIfAbsent(1L);
        return new PriceDTO(currentPriceRBucket.get());
    }

    public List<PriceDTO> findCurrentPrice(List<Long> tokenNoList) {
        List<PriceDTO> priceDTOList = new ArrayList<>(tokenNoList.size());
        int index = 0;
        for (Long tokenNo: tokenNoList) {
            priceDTOList.add(findCurrentPrice(tokenNo));
        }
        return priceDTOList;
    }

    public PriceDTO findStartPrice(Long tokenNo) {
        RBucket<Long> startPriceRBucket = redissonClient.getBucket(START_PRICE_PREFIX + tokenNo);
        startPriceRBucket.setIfAbsent(1L);
        return new PriceDTO(startPriceRBucket.get());
    }

    public List<PriceDTO> findStartPrice(List<Long> tokenNoList) {
        List<PriceDTO> priceDTOList = new ArrayList<>(tokenNoList.size());
        int index = 0;
        for (Long tokenNo: tokenNoList) {
            priceDTOList.add(findStartPrice(tokenNo));
        }
        return priceDTOList;
    }
}
