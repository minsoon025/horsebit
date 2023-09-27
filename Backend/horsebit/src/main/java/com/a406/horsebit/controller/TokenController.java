package com.a406.horsebit.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.a406.horsebit.dto.CandleDTO;
import com.a406.horsebit.service.CandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.a406.horsebit.dto.TokenDTO;
import com.a406.horsebit.service.TokenService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/exchange/tokens")
@RestController
public class TokenController {
	private final TokenService tokenService;
	private final CandleService candleService;

	@Autowired
	public TokenController(TokenService tokenService, CandleService candleService) {
		this.tokenService = tokenService;
		this.candleService = candleService;
	}

	@GetMapping("")
	public List<TokenDTO> getTokens() {
		log.info("TokenController::getTokens() START");
		return tokenService.getAllTokens();
	}

	// @GetMapping("/{tokenNo}")
	// public TokenDTO getTokenDetail(@PathVariable Long tokenNo) {
	// 	log.info("TokenController::getTokenDetail() START");
	// 	return tokenService.getTokenByTokenNo();
	// }

	@GetMapping("/{tokenNo}/chart")
	public List<CandleDTO> getCandles(@PathVariable("tokenNo") Long tokenNo, @RequestParam("quantity") Long quantity, @RequestParam("endTime") LocalDateTime endTime, @RequestParam("candleTypeIndex") Integer candleTypeIndex, @RequestParam("margin") Long margin) {
		return candleService.getCandle(tokenNo, endTime, candleTypeIndex, quantity, margin);
	}
}
