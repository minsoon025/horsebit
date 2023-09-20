package com.a406.horsebit.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a406.horsebit.dto.OrderDTO;
import com.a406.horsebit.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
	private final OrderRepository orderRepository;

	@Autowired
	public OrderServiceImpl(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@Override
	public Optional<OrderDTO> getOrders(Long userNo, Long tokenNo) {
		log.info("OrderServiceImpl::getOrders() START");
		return orderRepository.findByUserNoAndTokenNo(userNo, tokenNo);
	}
}
