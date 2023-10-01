package com.a406.horsebit.service;

import com.a406.horsebit.domain.redis.Order;
import com.a406.horsebit.repository.TradeRepository;
import com.a406.horsebit.repository.redis.OrderRepository;
import com.a406.horsebit.repository.redis.PriceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceImplTest {
    OrderService orderService;

    @Autowired
    OrderServiceImplTest(OrderRepository orderRepository, PriceRepository priceRepository, TradeRepository tradeRepository) {
        this.orderService = new OrderServiceImpl(orderRepository, priceRepository, tradeRepository);
    }

    @Test
    void getOrders() {
    }

    @Test
    void processBuyOrder() {
        orderService.processBuyOrder(1L, 1L, new Order(10L, 20.0, 20.0, "B"));
        orderService.processBuyOrder(1L, 1L, new Order(20L, 20.0, 20.0, "B"));
        orderService.processBuyOrder(1L, 1L, new Order(30L, 20.0, 20.0, "B"));
        orderService.processSellOrder(2L, 1L, new Order(10L, 20.0, 20.0, "S"));
        orderService.processSellOrder(2L, 1L, new Order(10L, 20.0, 20.0, "S"));
        orderService.processSellOrder(2L, 1L, new Order(10L, 20.0, 20.0, "S"));
    }

    @Test
    void processSellOrder() {
    }
}