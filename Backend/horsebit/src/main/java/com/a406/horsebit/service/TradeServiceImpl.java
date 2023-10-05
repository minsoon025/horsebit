package com.a406.horsebit.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a406.horsebit.dto.TradeDTO;
import com.a406.horsebit.repository.TradeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TradeServiceImpl implements TradeService {
	private final TradeRepository tradeRepository;

	@Autowired
	public TradeServiceImpl(TradeRepository tradeRepository) {
		this.tradeRepository = tradeRepository;
	}

	@Override
	public List<TradeDTO> getTrades(Long userNo, Long tokenNo) {
		log.info("TradeServiceImpl::getTrades() START");
		return tradeRepository.findAllByUserNoAndTokenNo(userNo, tokenNo);
	}
}
