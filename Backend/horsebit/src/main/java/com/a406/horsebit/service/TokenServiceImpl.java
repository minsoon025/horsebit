package com.a406.horsebit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a406.horsebit.domain.Bookmark;
import com.a406.horsebit.dto.PriceDTO;
import com.a406.horsebit.dto.TokenDTO;
import com.a406.horsebit.repository.TokenRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {
	private final TokenRepository tokenRepository;
	private final PriceService priceService;

	@Autowired
	public TokenServiceImpl(TokenRepository tokenRepository, PriceService priceService) {
		this.tokenRepository = tokenRepository;
		this.priceService = priceService;
	}

	public List<TokenDTO> setTokenRedisInfos(List<Long> tokens) {
		log.info("BookmarkServiceImpl::findAll() START");
		List<TokenDTO> result = new ArrayList<>();

		//
		// for(TokenDTO token : tokens) {
		// 	Long tokenNo = token.getTokenNo();
		// 	TokenDTO cToken = tokenRepository.findTokenByTokenNo(tokenNo);
		//
		// 	//2-1)각 token의 실시간 정보 조회(redis)
		// 	token.setCurrentPrice(priceService.getCurrentPrice(tokenNo).getPrice());
		// 	token.setPriceRateOfChange(priceService.getPriceOfRate(tokenNo).getPriceRateOfChange());
		// 	//TODO: redis-api merge 받아와서 repository 함수 활용하여 set하기
		// 	token.setVolume(2.3);
		//
		// 	result.add(token);
		// }

		log.info("BookmarkServiceImpl::findAll() result : " + result.toString());
		return result;
	}

	@Override
	public List<TokenDTO> findAllTokens() {
		List<TokenDTO> result = tokenRepository.findAllTokens();
		List<PriceDTO> prices = priceService.getCurrentPrice();
		//result = setTokenRedisInfos(result); //TODO: 검토 필요. result를 보내서 다시 result를 받아도 되나?

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
		List<PriceDTO> prices = priceService.getCurrentPrice(tokensNo);
		//List<>

		for(Long tokenNo : tokensNo) {
			TokenDTO token = findOneToken(tokenNo);

			//token.set~ (prices[i]);

			result.add(token);
		}

		return result;
	}

	//TODO: assetService로 뺄지 검토 필요
	@Override
	public List<Long> findPossessTokens(Long userNo) {
		log.info("TokenServiceImpl::findPossessTokens() START");
		List<Long> result = new ArrayList<>();

		//TODO: asset에서 받아오기


		return result;
	}
}
