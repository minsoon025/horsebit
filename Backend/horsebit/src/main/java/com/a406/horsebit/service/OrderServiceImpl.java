package com.a406.horsebit.service;

import java.util.List;

import com.a406.horsebit.repository.redis.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a406.horsebit.dto.OrderDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;

	@Autowired
	public OrderServiceImpl(OrderRepository orderRedisRepository) {
		this.orderRepository = orderRedisRepository;
	}

	@Override
	public List<OrderDTO> getOrders(Long userNo, Long tokenNo) {
		log.info("OrderServiceImpl::getOrders() START");
		return orderRepository.findAllOrder(userNo, tokenNo, "A");
	}
}
