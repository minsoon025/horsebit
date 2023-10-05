package com.a406.horsebit.service;

import com.a406.horsebit.domain.redis.Order;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;

public interface OrderAsyncService {
    @Async
    void buyExecuteTrade(long price, double quantity, long tokenNo, long buyOrderNo, long buyUserNo, long sellOrderNo, long sellUserNo, Order sellOrder, LocalDateTime tradeTime);
    @Async
    void sellExecuteTrade(long price, double quantity, long tokenNo, long buyOrderNo, long buyUserNo, Order buyOrder, long sellOrderNo, long sellUserNo, LocalDateTime tradeTime);
}
