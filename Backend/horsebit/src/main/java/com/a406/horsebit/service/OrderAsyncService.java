package com.a406.horsebit.service;

import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;

public interface OrderAsyncService {
    @Async
    void buyExecuteTrade(long price, double quantity, long tokenNo, long buyOrderNo, long buyUserNo, long sellOrderNo, long sellUserNo, LocalDateTime tradeTime);
    @Async
    void sellExecuteTrade(long price, double quantity, long tokenNo, long buyOrderNo, long buyUserNo, long sellOrderNo, long sellUserNo, LocalDateTime tradeTime);
}
