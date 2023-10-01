package com.a406.horsebit.repository.redis;

import com.a406.horsebit.domain.redis.Order;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class OrderRepositoryTest {
    OrderRepository orderRepository;
    RedissonClient redissonClient;

    @Autowired
    OrderRepositoryTest(RedissonClient redissonClient) {
        this.orderRepository = new OrderRepository(redissonClient);
        this.redissonClient = redissonClient;
    }

    //@Test
    void generateUser() {
        for (long tokenNo = 1L; tokenNo <= 25; ++tokenNo) {
//            orderRepository.newUserOrderList(4L, tokenNo);
            RMap<Long, RMap<Long, Order>> userOrderList = redissonClient.getMap("USER_ORDER_LIST:3");
            RMap<Long, Order> userOrderMap = redissonClient.getMap("USER_ORDER_LIST:3:" + tokenNo);
            userOrderList.fastPut(tokenNo, userOrderMap);
            userOrderList.get(tokenNo).fastPut(0L, new Order());
            log.info(String.valueOf(userOrderList.get(tokenNo).isEmpty()));
            userOrderMap.fastRemove(0L);
            log.info(String.valueOf(userOrderList.get(tokenNo).isEmpty()));
        }
    }

    //@Test
    void checkUser() {
        for (long tokenNo = 1L; tokenNo <= 25; ++tokenNo) {
            log.info(redissonClient.getMap("USER_ORDER_LIST:1").get(1L).toString());
        }
    }

    @Test
    void generateTotalValue() {
        for (long tokenNo = 1L; tokenNo <= 25; ++tokenNo) {
            orderRepository.newTotalVolume(tokenNo);
        }
    }
}