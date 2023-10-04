package com.a406.horsebit.controller;

import java.util.List;

import com.a406.horsebit.constant.OrderConstant;
import com.a406.horsebit.domain.redis.Order;
import com.a406.horsebit.dto.OrderRequestDTO;
import com.a406.horsebit.dto.OrderResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
		return orderService.getOrders(userNo, tokenNo);
	}

	@PostMapping("/order/buy")
	public OrderResponseDTO postBuyOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
		String orderStatus = orderService.processBuyOrder(
				1L,                     // TODO: Login token 기능 개발 완료 후 삭제
				orderRequestDTO.getTokenNo(),
				new Order(orderRequestDTO, OrderConstant.BUY_FLAG)
		);
		return new OrderResponseDTO(orderStatus);
	}

	@PostMapping("/order/sell")
	public OrderResponseDTO postSellOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
		String orderStatus = orderService.processSellOrder(
				1L,                     // TODO: Login token 기능 개발 완료 후 삭제
				orderRequestDTO.getTokenNo(),
				new Order(orderRequestDTO, OrderConstant.SELL_FLAG)
		);
		return new OrderResponseDTO(orderStatus);
	}

	// TODO: temporary method. delete after access token feature develop.
	@PostMapping("/order/buy/{userNo}")
	public OrderResponseDTO postBuyOrder(@RequestBody OrderRequestDTO orderRequestDTO, @PathVariable("userNo") Long userNo) {
		String orderStatus = orderService.processBuyOrder(
				userNo,
				orderRequestDTO.getTokenNo(),
				new Order(orderRequestDTO, OrderConstant.BUY_FLAG)
		);
		return new OrderResponseDTO(orderStatus);
	}

	// TODO: temporary method. delete after access token feature develop.
	@PostMapping("/order/sell/{userNo}")
	public OrderResponseDTO postSellOrder(@RequestBody OrderRequestDTO orderRequestDTO, @PathVariable("userNo") Long userNo) {
		String orderStatus = orderService.processSellOrder(
				userNo,
				orderRequestDTO.getTokenNo(),
				new Order(orderRequestDTO, OrderConstant.SELL_FLAG)
		);
		return new OrderResponseDTO(orderStatus);
	}
}
