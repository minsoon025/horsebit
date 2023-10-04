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

import com.a406.horsebit.dto.OrderDTO;
import com.a406.horsebit.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/exchange")
@RestController
public class OrderController {
	private final OrderService orderService;
	private final UserService userService;

	@Autowired
	public OrderController(OrderService orderService, UserService userService) {
		this.orderService = orderService;
		this.userService = userService;
	}

	@GetMapping("/orders/{tokenNo}")
	public List<OrderDTO> getTokenOrders(HttpServletRequest request, HttpServletResponse response, @PathVariable("tokenNo") Long tokenNo) throws ParseException {
		String accessToken = (request.getHeader("Authorization")).substring("Bearer ".length());
		User user = userService.userInfoFromToken(accessToken);
		Long userNo = user.getId();
		log.info("user id : {}", userNo);

		log.info("OrderController::getTokenOrders() START");
		return orderService.getOrders(userNo, tokenNo);
	}
}
