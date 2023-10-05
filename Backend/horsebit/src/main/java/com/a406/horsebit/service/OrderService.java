package com.a406.horsebit.service;

import java.time.LocalDateTime;
import java.util.List;

import com.a406.horsebit.domain.redis.Order;
import com.a406.horsebit.dto.OrderDTO;

public interface OrderService {
	/**
	 * 미체결 내역 조회
	 * @param userNo
	 * @param tokenNo
	 * @return
	 */
	List<OrderDTO> getOrders(Long userNo, Long tokenNo);
	String processBuyOrder(Long userNo, Long tokenNo, Order order);
	String processSellOrder(Long userNo, Long tokenNo, Order order);
	String processBuyOrder(Long userNo, Long tokenNo, Order order, LocalDateTime orderCaptureTime);
	String processSellOrder(Long userNo, Long tokenNo, Order order, LocalDateTime orderCaptureTime);
}
