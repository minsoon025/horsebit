package com.a406.horsebit.service;

import com.a406.horsebit.domain.Trade;
import com.a406.horsebit.domain.redis.Order;
import com.a406.horsebit.repository.TokenRepository;
import com.a406.horsebit.repository.TradeRepository;
import com.a406.horsebit.repository.redis.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class OrderAsyncServiceImpl implements OrderAsyncService{
    private final OrderRepository orderRepository;
    private final TradeRepository tradeRepository;
    private final TokenRepository tokenRepository;

    @Autowired
    OrderAsyncServiceImpl(OrderRepository orderRepository, TradeRepository tradeRepository, TokenRepository tokenRepository) {
        this.orderRepository = orderRepository;
        this.tradeRepository = tradeRepository;
        this.tokenRepository = tokenRepository;
    }

    public void buyExecuteTrade(long price, double quantity, long tokenNo, long buyOrderNo, long buyUserNo, long sellOrderNo, long sellUserNo, Order sellOrder, LocalDateTime tradeTime) {
        Trade trade = new Trade();
        trade.setToken(tokenRepository.findById(tokenNo).get());
        trade.setPrice(((int) price));
        trade.setQuantity(quantity);
        trade.setBuyerOrderNo(buyOrderNo);
        trade.setBuyerUserNo(buyUserNo);
        trade.setBuyerOrderTime(Timestamp.valueOf(tradeTime));
        trade.setSellerOrderNo(sellOrderNo);
        trade.setSellerUserNo(sellUserNo);
        trade.setSellerOrderTime(Timestamp.valueOf(sellOrder.getOrderTime()));
        trade.setSellBuyFlag("B");
        tradeRepository.save(trade);
        trade.setSellBuyFlag("S");
        tradeRepository.save(trade);
    }

    public void sellExecuteTrade(long price, double quantity, long tokenNo, long buyOrderNo, long buyUserNo, Order buyOrder, long sellOrderNo, long sellUserNo, LocalDateTime tradeTime) {
        Trade trade = new Trade();
		trade.setToken(tokenRepository.findById(tokenNo).get());
        trade.setPrice(((int) price));
        trade.setQuantity(quantity);
        trade.setTimestamp(Timestamp.valueOf(tradeTime));
        trade.setBuyerOrderNo(buyOrderNo);
        trade.setBuyerUserNo(buyUserNo);
        trade.setBuyerOrderTime(Timestamp.valueOf(buyOrder.getOrderTime()));
        trade.setSellerOrderNo(sellOrderNo);
        trade.setSellerUserNo(sellUserNo);
        trade.setSellerOrderTime(Timestamp.valueOf(tradeTime));
        trade.setSellBuyFlag("B");
		tradeRepository.save(trade);
        trade.setSellBuyFlag("S");
		tradeRepository.save(trade);
    }
}
