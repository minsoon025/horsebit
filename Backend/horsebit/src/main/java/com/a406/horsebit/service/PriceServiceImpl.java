package com.a406.horsebit.service;

import com.a406.horsebit.dto.PriceDTO;
import com.a406.horsebit.repository.redis.PriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PriceServiceImpl implements PriceService {
    private final PriceRepository priceRepository;

    @Autowired
    PriceServiceImpl(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public PriceDTO getCurrentPrice(Long tokenNo) {
        return priceRepository.findOneByTokenNo(tokenNo);
    }

    @Override
    public List<PriceDTO> getCurrentPrice(List<Long> tokenNoList) {
        return priceRepository.findAllByTokenNo(tokenNoList);
    }
}
