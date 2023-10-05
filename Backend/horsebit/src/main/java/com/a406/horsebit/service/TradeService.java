package com.a406.horsebit.service;

import java.util.List;
import java.util.Optional;

import com.a406.horsebit.dto.TradeDTO;

public interface TradeService {
	/**
	 * 체결 내역 조회
	 * @param userNo
	 * @param tokenNo
	 * @return
	 */
	List<TradeDTO> getTrades(Long userNo, Long tokenNo);
}
