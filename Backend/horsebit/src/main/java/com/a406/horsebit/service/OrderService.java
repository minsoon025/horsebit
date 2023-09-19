package com.a406.horsebit.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.a406.horsebit.domain.Order;
import com.a406.horsebit.dto.OrderDTO;

import jakarta.transaction.Transactional;


public interface OrderService {
	/**
	 * 미체결 내역 조회
	 * @param userNo
	 * @param tokenNo
	 * @return
	 */
	Optional<OrderDTO> getOrders(Long userNo, Long tokenNo);
}
