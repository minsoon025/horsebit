package com.a406.horsebit.repository;

import com.a406.horsebit.domain.redis.Order;
import com.a406.horsebit.dto.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Repository
public class OrderRedisRepository {
    private final RedissonClient redissonClient;
    private static final String REDIS_USER_ORDERS_PREFIX = "ORDER_BOOK_";

    @Autowired
    public OrderRedisRepository(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public List<OrderDTO> findAllByUserNoAndTokenNoAndCode(Long userNo, Long tokenNo, String code) {
        RMap<String, RMap<String, Order>>  userOrderBookMap = redissonClient.getMap(REDIS_USER_ORDERS_PREFIX + userNo);
        RMap<String, Order> userOrderMap = userOrderBookMap.get(code);

        //test
        Order order = new Order();
        order.setHrNo(1L);
        order.setTokenNo(2L);
        order.setPrice(3);
        order.setQuantity(4.0);
        order.setRemain(5.0);
        order.setOrderTime(new Timestamp(1234, 5, 6, 7, 8, 9, 10));
        order.setSellBuyFlag("B");
        userOrderMap.fastPut("1", order);
        //test

        List<OrderDTO> orderDTOList = new ArrayList<OrderDTO>();
        userOrderMap.forEach((orderKey, orderValue)->{
            orderDTOList.add(new OrderDTO(Long.parseLong(orderKey), userNo, orderValue.getTokenNo(), code, orderValue.getPrice(), orderValue.getQuantity(), orderValue.getRemain(), orderValue.getOrderTime(), orderValue.getSellBuyFlag()));
        });
        orderDTOList.sort(Comparator.comparingLong(OrderDTO::getOrderNo));

        log.info("orderDTO: " + orderDTOList.toString());

        return orderDTOList;
    }
}
