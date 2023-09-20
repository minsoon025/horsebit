package com.a406.horsebit.controller;

import java.util.List;

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

	@Autowired
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	//TODO: userNo는 GET요청의 헤더의 유효성 검사하는 AOP 개발 후 제거 예정
	@GetMapping("/orders/{tokenNo}")
	public List<OrderDTO> getTokenOrders(@PathVariable("tokenNo") Long tokenNo) {
		log.info("OrderController::getTokenOrders() START");
		Long userNo = 1L;
		return orderService.getOrders(userNo, tokenNo)
			.stream().toList();
	}
}
