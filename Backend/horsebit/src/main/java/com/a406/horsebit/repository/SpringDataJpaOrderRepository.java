package com.a406.horsebit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.a406.horsebit.domain.Order;

public interface SpringDataJpaOrderRepository extends JpaRepository<Order, Long>, OrderRepository {


}
