package com.a406.horsebit.repository.redis;

import com.a406.horsebit.domain.redis.Order;
import com.a406.horsebit.domain.redis.OrderPage;
import com.a406.horsebit.domain.redis.OrderSummary;
import com.a406.horsebit.domain.redis.VolumePage;
import com.a406.horsebit.dto.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class OrderRepository {
    private final RedissonClient redissonClient;
    private static final String REDIS_TOKEN_BUY_TOTAL_VOLUME_PREFIX = "BUY_TOTAL_VOLUME:";
    private static final String REDIS_TOKEN_SELL_TOTAL_VOLUME_PREFIX = "SELL_TOTAL_VOLUME:";
    private static final String REDIS_TOKEN_BUY_ORDER_BOOK_PREFIX = "BUY_ORDER_BOOK:";
    private static final String REDIS_TOKEN_SELL_ORDER_BOOK_PREFIX = "SELL_ORDER_BOOK:";
    private static final String REDIS_TOKEN_BUY_VOLUME_BOOK_PREFIX = "BUY_VOLUME_BOOK:";
    private static final String REDIS_TOKEN_SELL_VOLUME_BOOK_PREFIX = "SELL_VOLUME_BOOK:";
    private static final String REDIS_TOKEN_BUY_ORDER_SUMMARY_LIST_PREFIX = "BUY_ORDER_SUMMARY_LIST:";
    private static final String REDIS_TOKEN_SELL_ORDER_SUMMARY_LIST_PREFIX = "SELL_ORDER_SUMMARY_LIST:";
    private static final String REDIS_USER_ORDER_LIST_PREFIX = "USER_ORDER_BOOK:";

    @Autowired
    public OrderRepository(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public List<OrderDTO> findAllByUserNoAndTokenNoAndCode(Long userNo, Long tokenNo, String code) {
        RMap<Long, RMap<Long, Order>>  userOrderList = redissonClient.getMap(REDIS_USER_ORDER_LIST_PREFIX + userNo);

        //test
//        Order order = new Order();
////        order.setHrNo(1L);
////        order.setTokenNo(2L);
//        order.setPrice(3);
//        order.setQuantity(4.0);
//        order.setRemain(5.0);
//        order.setOrderTime(new Timestamp(1234, 5, 6, 7, 8, 9, 10));
//        order.setSellBuyFlag("B");
//        RMap<String, Order> userOrderMapInput = redissonClient.getMap(Long.toString(tokenNo));
//        userOrderMapInput.fastPut("1", order);
//        userOrderBook.fastPut(code, userOrderMapInput);
        //test

        RMap<Long, Order> userOrderMap = userOrderList.get(tokenNo);
        List<OrderDTO> orderDTOList = new ArrayList<OrderDTO>();
        userOrderMap.forEach((orderKey, orderValue)->{
            orderDTOList.add(new OrderDTO(orderKey, userNo, tokenNo, code, orderValue.getPrice(), orderValue.getQuantity(), orderValue.getRemain(), orderValue.getOrderTime(), orderValue.getSellBuyFlag()));
        });
        orderDTOList.sort(Comparator.comparingLong(OrderDTO::getOrderNo));

        log.info("orderDTO: " + orderDTOList.toString());

        return orderDTOList;
    }

    public Boolean saveTokenBuyOrderSummary(Long tokenNo, Long price, OrderSummary orderSummary) {
        return saveTokenOrderSummary(tokenNo, price, orderSummary, REDIS_TOKEN_BUY_TOTAL_VOLUME_PREFIX, REDIS_TOKEN_BUY_ORDER_BOOK_PREFIX, REDIS_TOKEN_BUY_ORDER_SUMMARY_LIST_PREFIX, REDIS_TOKEN_BUY_VOLUME_BOOK_PREFIX);
    }

    public Boolean saveTokenSellOrderSummary(Long tokenNo, Long price, OrderSummary orderSummary) {
        return saveTokenOrderSummary(tokenNo, price, orderSummary, REDIS_TOKEN_SELL_TOTAL_VOLUME_PREFIX, REDIS_TOKEN_SELL_ORDER_BOOK_PREFIX, REDIS_TOKEN_SELL_ORDER_SUMMARY_LIST_PREFIX, REDIS_TOKEN_SELL_VOLUME_BOOK_PREFIX);
    }

    private Boolean saveTokenOrderSummary(Long tokenNo, Long price, OrderSummary orderSummary, String TOTAL_VOLUME_PREFIX, String ORDER_BOOK_PREFIX, String ORDER_SUMMARY_LIST_PREFIX, String VOLUME_BOOK_PREFIX) {
        // Update total volume.
        RBucket<Double> totalVolume = redissonClient.getBucket(TOTAL_VOLUME_PREFIX + tokenNo);
        if(!totalVolume.setIfAbsent(orderSummary.getRemain())) {
            totalVolume.set(totalVolume.get() + orderSummary.getRemain());
        }

        // Update order book.
        RMap<Long, OrderPage> tokenOrderBook = redissonClient.getMap(ORDER_BOOK_PREFIX + tokenNo);
        OrderPage orderPage;
        // Price has empty list. Create order list first.
        if(tokenOrderBook.containsKey(price)) {
            orderPage = new OrderPage();
            orderPage.setVolume(0.0);
            orderPage.setOrderSummaryRList(redissonClient.getList(ORDER_SUMMARY_LIST_PREFIX + tokenNo));
        }
        // Get existing order page.
        else {
            orderPage = tokenOrderBook.get(price);
        }
        // Update volume of page.
        orderPage.setVolume(orderPage.getVolume() + orderSummary.getRemain());
        // Update order summary list of page.
        orderPage.getOrderSummaryRList().add(orderSummary);

        // Update volume book.
        RScoredSortedSet<VolumePage> tokenVolumeBook = redissonClient.getScoredSortedSet(VOLUME_BOOK_PREFIX + tokenNo);
        VolumePage volumePage = new VolumePage();
        volumePage.setVolume(orderPage.getVolume());
        volumePage.setPrice(price);
        return tokenVolumeBook.add(price, volumePage);
    }
}
