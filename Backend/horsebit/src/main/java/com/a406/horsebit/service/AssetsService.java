package com.a406.horsebit.service;

import java.util.List;

import com.a406.horsebit.dto.AssetsDTO;
import com.a406.horsebit.dto.HorseTokenDTO;
import com.a406.horsebit.dto.UserTradeDTO;

public interface AssetsService {
	AssetsDTO findAssetsByUserNo(Long userNo);
	List<HorseTokenDTO> findTokensByUserNo(Long userNo);
	List<UserTradeDTO> findTradeHistoryByUserNo(Long userNo);
	Long saveDepositWithdraw(Long userNo, Long amount);
	void saveNewAsset(Long userNo, Long amount);

	double findTokenByUserNoAndTokenNo(Long userNo, Long tokenNo);
}
