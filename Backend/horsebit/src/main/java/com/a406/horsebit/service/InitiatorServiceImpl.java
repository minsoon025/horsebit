package com.a406.horsebit.service;

import com.a406.horsebit.domain.redis.VolumePage;
import com.a406.horsebit.repository.redis.CandleRepository;
import com.a406.horsebit.repository.redis.OrderRepository;
import com.a406.horsebit.repository.redis.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InitiatorServiceImpl implements InitiatorService {
    private final CandleRepository candleRepository;
    private final OrderRepository orderRepository;
    private final PriceRepository priceRepository;

    @Autowired
    InitiatorServiceImpl(CandleRepository candleRepository, OrderRepository orderRepository, PriceRepository priceRepository) {
        this.candleRepository = candleRepository;
        this.orderRepository = orderRepository;
        this.priceRepository = priceRepository;
    }

    @Override
    public void resetOrder() {
        orderRepository.newOrderNo();
    }

    @Override
    public void resetTokens(Long tokenNo, LocalDateTime initialTime, Long price) {
        candleRepository.setCandleInitialTime(tokenNo, initialTime);
        candleRepository.setInitialCandle(tokenNo, initialTime, price);

        orderRepository.newTotalVolume(tokenNo);

        while (!orderRepository.isBuyVolumeBookEmpty(tokenNo)) {
            VolumePage volumePage = orderRepository.findMaxBuyVolumePage(tokenNo);
            orderRepository.deleteBuyOrderSummaryList(tokenNo, volumePage.getPrice());
            orderRepository.deleteBuyVolumePage(tokenNo);
        }

        while (!orderRepository.isSellVolumeBookEmpty(tokenNo)) {
            VolumePage volumePage = orderRepository.findMinSellVolumePage(tokenNo);
            orderRepository.deleteSellOrderSummaryList(tokenNo, volumePage.getPrice());
            orderRepository.deleteSellVolumePage(tokenNo);
        }
    }

    @Override
    public void resetTokens(Long tokenNo) {
        LocalDateTime initialTime = LocalDateTime.parse("2023-09-18T00:00:00.00");
        resetTokens(tokenNo, initialTime, 10000L);
    }

    @Override
    public void resetPrice(Long tokenNo, Long price) {
        priceRepository.saveCurrentPrice(tokenNo, price);
        priceRepository.saveStartPrice(tokenNo, price);
    }

    @Override
    public void resetPrice(Long tokenNo) {
        priceRepository.newCurrentPrice(tokenNo);
        priceRepository.newStartPrice(tokenNo);
    }

    public void resetUser(Long userNo, Long tokenNo) {
        orderRepository.resetUserOrderList(userNo, tokenNo);
    }
}
