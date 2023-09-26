package com.a406.horsebit.service;

import com.a406.horsebit.repository.redis.CandleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CandleServiceImplTest {
    private final CandleService candleService;


    @Autowired
    CandleServiceImplTest(CandleRepository candleRepository) {
        this.candleService = new CandleServiceImpl(candleRepository);
    }
}