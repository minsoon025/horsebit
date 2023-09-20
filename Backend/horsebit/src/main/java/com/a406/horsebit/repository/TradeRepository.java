package com.a406.horsebit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.a406.horsebit.domain.Trade;
import com.a406.horsebit.dto.TradeDTO;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
	/**
	 * 체결 내역 조회
	 * @param userNo
	 * @param tokenNo
	 * @return
	 */
	@Query("select NEW com.a406.horsebit.dto.TradeDTO(tr.executionNo, tr.token.tokenNo, tr.token.code, tr.price, tr.quantity, tr.timestamp, tr.sellBuyFlag) from Trade tr left join tr.token where tr.token.tokenNo = :tokenNo and ((tr.sellerUserNo = :userNo and tr.sellBuyFlag = 'S') or (tr.buyerUserNo = :userNo and tr.sellBuyFlag = 'B'))")
	List<TradeDTO> findAllByUserNoAndTokenNo(Long userNo, Long tokenNo);
}
