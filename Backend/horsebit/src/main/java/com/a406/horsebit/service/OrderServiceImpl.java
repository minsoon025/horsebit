package com.a406.horsebit.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a406.horsebit.dto.OrderDTO;
import com.a406.horsebit.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {
	private final OrderRepository orderRepository;

	@Autowired
	public OrderServiceImpl(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@Override
	public Optional<OrderDTO> getOrders(Long userNo, Long tokenNo) {
		return orderRepository.findByUserNoAndTokenNo(userNo, tokenNo);
	}
}
