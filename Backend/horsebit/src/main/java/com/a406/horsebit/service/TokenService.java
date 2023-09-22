package com.a406.horsebit.service;

import java.util.List;

import com.a406.horsebit.dto.TokenDTO;

public interface TokenService {
	List<TokenDTO> getAllTokens();
	// TokenDTO getTokenByTokenNo();
}
