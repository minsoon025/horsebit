package com.a406.horsebit.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.a406.horsebit.domain.Account;
import com.a406.horsebit.domain.Possess;
import com.a406.horsebit.domain.Trade;
import com.a406.horsebit.domain.TradeHistory;
import com.a406.horsebit.dto.AssetsDTO;
import com.a406.horsebit.dto.HorseTokenDTO;
import com.a406.horsebit.dto.PriceDTO;
import com.a406.horsebit.dto.TokenDTO;
import com.a406.horsebit.dto.UserTradeDTO;
import com.a406.horsebit.repository.AccountRepository;
import com.a406.horsebit.repository.PossessRepository;
import com.a406.horsebit.repository.TokenRepository;
import com.a406.horsebit.repository.TradeRepository;
import com.a406.horsebit.repository.redis.PriceRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AssetsServiceImpl implements AssetsService {
	private final PossessRepository possessRepository;
	
	private final PriceRepository priceRepository;

	private final TokenRepository tokenRepository;

	private final TradeRepository tradeRepository;
	private final AccountRepository accountRepository;

	//TODO: KRW의 tokenNO를 공통 상수로 빼기
	private final Long KRW = 11L;
	private final String CODE_KRW = "KRW";
	private final String TYPE_BID = "BID";
	private final String TYPE_OFFER = "OFFER";
	private final String TYPE_DEPOSIT = "DEPOSIT";
	private final String TYPE_WITHDRAW = "WITHDRAW";

	public AssetsServiceImpl(PossessRepository possessRepository, PriceRepository priceRepository, TokenRepository tokenRepository,
		TradeRepository tradeRepository, AccountRepository accountRepository) {
		this.possessRepository = possessRepository;
		this.priceRepository = priceRepository;
		this.tokenRepository = tokenRepository;
		this.tradeRepository = tradeRepository;
		this.accountRepository = accountRepository;
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
			PriceDTO price = priceRepository .findOneCurrentPriceByTokenNo(token.getKey());
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
			if(token.getCode().equals(CODE_KRW)) continue; //KRW는 전달 X

			PriceDTO price = priceRepository.findOneCurrentPriceByTokenNo(possess.getTokenNo());

			horseToken.setTokenNo(possess.getTokenNo());
			horseToken.setName(token.getName());
			horseToken.setCode(token.getCode());

			double profit = (price.getPrice() * possess.getQuantity()) - possess.getTotalAmountPurchase();
			horseToken.setProfitOrLoss(profit); //평가손익 = 현재가*개수 - 총구매가
			horseToken.setReturnRate(Math.round((profit / possess.getTotalAmountPurchase())*1000)/1000.0); //수익률 = (현재가*개수 - 총구매가) / 총구매가

			result.add(horseToken);
		}

		return result;
	}

	/**
	 * 개인 입출금 및 거래내역 전체조회
	 * @param userNo
	 * @return
	 */
	@Override
	public List<UserTradeDTO> findTradeHistoryByUserNo(Long userNo) {
		log.info("AssetsServiceImpl::findTradeHistoryByUserNo() START");

		List<UserTradeDTO> result = new ArrayList<>();
		List<TradeHistory> tradeResult = tradeRepository.findAllByUserNo(userNo);
		List<Account> accontResult = accountRepository.findAccountsByUserNo(userNo);

		log.info("tradeResult: " + tradeResult.toString());
		log.info("accountResult: " + accontResult.toString());

		for(TradeHistory trade : tradeResult) {
			UserTradeDTO userTrade = new UserTradeDTO();
			userTrade.setExecutionTime(trade.getTimestamp());
			userTrade.setCode(trade.getTokenCode());
			userTrade.setVolume(trade.getQuantity());
			userTrade.setPrice(trade.getPrice());
			userTrade.setTransactionAmount(trade.getQuantity() * trade.getPrice()); //거래금액 = 수량 * 단가
			userTrade.setFee((trade.getFee()));

			if(trade.getSellerUserNo().equals(userNo)) { //판매자의 경우
				userTrade.setTransactionType(TYPE_OFFER);
				userTrade.setAmount(trade.getQuantity() * trade.getPrice() - trade.getFee()); //정산금액 = 거래금액 - 수수료 (매도)
				userTrade.setOrderTime(trade.getSellerOrderTime());
			}
			else if(trade.getBuyerUserNo().equals(userNo)) { //구매자의 경우
				userTrade.setTransactionType(TYPE_BID);
				userTrade.setAmount(trade.getQuantity() * trade.getPrice() + trade.getFee()); //정산금액 = 거래금액 + 수수료 (매수)
				userTrade.setOrderTime(trade.getBuyerOrderTime());
			}

			result.add(userTrade);
		}

		for(Account account : accontResult) {
			UserTradeDTO userTrade = new UserTradeDTO();
			userTrade.setExecutionTime(account.getDatetime());
			userTrade.setCode(CODE_KRW);
			userTrade.setVolume(account.getAmount());
			userTrade.setTransactionAmount(account.getAmount());
			userTrade.setAmount(account.getAmount());

			if(account.getAmount() > 0) {
				userTrade.setTransactionType(TYPE_DEPOSIT);
			}
			else if (account.getAmount() < 0) {
				userTrade.setTransactionType(TYPE_WITHDRAW);
			}

			result.add(userTrade);
		}

		Collections.sort(result);

		return result;
	}
}
