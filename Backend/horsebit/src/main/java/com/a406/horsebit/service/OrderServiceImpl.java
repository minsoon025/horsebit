package com.a406.horsebit.service;

import java.util.List;

import com.a406.horsebit.repository.OrderRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a406.horsebit.dto.OrderDTO;
import com.a406.horsebit.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
	private final OrderRepository orderRepository;

	private final OrderRedisRepository orderRedisRepository;

	@Autowired
	public OrderServiceImpl(OrderRepository orderRepository, OrderRedisRepository orderRedisRepository) {
		this.orderRepository = orderRepository;
		this.orderRedisRepository = orderRedisRepository;
	}

	@Override
	public List<OrderDTO> getOrders(Long userNo, Long tokenNo) {
		log.info("OrderServiceImpl::getOrders() START");
		return orderRedisRepository.findAllByUserNoAndTokenNoAndCode(userNo, tokenNo, "A");
		//return orderRepository.findAllByUserNoAndTokenNo(userNo, tokenNo);
	}
}
