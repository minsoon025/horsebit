package com.a406.horsebit.repository;

import java.util.List;
import java.util.Optional;

import com.a406.horsebit.domain.Order;

public interface OrderRepository {
	Order save(Order order);
	List<Order> findByUserNo(int userNo);
	List<Order> findByTokenNo(int tokenNo);
	List<Order> findAll();
}
