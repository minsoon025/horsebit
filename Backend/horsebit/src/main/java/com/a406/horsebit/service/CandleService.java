package com.a406.horsebit.service;

import com.a406.horsebit.dto.CandleDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface CandleService {
    CandleDTO getCandle(Long tokenNo, LocalDateTime endTime, Integer candleTypeIndex);
    List<CandleDTO> getCandle(Long tokenNo, LocalDateTime endTime, Integer candleTypeIndex, Long quantity, Long margin);
}
