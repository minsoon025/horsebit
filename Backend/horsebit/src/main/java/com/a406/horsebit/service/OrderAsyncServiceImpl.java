package com.a406.horsebit.service;

import com.a406.horsebit.domain.Token;
import com.a406.horsebit.domain.Trade;
import com.a406.horsebit.domain.redis.Order;
import com.a406.horsebit.repository.TokenRepository;
import com.a406.horsebit.repository.TradeRepository;
import com.a406.horsebit.repository.redis.CandleRepository;
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
        saveTrade(
                tokenRepository.findById(tokenNo).get(),
                (int) price,
                quantity,
                Timestamp.valueOf(tradeTime),
                buyOrderNo,
                buyUserNo,
                Timestamp.valueOf(tradeTime),
                sellOrderNo,
                sellUserNo,
                Timestamp.valueOf(sellOrder.getOrderTime()),
                "B"
        );
        saveTrade(
                tokenRepository.findById(tokenNo).get(),
                (int) price,
                quantity,
                Timestamp.valueOf(tradeTime),
                buyOrderNo,
                buyUserNo,
                Timestamp.valueOf(tradeTime),
                sellOrderNo,
                sellUserNo,
                Timestamp.valueOf(sellOrder.getOrderTime()),
                "S"
        );
    }

    public void sellExecuteTrade(long price, double quantity, long tokenNo, long buyOrderNo, long buyUserNo, Order buyOrder, long sellOrderNo, long sellUserNo, LocalDateTime tradeTime) {
        saveTrade(
                tokenRepository.findById(tokenNo).get(),
                (int) price,
                quantity,
                Timestamp.valueOf(tradeTime),
                buyOrderNo,
                buyUserNo,
                Timestamp.valueOf(buyOrder.getOrderTime()),
                sellOrderNo,
                sellUserNo,
                Timestamp.valueOf(tradeTime),
                "B"
        );
        saveTrade(
                tokenRepository.findById(tokenNo).get(),
                (int) price,
                quantity,
                Timestamp.valueOf(tradeTime),
                buyOrderNo,
                buyUserNo,
                Timestamp.valueOf(buyOrder.getOrderTime()),
                sellOrderNo,
                sellUserNo,
                Timestamp.valueOf(tradeTime),
                "S"
        );
    }

    private void saveTrade(Token token, int price, Double quantity, Timestamp timestamp, Long buyerOrderNo, Long buyerUserNo, Timestamp buyerOrderTime, Long sellerOrderNo, Long sellerUserNo, Timestamp sellerOrderTime, String sellBuyFlag) {
        Trade trade = new Trade();
        trade.setToken(token);
        trade.setPrice(price);
        trade.setQuantity(quantity);
        trade.setTimestamp(timestamp);
        trade.setBuyerOrderNo(buyerOrderNo);
        trade.setBuyerUserNo(buyerUserNo);
        trade.setBuyerOrderTime(buyerOrderTime);
        trade.setSellerOrderNo(sellerOrderNo);
        trade.setSellerUserNo(sellerUserNo);
        trade.setSellerOrderTime(sellerOrderTime);
        trade.setSellBuyFlag(sellBuyFlag);
        tradeRepository.save(trade);
    }
}
