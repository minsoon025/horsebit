package com.a406.horsebit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a406.horsebit.dto.TokenDTO;
import com.a406.horsebit.repository.TokenRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {
	private final TokenRepository tokenRepository;

	@Autowired
	public TokenServiceImpl(TokenRepository tokenRepository) {
		this.tokenRepository = tokenRepository;
	}

	//TODO: 아래 refactoring 필요
	@Override
	public List<TokenDTO> getAllTokens() {
		List<TokenDTO> result = tokenRepository.findAllTokens();

		log.debug("TokenServiceImpl::getAllTokens() START" + result.toString());

		return result;
	}

	// @Override
	// public TokenDTO getTokenByTokenNo() {
	// 	return tokenRepository.findTokenByTokenNo();
	// }
}
