package com.a406.horsebit.service;

import com.a406.horsebit.constant.CandleConstant;
import com.a406.horsebit.domain.redis.CandleType;
import com.a406.horsebit.dto.CandleDTO;
import com.a406.horsebit.repository.redis.CandleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class CandleServiceImpl implements CandleService {
    private final CandleRepository candleRepository;

    @Autowired
    CandleServiceImpl(CandleRepository candleRepository) {
        this.candleRepository = candleRepository;
    }

    private int getIndex(Long tokenNo, LocalDateTime endTime, Integer candleTypeIndex) {
        LocalDateTime tokenInitialTime = candleRepository.findCandleInitialTime(tokenNo);
        long minutes = ChronoUnit.MINUTES.between(tokenInitialTime, endTime);
        return (int) (minutes / CandleConstant.CANDLE_TYPE_LIST.get(candleTypeIndex).getCandleMinuteTime());
    }
    public CandleDTO getCandle(Long tokenNo, LocalDateTime endTime, Integer candleTypeIndex) {
        int index = getIndex(tokenNo, endTime, candleTypeIndex);
        if(index < 0) { return null; }
        CandleType candleType = CandleConstant.CANDLE_TYPE_LIST.get(candleTypeIndex);
        return candleRepository.findOneByTokenNo(tokenNo, index, candleType);
    }

    public List<CandleDTO> getCandle(Long tokenNo, LocalDateTime endTime, Integer candleTypeIndex, Long quantity, Long margin) {
        List<CandleDTO> candleDTOList = new ArrayList<CandleDTO>();
        int endIndex = getIndex(tokenNo, endTime, candleTypeIndex) + 1;
        int startIndex = endIndex - quantity.intValue() - margin.intValue();
        startIndex = Math.max(startIndex, 0);
        endIndex = Math.min(getIndex(tokenNo, LocalDateTime.now(), candleTypeIndex) + 1, endIndex + margin.intValue());
        CandleType candleType = CandleConstant.CANDLE_TYPE_LIST.get(candleTypeIndex);
        return candleRepository.findRangeByTokenNo(tokenNo, startIndex, endIndex - startIndex, candleType);
    }
}
