package com.a406.horsebit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a406.horsebit.domain.Possess;
import com.a406.horsebit.dto.PriceDTO;
import com.a406.horsebit.dto.PriceRateOfChangeDTO;
import com.a406.horsebit.dto.TokenDTO;
import com.a406.horsebit.dto.VolumeDTO;
import com.a406.horsebit.repository.PossessRepository;
import com.a406.horsebit.repository.TokenRepository;
import com.a406.horsebit.repository.redis.OrderRepository;
import com.a406.horsebit.repository.redis.PriceRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {
	private final TokenRepository tokenRepository;
	private final PossessRepository possessRepository;
	private final PriceRepository priceRepository;
	private final OrderRepository orderRepository;
	private final PriceService priceService;

	private final Long KRW_NO = 11L;

	@Autowired
	public TokenServiceImpl(TokenRepository tokenRepository, PossessRepository possessRepository, PriceRepository priceRepository,
		OrderRepository orderRepository, PriceService priceService) {
		this.tokenRepository = tokenRepository;
		this.possessRepository = possessRepository;
		this.priceRepository = priceRepository;
		this.orderRepository = orderRepository;
		this.priceService = priceService;
	}

	/**
	 * 전체 토큰 목록 조회
	 * @return
	 */
	@Override
	public List<TokenDTO> findAllTokens() {
		//전체 토큰 번호 조회 후 정보 조회
		List<Long> tokensNo = tokenRepository.findAllTokenNos();
		List<TokenDTO> result = findTokens(tokensNo);

		log.debug("TokenServiceImpl::getAllTokens() START" + result.toString());
		return result;
	}

	/**
	 * 특정 토큰의 정적 데이터 조회
	 * @param tokenNo
	 * @return
	 */
	public TokenDTO findOneToken(Long tokenNo) {
		return tokenRepository.findTokenByTokenNo(tokenNo);
	}

	/**
	 * 여러 토큰의 정적/동적 데이터 조회
	 * @param tokensNo
	 * @return
	 */
	@Override
	public List<TokenDTO> findTokens(List<Long> tokensNo) {
		log.info("TokenServiceImpl::findTokens() START");
		List<TokenDTO> result = new ArrayList<>();
		
		List<PriceDTO> rPrices = priceService.getCurrentPrice(tokensNo); //대상 토큰들의 현재가 조회
		List<PriceRateOfChangeDTO> rRates = priceService.getPriceOfRate(tokensNo); //대상 토큰들의 변동률 조회

		//대상 토큰들의 수와 가져온 데이터의 수가 불일치시 오류!!
		if(!(tokensNo.size() == rPrices.size() && tokensNo.size() == rRates.size())) {
			//TODO: 반환값 바꾸기
			return null;
		}

		//각 토큰의 정적/동적 데이터를 TokenDTO로 묶기
		//TODO: 소수점 3자리 Common으로 빼기
		int ind = 0;
		for(Long tokenNo : tokensNo) {
			TokenDTO token = findOneToken(tokenNo); //특정 토큰의 정적데이터 조회
			token.setCurrentPrice(rPrices.get(ind).getPrice()); //현재가 셋팅

			//변동률 셋팅 - 소수점 3자리까지 표기
			double rRate = rRates.get(ind).getPriceRateOfChange();
			rRate = Math.round(rRate * 1000) / 1000.0;
			token.setPriceRateOfChange(rRate);
			
			//TODO: NullPointer 오류나는 건 - findTradeTotalVolume 수정 후 정상 확인 필요
			token.setVolume(orderRepository.findTradeTotalVolume(tokenNo)); //현재 거래금액 조회
			ind++;

			result.add(token);
		}

		return result;
	}

	/**
	 * 개인의 소유 토큰 목록 조회
	 * @param userNo
	 * @return
	 */
	@Override
	public List<Long> findPossessTokens(Long userNo) {
		log.info("TokenServiceImpl::findPossessTokens() START");
		List<Long> result = new ArrayList<>();
		
		List<Possess> possesses = possessRepository.findPossessesByUserNo(userNo);
		for(Possess possess : possesses) {
			if(possess.getTokenNo() == KRW_NO) continue; //원화는 제외
			result.add(possess.getTokenNo()); //보유 토큰번호만 추출
		}
		
		return result;
	}

	/**
	 * 특정 토큰 상세 정보 조회
	 * @param tokenNo
	 * @return
	 */
	@Override
	public TokenDTO findTokenDetail(Long tokenNo) {
		TokenDTO token = findOneToken(tokenNo); //정적데이터

		//동적데이터 - 현재가, 변동률, 변동금액
		long rPrice = priceService.getCurrentPrice(tokenNo).getPrice();
		double rRate = priceService.getPriceOfRate(tokenNo).getPriceRateOfChange();
		rRate = Math.round(rRate * 1000) / 1000.0;
		long sPrice = priceRepository.findStartPrice(tokenNo).getPrice();

		token.setCurrentPrice(rPrice);
		token.setPriceRateOfChange(rRate);
		token.setPriceOfChange(rPrice-sPrice);

		return token;
	}

	/**
	 * 주문현황 대상금액 리스트 추출 - 현재가 기준
	 * @param curPrice 현재가
	 * @param flag 1:매도, -1:매수
	 * @param stInd 시작 인덱스(매도:0~9, 매수:1~10)
	 * @param enInd 끝 인덱스
	 * @param gap 차이 금액
	 * @return
	 */
	List<Long> getPriceList(Long curPrice, int flag, int stInd, int enInd, int gap) {
		List<Long> result = new ArrayList<>();
		for(int i = stInd; i <= enInd; i++) {
			result.add(curPrice + gap * i * flag);
		}

		return result;
	}

	/**
	 * 특정 토큰의 주문현황 조회
	 * @param tokenNo
	 * @return
	 */
	@Override
	public List<VolumeDTO> findTokenVolumes(Long tokenNo) {
		List<VolumeDTO> priceList = new ArrayList<>();
		Long curPrice = priceRepository.findCurrentPrice(tokenNo).getPrice();
		log.info("TokenServiceImpl::findTokenVolumes() curPrice: " + curPrice);

		//현재가 기준으로 대상금액 추출(총 20건)
		List<Long> sellPriceList = getPriceList(curPrice, 1, 0, 9, 5);
		List<Long> buyPriceList = getPriceList(curPrice, -1, 1, 10, 5);

		//대상금액별 주문량 조회
		List<Double> sellVolumes = orderRepository.findSellVolumeByPriceAtOrderBook(tokenNo, sellPriceList);
		List<Double> buyVolumes = orderRepository.findBuyVolumeByPriceAtOrderBook(tokenNo, buyPriceList);

		//매도 데이터 10건 - 금액, 주문량, 변동추이
		for(int ind = sellVolumes.size() - 1; ind >= 0; ind--) {
			VolumeDTO curr = new VolumeDTO();
			curr.setPrice(sellPriceList.get(ind));
			curr.setVolume(sellVolumes.get(ind));

			double rRate = priceService.getPriceOfRate(tokenNo, sellPriceList.get(ind)).getPriceRateOfChange();
			rRate = Math.round(rRate * 1000) / 1000.0;
			curr.setPriceRateOfChange(rRate);
			priceList.add(curr);
		}

		//매수 데이터 10건 - 금액, 주문량, 변동추이
		for(int ind = 0; ind < buyVolumes.size(); ind++) {
			VolumeDTO curr = new VolumeDTO();
			curr.setPrice(buyPriceList.get(ind));
			curr.setVolume(buyVolumes.get(ind));

			double rRate = priceService.getPriceOfRate(tokenNo, buyPriceList.get(ind)).getPriceRateOfChange();
			rRate = Math.round(rRate * 1000) / 1000.0;
			curr.setPriceRateOfChange(rRate);
			priceList.add(curr);
		}

		return priceList;
	}
}
