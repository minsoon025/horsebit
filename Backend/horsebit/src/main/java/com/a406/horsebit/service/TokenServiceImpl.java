package com.a406.horsebit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a406.horsebit.domain.Bookmark;
import com.a406.horsebit.domain.Possess;
import com.a406.horsebit.dto.PriceDTO;
import com.a406.horsebit.dto.PriceRateOfChangeDTO;
import com.a406.horsebit.dto.TokenDTO;
import com.a406.horsebit.repository.PossessRepository;
import com.a406.horsebit.repository.TokenRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {
	private final TokenRepository tokenRepository;
	private final PossessRepository possessRepository;
	private final PriceService priceService;

	private final Long KRW_NO = 11L;

	@Autowired
	public TokenServiceImpl(TokenRepository tokenRepository, PossessRepository possessRepository, PriceService priceService) {
		this.tokenRepository = tokenRepository;
		this.possessRepository = possessRepository;
		this.priceService = priceService;
	}

	@Override
	public List<TokenDTO> findAllTokens() {
		List<Long> tokensNo = tokenRepository.findAllTokenNos();
		List<TokenDTO> result = findTokens(tokensNo);

		log.debug("TokenServiceImpl::getAllTokens() START" + result.toString());
		return result;
	}

	public TokenDTO findOneToken(Long tokenNo) {
		return tokenRepository.findTokenByTokenNo(tokenNo);
	}

	@Override
	public List<TokenDTO> findTokens(List<Long> tokensNo) {
		log.info("TokenServiceImpl::findTokens() START");
		List<TokenDTO> result = new ArrayList<>();
		List<PriceDTO> rPrices = priceService.getCurrentPrice(tokensNo);
		List<PriceRateOfChangeDTO> rRates = priceService.getPriceOfRate(tokensNo);

		if(!(tokensNo.size() == rPrices.size() && tokensNo.size() == rRates.size())) {
			//TODO: 반환값 바꾸기
			return null;
		}

		int ind = 0;
		for(Long tokenNo : tokensNo) {
			TokenDTO token = findOneToken(tokenNo);
			token.setCurrentPrice(rPrices.get(ind).getPrice());
			token.setPriceRateOfChange(rRates.get(ind).getPriceRateOfChange());
			token.setVolume(0); //TODO: volume redis 접근 및 반환
			ind++;

			result.add(token);
		}

		return result;
	}

	@Override
	public List<Long> findPossessTokens(Long userNo) {
		log.info("TokenServiceImpl::findPossessTokens() START");
		List<Long> result = new ArrayList<>();
		List<Possess> possesses = possessRepository.findPossessesByUserNo(userNo);
		for(Possess possess : possesses) {
			if(possess.getTokenNo() == KRW_NO) continue;
			result.add(possess.getTokenNo());
		}
		
		return result;
	}
}
