package com.a406.horsebit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.a406.horsebit.dto.TradeDTO;
import com.a406.horsebit.service.TradeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/exchange")
@RestController
public class TradeController {
	private final TradeService tradeService;

	@Autowired
	public TradeController(TradeService tradeService) {
		this.tradeService = tradeService;
	}

	//TODO: userNo는 GET요청의 헤더의 유효성 검사하는 AOP 개발 후 제거 예정
	@GetMapping("/executions/{tokenNo}")
	public List<TradeDTO> getTokenTrades(@PathVariable("tokenNo") Long tokenNo) {
		log.info("TradeController::getTokenTrades() START");
		Long userNo = 1L;
		return tradeService.getTrades(userNo, tokenNo);
	}
}
