package com.a406.horsebit.service;

import java.util.List;

import com.a406.horsebit.domain.Token;
import com.a406.horsebit.dto.TokenDTO;

public interface TokenService {
	List<TokenDTO> findAllTokens();
	List<TokenDTO> findTokens(List<Long> tokensNo);
	List<Long> findPossessTokens(Long userNo);
}
