package com.a406.horsebit.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.a406.horsebit.domain.Order;

import jakarta.transaction.Transactional;

@Service
@Transactional
public interface OrderService {
	public List<Order> getOrders();
}
