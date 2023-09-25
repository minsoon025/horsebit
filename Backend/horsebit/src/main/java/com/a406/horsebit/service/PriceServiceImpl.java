package com.a406.horsebit.service;

import com.a406.horsebit.dto.PriceDTO;
import com.a406.horsebit.dto.PriceRateOfChangeDTO;
import com.a406.horsebit.repository.redis.PriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PriceServiceImpl implements PriceService {
    private final PriceRepository priceRepository;

    @Autowired
    PriceServiceImpl(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public PriceDTO getCurrentPrice(Long tokenNo) {
        return priceRepository.findOneCurrentPriceByTokenNo(tokenNo);
    }

    @Override
    public List<PriceDTO> getCurrentPrice(List<Long> tokenNoList) {
        return priceRepository.findAllCurrentPriceByTokenNo(tokenNoList);
    }

    @Override
    public PriceRateOfChangeDTO getPriceOfRate(Long tokenNo, Long currentPrice) {
        double doubleStartPrice = priceRepository.findOneStartPriceByTokenNo(tokenNo).getPrice().doubleValue();
        return new PriceRateOfChangeDTO((currentPrice.doubleValue() - doubleStartPrice) / doubleStartPrice);
    }

    @Override
    public PriceRateOfChangeDTO getPriceOfRate(Long tokenNo) {
        Long currentPrice = priceRepository.findOneCurrentPriceByTokenNo(tokenNo).getPrice();
        return getPriceOfRate(tokenNo, currentPrice);
    }

    @Override
    public List<PriceRateOfChangeDTO> getPriceOfRate(List<Long> tokenNoList, List<PriceDTO> currentPriceDTOList) {
        List<PriceDTO> startPriceDTOList = priceRepository.findAllStartPriceByTokenNo(tokenNoList);
        List<PriceRateOfChangeDTO> doublePriceRateOfChangeList = new ArrayList<PriceRateOfChangeDTO>(currentPriceDTOList.size());
        int index = 0;
        for (PriceDTO currentPriceDTO: currentPriceDTOList) {
            double currentPrice = currentPriceDTO.getPrice().doubleValue();
            double startPrice = startPriceDTOList.get(index).getPrice().doubleValue();
            doublePriceRateOfChangeList.add(new PriceRateOfChangeDTO((currentPrice - startPrice) / startPrice));
        }
        return doublePriceRateOfChangeList;
    }

    @Override
    public List<PriceRateOfChangeDTO> getPriceOfRate(List<Long> tokenNoList) {
        List<PriceDTO> currentPriceDTOList = priceRepository.findAllCurrentPriceByTokenNo(tokenNoList);
        return getPriceOfRate(tokenNoList, currentPriceDTOList);
    }
}
