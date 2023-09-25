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
    private String CURRENT_PRICE_PREFIX = "CURRENT_PRICE:";
    private String START_PRICE_PREFIX = "START_PRICE:";

    @Autowired
    public PriceRepository(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public PriceDTO findOneCurrentPriceByTokenNo(Long tokenNo) {
        RBucket<Long> currentPrice = redissonClient.getBucket(CURRENT_PRICE_PREFIX + tokenNo);
        currentPrice.setIfAbsent(1L);
        return new PriceDTO(currentPrice.get());
    }

    public List<PriceDTO> findAllCurrentPriceByTokenNo(List<Long> tokenNoList) {
        List<PriceDTO> priceDTOList = new ArrayList<>(tokenNoList.size());
        int index = 0;
        for (Long tokenNo: tokenNoList) {
            priceDTOList.add(findOneCurrentPriceByTokenNo(tokenNo));
        }
        return priceDTOList;
    }

    public PriceDTO findOneStartPriceByTokenNo(Long tokenNo) {
        RBucket<Long> startPrice = redissonClient.getBucket(START_PRICE_PREFIX + tokenNo);
        startPrice.setIfAbsent(1L);
        return new PriceDTO(startPrice.get());
    }

    public List<PriceDTO> findAllStartPriceByTokenNo(List<Long> tokenNoList) {
        List<PriceDTO> priceDTOList = new ArrayList<>(tokenNoList.size());
        int index = 0;
        for (Long tokenNo: tokenNoList) {
            priceDTOList.add(findOneStartPriceByTokenNo(tokenNo));
        }
        return priceDTOList;
    }
}
