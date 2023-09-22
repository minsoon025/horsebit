package com.a406.horsebit.service;

import java.util.ArrayList;
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

		//TODO: 아래의 테스트코드 삭제 요망
		List<Double> testTrend = new ArrayList<>();
		testTrend.add(-22.3);
		testTrend.add(-19.3);
		testTrend.add(-15.0);
		testTrend.add(0.0);
		testTrend.add(1.0);
		testTrend.add(17.0);
		testTrend.add(23.8);
		int i = 0;
		for(TokenDTO token : result) {
			token.setPriceTrend(testTrend.get(i++));
		}

		log.debug("TokenServiceImpl::getAllTokens() START" + result.toString());

		return result;
	}

	// @Override
	// public TokenDTO getTokenByTokenNo() {
	// 	return tokenRepository.findTokenByTokenNo();
	// }
}
