package com.a406.horsebit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.a406.horsebit.dto.TokenDTO;
import com.a406.horsebit.service.TokenService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/exchange/tokens")
@RestController
public class TokenController {
	private final TokenService tokenService;

	@Autowired
	public TokenController(TokenService tokenService) {
		this.tokenService = tokenService;
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
}
