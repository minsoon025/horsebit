package com.a406.horsebit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a406.horsebit.domain.Order;
import com.a406.horsebit.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {
	private final OrderRepository orderRepository;

	@Autowired
	public OrderServiceImpl(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@Override
	public List<Order> getOrders() {
		return orderRepository.findAll();
	}
}
