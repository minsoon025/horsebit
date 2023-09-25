package com.a406.horsebit.service;

import com.a406.horsebit.dto.PriceDTO;

import java.util.List;

public interface PriceService {
    PriceDTO getCurrentPrice(Long tokenNo);
    List<PriceDTO> getCurrentPrice(List<Long> tokenNoList);
}
