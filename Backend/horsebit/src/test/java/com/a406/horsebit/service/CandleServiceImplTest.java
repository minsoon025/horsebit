package com.a406.horsebit.service;

import com.a406.horsebit.dto.CandleDTO;
import com.a406.horsebit.repository.redis.CandleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CandleServiceImplTest {
    private final CandleService candleService;

    @Autowired
    CandleServiceImplTest(CandleService candleService) {
        this.candleService = candleService;
    }

    @Test
    public void rangeTest() {
        Long tokenNo = 1L;
        LocalDateTime endTime = LocalDateTime.parse("2023-10-04T09:35:21.948");
        Integer candleTypeIndex = 0;
        Long quantity = 10L;
        Long margin = 1L;
        List<CandleDTO> candleDTOList = candleService.getCandle(tokenNo, endTime, candleTypeIndex, quantity, margin);
    }
}