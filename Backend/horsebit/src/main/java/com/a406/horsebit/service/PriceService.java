package com.a406.horsebit.service;

import com.a406.horsebit.dto.PriceDTO;
import com.a406.horsebit.dto.PriceRateOfChangeDTO;

import java.util.List;

public interface PriceService {
    PriceDTO getCurrentPrice(Long tokenNo);
    List<PriceDTO> getCurrentPrice(List<Long> tokenNoList);
    PriceRateOfChangeDTO getPriceOfRate(Long tokenNo, Long currentPrice);
    PriceRateOfChangeDTO getPriceOfRate(Long tokenNo);
    List<PriceRateOfChangeDTO> getPriceOfRate(List<Long> tokenNoList, List<PriceDTO> currentPriceDTOList);
    List<PriceRateOfChangeDTO> getPriceOfRate(List<Long> tokenNoList);

}
