package com.a406.horsebit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.a406.horsebit.domain.Order;
import com.a406.horsebit.service.OrderService;

@Controller
public class OrderController {
	private final OrderService orderService;

	@Autowired
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@GetMapping("api/exchange/orders")
	@ResponseBody
	public List<Order> getOrders() {
		System.out.println("OrderController Start");
		return orderService.getOrders();
	}
}
