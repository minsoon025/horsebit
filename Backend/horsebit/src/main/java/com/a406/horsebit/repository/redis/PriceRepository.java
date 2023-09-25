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

    @Autowired
    public PriceRepository(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public PriceDTO findOneByTokenNo(Long tokenNo) {
        RBucket<Long> currentPrice = redissonClient.getBucket(CURRENT_PRICE_PREFIX + tokenNo);
        currentPrice.setIfAbsent(0L);
        return new PriceDTO(currentPrice.get());
    }

    public List<PriceDTO> findAllByTokenNo(List<Long> tokenNoList) {
        List<PriceDTO> priceDTOList = new ArrayList<>(tokenNoList.size());
        int index = 0;
        for (Long tokenNo: tokenNoList) {
            priceDTOList.set(index++, findOneByTokenNo(tokenNo));
        }
        return priceDTOList;
    }
}
