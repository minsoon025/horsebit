package com.a406.horsebit.repository.redis;

import com.a406.horsebit.constant.PriceConstant;
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
    private static final String REDIS_TOKEN_TRADE_TOTAL_VOLUME_PREFIX = "TRADE_TOTAL_VOLUME:";
    private static final String REDIS_TOKEN_BUY_ORDER_BOOK_PREFIX = "BUY_ORDER_BOOK:";
    private static final String REDIS_TOKEN_SELL_ORDER_BOOK_PREFIX = "SELL_ORDER_BOOK:";
    private static final String REDIS_TOKEN_BUY_VOLUME_BOOK_PREFIX = "BUY_VOLUME_BOOK:";
    private static final String REDIS_TOKEN_SELL_VOLUME_BOOK_PREFIX = "SELL_VOLUME_BOOK:";
    private static final String REDIS_TOKEN_BUY_ORDER_SUMMARY_LIST_PREFIX = "BUY_ORDER_SUMMARY_LIST:";
    private static final String REDIS_TOKEN_SELL_ORDER_SUMMARY_LIST_PREFIX = "SELL_ORDER_SUMMARY_LIST:";
    private static final String REDIS_USER_ORDER_LIST_PREFIX = "USER_ORDER_LIST:";

    @Autowired
    public OrderRepository(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    ////////////////////////////////////////////////////
    /* --- Token Redis Structure Initiate Methods --- */
    ////////////////////////////////////////////////////

    // Total volume, user order list should be generated at token launch.
    // Volume book and order book are managed dynamically.

    public void newTotalVolume(Long tokenNo) {
        // Generate total volume RBucket.
        final Double INITIAL_VOLUME = 0.0;
        RBucket<Double> buyTotalVolumeRBucket = redissonClient.getBucket(REDIS_TOKEN_BUY_TOTAL_VOLUME_PREFIX + tokenNo);
        buyTotalVolumeRBucket.setIfAbsent(INITIAL_VOLUME);
        RBucket<Double> sellTotalVolumeRBucket = redissonClient.getBucket(REDIS_TOKEN_SELL_TOTAL_VOLUME_PREFIX + tokenNo);
        sellTotalVolumeRBucket.setIfAbsent(INITIAL_VOLUME);
        RBucket<Double> tradeTotalVolumeRBucket = redissonClient.getBucket(REDIS_TOKEN_TRADE_TOTAL_VOLUME_PREFIX + tokenNo);
        tradeTotalVolumeRBucket.setIfAbsent(INITIAL_VOLUME);
    }

    public void newUserOrderList(Long userNo, Long tokenNo) {
        // Get order book RMap.
        RMap<Long, RMap<Long, Order>> userOrderList = redissonClient.getMap(REDIS_USER_ORDER_LIST_PREFIX + userNo);
        // Generate user token order map RMap.
        RMap<Long, Order> userOrderMap = redissonClient.getMap(REDIS_USER_ORDER_LIST_PREFIX + userNo + ":" + tokenNo);
        userOrderList.fastPutIfAbsent(tokenNo, userOrderMap);
    }

    public void newUserOrderList(Long userNo, List<Long> tokenNoList) {
        // Get order book RMap.
        RMap<Long, RMap<Long, Order>> userOrderList = redissonClient.getMap(REDIS_USER_ORDER_LIST_PREFIX + userNo);
        // Generate user token order map RMap.
        for (long tokenNo: tokenNoList) {
            RMap<Long, Order> userOrderMap = redissonClient.getMap(REDIS_USER_ORDER_LIST_PREFIX + userNo + ":" + tokenNo);
            userOrderList.fastPutIfAbsent(tokenNo, userOrderMap);
        }
    }

    /////////////////////////////////////
    /* --- User Order List Methods --- */
    /////////////////////////////////////

    public List<OrderDTO> findAllOrder(Long userNo, Long tokenNo, String code) {
        RMap<Long, Order> userOrderMap = getUserOrderMap(userNo, tokenNo);
        List<OrderDTO> orderDTOList = new ArrayList<>();
        if(userOrderMap.isEmpty()) {
            return orderDTOList;
        }
        userOrderMap.forEach((orderKey, orderValue)->{
            orderDTOList.add(new OrderDTO(orderKey, userNo, tokenNo, code, ((int) orderValue.getPrice()), orderValue.getQuantity(), orderValue.getRemain(), orderValue.getOrderTime(), orderValue.getSellBuyFlag()));
        });
        orderDTOList.sort(Comparator.comparingLong(OrderDTO::getOrderNo));
        return orderDTOList;
    }

    public Order findOrder(Long userNo, Long tokenNo, Long orderNo) {
        RMap<Long, Order> userOrderMap = getUserOrderMap(userNo, tokenNo);
        return userOrderMap.get(orderNo);
    }

    public void saveOrder(Long userNo, Long tokenNo, Long orderNo, Order order) {
        RMap<Long, Order> userOrderMap = getUserOrderMap(userNo, tokenNo);
        userOrderMap.fastPut(orderNo, order);
    }

    public Order deleteOrder(Long userNo, Long tokenNo, Long orderNo) {
        RMap<Long, Order> userOrderMap = getUserOrderMap(userNo, tokenNo);
        return userOrderMap.remove(orderNo);
    }

    private RMap<Long, Order> getUserOrderMap(Long userNo, Long tokenNo) {
        RMap<Long, RMap<Long, Order>> userOrderList = redissonClient.getMap(REDIS_USER_ORDER_LIST_PREFIX + userNo);
        return userOrderList.get(tokenNo);
    }

    ////////////////////////////////
    /* --- Order Book Methods --- */
    ////////////////////////////////

    public OrderSummary findBuyOrderSummary(Long tokenNo, Long price) {
        return findOrderSummary(tokenNo, price, REDIS_TOKEN_BUY_ORDER_BOOK_PREFIX, REDIS_TOKEN_BUY_ORDER_SUMMARY_LIST_PREFIX);
    }

    public OrderSummary findSellOrderSummary(Long tokenNo, Long price) {
        return findOrderSummary(tokenNo, price, REDIS_TOKEN_SELL_ORDER_BOOK_PREFIX, REDIS_TOKEN_SELL_ORDER_SUMMARY_LIST_PREFIX);
    }

    private OrderSummary findOrderSummary(Long tokenNo, Long price, String ORDER_BOOK_PREFIX, String ORDER_SUMMARY_LIST_PREFIX) {
        // Get order book.
        RMap<Long, OrderPage> tokenOrderBook = redissonClient.getMap(ORDER_BOOK_PREFIX + tokenNo);
        // Get order page.
        OrderPage orderPage = getOrderPage(tokenNo, price, tokenOrderBook, ORDER_SUMMARY_LIST_PREFIX);
        // Get order summary list.
        RList<OrderSummary> orderSummaryRList = orderPage.getOrderSummaryRList();
        // Get order summary.
        if (orderSummaryRList.isEmpty()) {
            return null;
        }
        int FIRST_INDEX = 0;
        return orderSummaryRList.get(FIRST_INDEX);
    }

    public Boolean saveBuyOrderSummary(Long tokenNo, Long price, OrderSummary orderSummary) {
        return saveOrderSummary(tokenNo, price, orderSummary, REDIS_TOKEN_BUY_ORDER_BOOK_PREFIX, REDIS_TOKEN_BUY_ORDER_SUMMARY_LIST_PREFIX);
    }

    public Boolean saveSellOrderSummary(Long tokenNo, Long price, OrderSummary orderSummary) {
        return saveOrderSummary(tokenNo, price, orderSummary, REDIS_TOKEN_SELL_ORDER_BOOK_PREFIX, REDIS_TOKEN_SELL_ORDER_SUMMARY_LIST_PREFIX);
    }

    private Boolean saveOrderSummary(Long tokenNo, Long price, OrderSummary orderSummary, String ORDER_BOOK_PREFIX, String ORDER_SUMMARY_LIST_PREFIX) {
        // Get order book.
        RMap<Long, OrderPage> tokenOrderBook = redissonClient.getMap(ORDER_BOOK_PREFIX + tokenNo);
        // Get order page.
        OrderPage orderPage = getOrderPage(tokenNo, price, tokenOrderBook, ORDER_SUMMARY_LIST_PREFIX);
        // Update volume of page.
        orderPage.setVolume(orderPage.getVolume() + orderSummary.getRemain());
        putOrderPage(price, orderPage, tokenOrderBook);
        // Update order summary list of page.
        return orderPage.getOrderSummaryRList().add(orderSummary);
    }

    public OrderSummary changeBuyOrderSummary(Long tokenNo, Long price, OrderSummary orderSummary) {
        return changeOrderSummary(tokenNo, price, orderSummary, REDIS_TOKEN_BUY_ORDER_BOOK_PREFIX, REDIS_TOKEN_BUY_ORDER_SUMMARY_LIST_PREFIX);
    }

    public OrderSummary changeSellOrderSummary(Long tokenNo, Long price, OrderSummary orderSummary) {
        return changeOrderSummary(tokenNo, price, orderSummary, REDIS_TOKEN_SELL_ORDER_BOOK_PREFIX, REDIS_TOKEN_SELL_ORDER_SUMMARY_LIST_PREFIX);
    }

    private OrderSummary changeOrderSummary(Long tokenNo, Long price, OrderSummary orderSummary, String ORDER_BOOK_PREFIX, String ORDER_SUMMARY_LIST_PREFIX) {
        // Get order book.
        RMap<Long, OrderPage> tokenOrderBook = redissonClient.getMap(ORDER_BOOK_PREFIX + tokenNo);
        // Get order page.
        OrderPage orderPage = getOrderPage(tokenNo, price, tokenOrderBook, ORDER_SUMMARY_LIST_PREFIX);
        // Get order summary list.
        RList<OrderSummary> orderSummaryRList = orderPage.getOrderSummaryRList();
        // Get order summary.
        int FIRST_INDEX = 0;
        OrderSummary unchangedOrderSummary = orderSummaryRList.get(FIRST_INDEX);
        // Update volume of page.
        orderPage.setVolume(orderPage.getVolume() - unchangedOrderSummary.getRemain() + orderSummary.getRemain());
        putOrderPage(price, orderPage, tokenOrderBook);
        // Update order summary list of page.
        return orderSummaryRList.set(FIRST_INDEX, orderSummary);
    }

    public void deleteBuyOrderSummary(Long tokenNo, Long price) {
        deleteOrderSummary(tokenNo, price, REDIS_TOKEN_BUY_ORDER_BOOK_PREFIX, REDIS_TOKEN_BUY_ORDER_SUMMARY_LIST_PREFIX);
    }

    public void deleteSellOrderSummary(Long tokenNo, Long price) {
        deleteOrderSummary(tokenNo, price, REDIS_TOKEN_SELL_ORDER_BOOK_PREFIX, REDIS_TOKEN_SELL_ORDER_SUMMARY_LIST_PREFIX);
    }

    private void deleteOrderSummary(Long tokenNo, Long price, String ORDER_BOOK_PREFIX, String ORDER_SUMMARY_LIST_PREFIX) {
        // Get order book.
        RMap<Long, OrderPage> tokenOrderBook = redissonClient.getMap(ORDER_BOOK_PREFIX + tokenNo);
        // Get order page.
        OrderPage orderPage = getOrderPage(tokenNo, price, tokenOrderBook, ORDER_SUMMARY_LIST_PREFIX);
        // Get order summary list.
        RList<OrderSummary> orderSummaryRList = orderPage.getOrderSummaryRList();
        // Get order summary.
        int FIRST_INDEX = 0;
        OrderSummary orderSummary = orderSummaryRList.get(FIRST_INDEX);
        // Update volume of page.
        orderPage.setVolume(orderPage.getVolume() - orderSummary.getRemain());
        // Delete order summary.
        orderSummaryRList.fastRemove(FIRST_INDEX);
        // Check if order summary list is empty.
        if (orderSummaryRList.isEmpty()) {
            Double INITIAL_VOLUME = 0.0;
            orderPage.setVolume(INITIAL_VOLUME);
        }
        putOrderPage(price, orderPage, tokenOrderBook);
    }

    private OrderPage getOrderPage(Long tokenNo, Long price, RMap<Long, OrderPage> tokenOrderBook, String ORDER_SUMMARY_LIST_PREFIX) {
        // Get existing order page.
        if(tokenOrderBook.containsKey(price)) {
            return tokenOrderBook.get(price);
        }
        // Price has empty list. Create order list first.
        OrderPage orderPage = new OrderPage();
        Double INITIAL_VOLUME = 0.0;
        orderPage.setVolume(INITIAL_VOLUME);
        orderPage.setOrderSummaryRList(redissonClient.getList(ORDER_SUMMARY_LIST_PREFIX + tokenNo + ":" + price));
        return orderPage;
    }

    private void putOrderPage(Long price, OrderPage orderPage, RMap<Long, OrderPage> tokenOrderBook) {
        // Put order page.
        tokenOrderBook.fastPut(price, orderPage);
    }

    public Double findBuyVolumeByPriceAtOrderBook(Long tokenNo, Long price) {
        return findVolumeByPriceAtOrderBook(tokenNo, price, REDIS_TOKEN_BUY_ORDER_BOOK_PREFIX);
    }

    public Double findSellVolumeByPriceAtOrderBook(Long tokenNo, Long price) {
        return findVolumeByPriceAtOrderBook(tokenNo, price, REDIS_TOKEN_SELL_ORDER_BOOK_PREFIX);
    }

    private Double findVolumeByPriceAtOrderBook(Long tokenNo, Long price, String ORDER_BOOK_PREFIX) {
        // Get order book.
        RMap<Long, OrderPage> tokenOrderBook = redissonClient.getMap(ORDER_BOOK_PREFIX + tokenNo);
        // Get existing order page.
        if(tokenOrderBook.containsKey(price)) {
            return tokenOrderBook.get(price).getVolume();
        }
        // There is no volume.
        return 0.0;
    }

    public List<Double> findBuyVolumeByPriceAtOrderBook(Long tokenNo, List<Long> priceList) {
        return findVolumeByPriceAtOrderBook(tokenNo, priceList, REDIS_TOKEN_BUY_ORDER_BOOK_PREFIX);
    }

    public List<Double> findSellVolumeByPriceAtOrderBook(Long tokenNo, List<Long> priceList) {
        return findVolumeByPriceAtOrderBook(tokenNo, priceList, REDIS_TOKEN_SELL_ORDER_BOOK_PREFIX);
    }

    private List<Double> findVolumeByPriceAtOrderBook(Long tokenNo, List<Long> priceList, String ORDER_BOOK_PREFIX) {
        // Get order book.
        RMap<Long, OrderPage> tokenOrderBook = redissonClient.getMap(ORDER_BOOK_PREFIX + tokenNo);
        // Generate return list.
        List<Double> volumeList = new ArrayList<>(priceList.size());
        for (long price: priceList) {
            // Get existing order page.
            if (tokenOrderBook.containsKey(price)) {
                volumeList.add(tokenOrderBook.get(price).getVolume());
            }
            // There is no volume.
            volumeList.add(0.0);
        }
        return volumeList;
    }

    //////////////////////////////////
    /* --- Total Volume Methods --- */
    //////////////////////////////////

    public Double findBuyTotalVolume(Long tokenNo) {
        RBucket<Double> buyTotalVolumeRBucket = redissonClient.getBucket(REDIS_TOKEN_BUY_TOTAL_VOLUME_PREFIX + tokenNo);
        return buyTotalVolumeRBucket.get();
    }

    public Double findSellTotalVolume(Long tokenNo) {
        RBucket<Double> sellTotalVolumeRBucket = redissonClient.getBucket(REDIS_TOKEN_SELL_TOTAL_VOLUME_PREFIX + tokenNo);
        return sellTotalVolumeRBucket.get();
    }

    public Double findTradeTotalVolume(Long tokenNo) {
        RBucket<Double> tradeTotalVolumeRBucket = redissonClient.getBucket(REDIS_TOKEN_TRADE_TOTAL_VOLUME_PREFIX + tokenNo);
        return tradeTotalVolumeRBucket.get();
    }

    public void changeBuyTotalVolume(Long tokenNo, Double changedVolume) {
        RBucket<Double> buyTotalVolumeRBucket = redissonClient.getBucket(REDIS_TOKEN_BUY_TOTAL_VOLUME_PREFIX + tokenNo);
        buyTotalVolumeRBucket.set(buyTotalVolumeRBucket.get() + changedVolume);
    }

    public void changeSellTotalVolume(Long tokenNo, Double changedVolume) {
        RBucket<Double> sellTotalVolumeRBucket = redissonClient.getBucket(REDIS_TOKEN_SELL_TOTAL_VOLUME_PREFIX + tokenNo);
        sellTotalVolumeRBucket.set(sellTotalVolumeRBucket.get() + changedVolume);
    }

    public void changeTradeTotalVolume(Long tokenNo, Double changedVolume) {
        RBucket<Double> tradeTotalVolumeRBucket = redissonClient.getBucket(REDIS_TOKEN_TRADE_TOTAL_VOLUME_PREFIX + tokenNo);
        tradeTotalVolumeRBucket.set(tradeTotalVolumeRBucket.get() + changedVolume);
    }

    /////////////////////////////////
    /* --- Volume Book Methods --- */
    /////////////////////////////////

    public VolumePage findMaxBuyVolumePage(Long tokenNo) {
        RScoredSortedSet<VolumePage> tokenBuyVolumeBook = redissonClient.getScoredSortedSet(REDIS_TOKEN_BUY_VOLUME_BOOK_PREFIX + tokenNo);
        // Sell price must be bigger than this buy price.
        if (tokenBuyVolumeBook.isEmpty()) {
            return new VolumePage(PriceConstant.MIN_PRICE, 0.0);
        }
        return tokenBuyVolumeBook.last();
    }

    public VolumePage findMinSellVolumePage(Long tokenNo) {
        RScoredSortedSet<VolumePage> tokenSellVolumeBook = redissonClient.getScoredSortedSet(REDIS_TOKEN_SELL_VOLUME_BOOK_PREFIX + tokenNo);
        // Buy price must be smaller than this sell price.
        if (tokenSellVolumeBook.isEmpty()) {
            return new VolumePage(PriceConstant.MAX_PRICE, 0.0);
        }
        return tokenSellVolumeBook.first();
    }

    public Boolean saveBuyVolumePage(Long tokenNo, VolumePage volumePage) {
        RScoredSortedSet<VolumePage> tokenBuyVolumeBook = redissonClient.getScoredSortedSet(REDIS_TOKEN_BUY_VOLUME_BOOK_PREFIX + tokenNo);
        return tokenBuyVolumeBook.add(volumePage.getPrice().doubleValue(), volumePage);
    }

    public Boolean saveSellVolumePage(Long tokenNo, VolumePage volumePage) {
        RScoredSortedSet<VolumePage> tokenSellVolumeBook = redissonClient.getScoredSortedSet(REDIS_TOKEN_SELL_VOLUME_BOOK_PREFIX + tokenNo);
        return tokenSellVolumeBook.add(volumePage.getPrice().doubleValue(), volumePage);
    }

    public VolumePage deleteBuyVolumePage(Long tokenNo) {
        RScoredSortedSet<VolumePage> tokenBuyVolumeBook = redissonClient.getScoredSortedSet(REDIS_TOKEN_BUY_VOLUME_BOOK_PREFIX + tokenNo);
        return tokenBuyVolumeBook.pollLast();
    }

    public VolumePage deleteSellVolumePage(Long tokenNo) {
        RScoredSortedSet<VolumePage> tokenSellVolumeBook = redissonClient.getScoredSortedSet(REDIS_TOKEN_SELL_VOLUME_BOOK_PREFIX + tokenNo);
        return tokenSellVolumeBook.pollFirst();
    }
}