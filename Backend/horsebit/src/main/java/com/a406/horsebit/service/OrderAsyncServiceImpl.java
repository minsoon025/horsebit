package com.a406.horsebit.service;

import com.a406.horsebit.domain.Trade;
import com.a406.horsebit.domain.redis.Order;
import com.a406.horsebit.repository.TokenRepository;
import com.a406.horsebit.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class OrderAsyncServiceImpl implements OrderAsyncService{
    private final TradeRepository tradeRepository;
    private final TokenRepository tokenRepository;

    @Autowired
    OrderAsyncServiceImpl(TradeRepository tradeRepository, TokenRepository tokenRepository) {
        this.tradeRepository = tradeRepository;
        this.tokenRepository = tokenRepository;
    }

    public void buyExecuteTrade(long price, double quantity, long tokenNo, long buyOrderNo, long buyUserNo, long sellOrderNo, long sellUserNo, Order sellOrder, LocalDateTime tradeTime) {
        Trade buyTrade = new Trade();
        buyTrade.setToken(tokenRepository.findById(tokenNo).get());
        buyTrade.setPrice(((int) price));
        buyTrade.setQuantity(quantity);
        buyTrade.setBuyerOrderNo(buyOrderNo);
        buyTrade.setBuyerUserNo(buyUserNo);
        buyTrade.setBuyerOrderTime(Timestamp.valueOf(tradeTime));
        buyTrade.setSellerOrderNo(sellOrderNo);
        buyTrade.setSellerUserNo(sellUserNo);
        buyTrade.setSellerOrderTime(Timestamp.valueOf(sellOrder.getOrderTime()));
        buyTrade.setSellBuyFlag("B");
        tradeRepository.save(buyTrade);

        Trade sellTrade = new Trade();
        sellTrade.setToken(tokenRepository.findById(tokenNo).get());
        sellTrade.setPrice(((int) price));
        sellTrade.setQuantity(quantity);
        sellTrade.setBuyerOrderNo(buyOrderNo);
        sellTrade.setBuyerUserNo(buyUserNo);
        sellTrade.setBuyerOrderTime(Timestamp.valueOf(tradeTime));
        sellTrade.setSellerOrderNo(sellOrderNo);
        sellTrade.setSellerUserNo(sellUserNo);
        sellTrade.setSellerOrderTime(Timestamp.valueOf(sellOrder.getOrderTime()));
        sellTrade.setSellBuyFlag("S");
        tradeRepository.save(sellTrade);
    }

    public void sellExecuteTrade(long price, double quantity, long tokenNo, long buyOrderNo, long buyUserNo, Order buyOrder, long sellOrderNo, long sellUserNo, LocalDateTime tradeTime) {
        Trade buyTrade = new Trade();
		buyTrade.setToken(tokenRepository.findById(tokenNo).get());
        buyTrade.setPrice(((int) price));
        buyTrade.setQuantity(quantity);
        buyTrade.setTimestamp(Timestamp.valueOf(tradeTime));
        buyTrade.setBuyerOrderNo(buyOrderNo);
        buyTrade.setBuyerUserNo(buyUserNo);
        buyTrade.setBuyerOrderTime(Timestamp.valueOf(buyOrder.getOrderTime()));
        buyTrade.setSellerOrderNo(sellOrderNo);
        buyTrade.setSellerUserNo(sellUserNo);
        buyTrade.setSellerOrderTime(Timestamp.valueOf(tradeTime));
        buyTrade.setSellBuyFlag("B");
        tradeRepository.save(buyTrade);

        Trade sellTrade = new Trade();
        sellTrade.setToken(tokenRepository.findById(tokenNo).get());
        sellTrade.setPrice(((int) price));
        sellTrade.setQuantity(quantity);
        sellTrade.setTimestamp(Timestamp.valueOf(tradeTime));
        sellTrade.setBuyerOrderNo(buyOrderNo);
        sellTrade.setBuyerUserNo(buyUserNo);
        sellTrade.setBuyerOrderTime(Timestamp.valueOf(buyOrder.getOrderTime()));
        sellTrade.setSellerOrderNo(sellOrderNo);
        sellTrade.setSellerUserNo(sellUserNo);
        sellTrade.setSellerOrderTime(Timestamp.valueOf(tradeTime));
        sellTrade.setSellBuyFlag("S");
        tradeRepository.save(sellTrade);
    }
}
