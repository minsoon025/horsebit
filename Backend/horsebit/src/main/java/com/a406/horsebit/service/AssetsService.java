package com.a406.horsebit.service;

import java.util.List;

import com.a406.horsebit.dto.AssetsDTO;
import com.a406.horsebit.dto.HorseTokenDTO;

public interface AssetsService {
	AssetsDTO findAssetsByUserNo(Long userNo);
	List<HorseTokenDTO> findTokensByUserNo(Long userNo);
}
