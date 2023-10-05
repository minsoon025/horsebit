package com.a406.horsebit.controller;

import java.text.ParseException;
import java.util.List;

import com.a406.horsebit.domain.User;
import com.a406.horsebit.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

	@PostMapping("/order/buy")
	public OrderResponseDTO postBuyOrder(HttpServletRequest request, HttpServletResponse response, @RequestBody OrderRequestDTO orderRequestDTO) throws ParseException {
		String accessToken = (request.getHeader("Authorization")).substring("Bearer ".length());
		User user = userService.userInfoFromToken(accessToken);
		Long userNo = user.getId();
		String orderStatus = orderService.processBuyOrder(
				userNo,
				orderRequestDTO.getTokenNo(),
				new Order(orderRequestDTO, OrderConstant.BUY_FLAG)
		);
		return new OrderResponseDTO(orderStatus);
	}

	@PostMapping("/order/sell")
	public OrderResponseDTO postSellOrder(HttpServletRequest request, HttpServletResponse response, @RequestBody OrderRequestDTO orderRequestDTO) throws ParseException {
		String accessToken = (request.getHeader("Authorization")).substring("Bearer ".length());
		User user = userService.userInfoFromToken(accessToken);
		Long userNo = user.getId();
		String orderStatus = orderService.processSellOrder(
				userNo,
				orderRequestDTO.getTokenNo(),
				new Order(orderRequestDTO, OrderConstant.SELL_FLAG)
		);
		return new OrderResponseDTO(orderStatus);
	}

	@PostMapping("/tokens/{tokenNo}/buy/{userNo}")
	public OrderResponseDTO buyDataFiller(@PathVariable("tokenNo") Long tokenNo, @PathVariable("userNo") Long userNo, @RequestBody Order order) {
		String orderStatus = orderService.processBuyOrder(
				userNo,
				tokenNo,
				order,
				order.getOrderTime()
		);
		return new OrderResponseDTO(orderStatus);
	}

	@PostMapping("/tokens/{tokenNo}/sell/{userNo}")
	public OrderResponseDTO sellDataFiller(@PathVariable("tokenNo") Long tokenNo, @PathVariable("userNo") Long userNo, @RequestBody Order order) {
		String orderStatus = orderService.processSellOrder(
				userNo,
				tokenNo,
				order,
				order.getOrderTime()
		);
		return new OrderResponseDTO(orderStatus);
	}
}
