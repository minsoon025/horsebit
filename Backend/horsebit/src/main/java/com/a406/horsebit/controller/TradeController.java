package com.a406.horsebit.controller;

import java.text.ParseException;
import java.util.List;

import com.a406.horsebit.domain.User;
import com.a406.horsebit.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
	private final UserService userService;

	@Autowired
	public TradeController(TradeService tradeService, UserService userService) {
		this.tradeService = tradeService;
		this.userService = userService;
	}

	@GetMapping("/executions/{tokenNo}")
	public List<TradeDTO> getTokenTrades(HttpServletRequest request, HttpServletResponse response, @PathVariable("tokenNo") Long tokenNo) throws ParseException {
		String token = (request.getHeader("Authorization")).substring("Bearer ".length());
		User user = userService.userInfoFromToken(token);
		Long userNo = user.getId();
		log.info("user id : {}", userNo);
		log.info("TradeController::getTokenTrades() START");

		return tradeService.getTrades(userNo, tokenNo);
	}
}
