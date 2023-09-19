package com.a406.horsebit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.a406.horsebit.domain.Order;
import com.a406.horsebit.dto.OrderDTO;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	/**
	 * 미체결 내역 조회 JPQL
	 * @param userNo
	 * @param tokenNo
	 * @return
	 */
	@Query("SELECT NEW com.a406.horsebit.dto.OrderDTO(o.orderNo, o.userNo, o.token.tokenNo, o.token.code, o.price, o.quantity, o.remain, o.orderTime, o.sellBuyFlag) from Order o left join o.token where o.userNo = :userNo and o.token.tokenNo = :tokenNo")
	Optional<OrderDTO> findByUserNoAndTokenNo(Long userNo, Long tokenNo);
}
