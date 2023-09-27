package com.a406.horsebit.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.a406.horsebit.domain.Possess;
import com.a406.horsebit.dto.AssetsDTO;
import com.a406.horsebit.dto.HorseTokenDTO;
import com.a406.horsebit.dto.PriceDTO;
import com.a406.horsebit.dto.TokenDTO;
import com.a406.horsebit.repository.PossessRepository;
import com.a406.horsebit.repository.TokenRepository;
import com.a406.horsebit.repository.redis.PriceRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AssetsServiceImpl implements AssetsService {
	private final PossessRepository possessRepository;
	
	private final PriceRepository priceRepository;

	private final TokenRepository tokenRepository;

	//TODO: KRW의 tokenNO를 공통 상수로 빼기
	private final Long KRW = 11L;

	public AssetsServiceImpl(PossessRepository possessRepository, PriceRepository priceRepository, TokenRepository tokenRepository) {
		this.possessRepository = possessRepository;
		this.priceRepository = priceRepository;
		this.tokenRepository = tokenRepository;
	}

	@Override
	public AssetsDTO findAssetsByUserNo(Long userNo) {
		AssetsDTO result = new AssetsDTO();

		List<Possess> possessesList = possessRepository.findPossessesByUserNo(userNo).stream().toList();

		double amtKRW = 0L; //잔여 현금
		double amtToken = 0L; //총 매수 금액
		Double amtEvaluation = 0.0; //총 평가
		Map<Long, Double> tokenMap = new HashMap<>(); // Key:tokenNo, Value:count

		//TODO: KRW의 tokenNo = 0 상수로 공통으로 빼기
		for(Possess possess : possessesList) {
			if(possess.getTokenNo().equals(KRW)) {
				amtKRW = possess.getTotalAmountPurchase();
				log.info("KRW IS FOUND" + possess.getQuantity());
			}
			else {
				amtToken += possess.getTotalAmountPurchase();

				Long curToken = possess.getTokenNo();
				Double curQuantity = possess.getQuantity();
				if(tokenMap.containsKey(curToken)) {
					Double cnt = tokenMap.get(curToken);
					cnt += curQuantity;
					tokenMap.replace(curToken, cnt);
				}
				else {
					tokenMap.put(curToken, curQuantity);
				}
			}
		}

		for(Map.Entry<Long, Double> token : tokenMap.entrySet()) {
			PriceDTO price = priceRepository .findCurrentPrice(token.getKey());
			log.info("CURRENT_PRICE IS FOUND" + price.getPrice());
			amtEvaluation += price.getPrice() * token.getValue();
		}

		result.setTotalAsset(amtKRW + amtEvaluation); //자산 총합
		result.setCashBalance(amtKRW); //잔여 현금
		result.setTotalPurchase(amtToken); //총 매수 금액
		result.setTotalEvaluation(amtEvaluation); //총 평가 금액
		result.setProfitOrLoss(amtEvaluation - amtToken); //평가 손익
		result.setReturnRate((amtEvaluation - amtToken)/amtToken); //수익률

		return result;
	}

	@Override
	public List<HorseTokenDTO> findTokensByUserNo(Long userNo) {
		List<Possess> possessList = possessRepository.findPossessesByUserNo(userNo).stream().toList();
		List<HorseTokenDTO> result = new ArrayList<>();

		for(Possess possess : possessList) {
			HorseTokenDTO horseToken = new HorseTokenDTO();
			TokenDTO token = tokenRepository.findTokenByTokenNo(possess.getTokenNo());
			PriceDTO price = priceRepository.findCurrentPrice(possess.getTokenNo());

			horseToken.setTokenNo(possess.getTokenNo());
			horseToken.setName(token.getName());
			horseToken.setCode(token.getCode());

			double profit = (price.getPrice() * possess.getQuantity()) - possess.getTotalAmountPurchase();
			horseToken.setProfitOrLoss(profit); //평가손익 = 현재가*개수 - 총구매가
			horseToken.setReturnRate(profit / possess.getTotalAmountPurchase()); //수익률 = (현재가*개수 - 총구매가) / 총구매가

			result.add(horseToken);
		}

		return result;
	}
}
