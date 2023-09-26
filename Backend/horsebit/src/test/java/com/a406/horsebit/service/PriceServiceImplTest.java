package com.a406.horsebit.service;

import com.a406.horsebit.dto.PriceDTO;
import com.a406.horsebit.dto.PriceRateOfChangeDTO;
import com.a406.horsebit.repository.redis.PriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PriceServiceImplTest {
    PriceService priceService;

    @Autowired
    public PriceServiceImplTest(PriceRepository priceRepository) {
        this.priceService = new PriceServiceImpl(priceRepository);
    }

    @Test
    public void getCurrentPrice() {
        Long currentPrice = priceService.getCurrentPrice(1L).getPrice();

        Assertions.assertNotEquals(currentPrice, currentPrice, currentPrice);
    }
}