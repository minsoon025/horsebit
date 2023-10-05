package com.a406.horsebit.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import com.a406.horsebit.domain.Account;
import com.a406.horsebit.domain.Possess;
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

import jakarta.persistence.Column;
import jakarta.transaction.Transactional;
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

	//TODO: CommonUtil 파일로 빼기
	public String getDateTimeFormat(Timestamp timestamp) {
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		Date date = new Date(Long.parseLong(String.valueOf(timestamp.getTime())));

		return String.valueOf(dtFormat.format(date));
	}

	@Override
	public AssetsDTO findAssetsByUserNo(Long userNo) {
		AssetsDTO result = new AssetsDTO();

		List<Possess> possessesList = possessRepository.findPossessesByUserNo(userNo).stream().toList();

		double amtKRW = 0L; //잔여 현금
		double amtToken = 0L; //총 매수 금액
		Double amtEvaluation = 0.0; //총 평가
		Map<Long, Long> tokenMap = new HashMap<>(); // Key:tokenNo, Value:count

		//TODO: KRW의 tokenNo = 0 상수로 공통으로 빼기
		for(Possess possess : possessesList) {
			if(possess.getTokenNo().equals(KRW)) {
				amtKRW = possess.getTotalAmountPurchase();
				log.info("KRW IS FOUND" + possess.getQuantity());
			}
			else {
				amtToken += possess.getTotalAmountPurchase();

				Long curToken = possess.getTokenNo();
				Long curQuantity = possess.getQuantity();
				if(tokenMap.containsKey(curToken)) {
					Long cnt = tokenMap.get(curToken);
					cnt += curQuantity;
					tokenMap.replace(curToken, cnt);
				}
				else {
					tokenMap.put(curToken, curQuantity);
				}
			}
		}

		for(Map.Entry<Long, Long> token : tokenMap.entrySet()) {
			PriceDTO price = priceRepository .findCurrentPrice(token.getKey());
			log.info("CURRENT_PRICE IS FOUND" + price.getPrice());
			amtEvaluation += price.getPrice() * token.getValue();
		}

		result.setTotalAsset(amtKRW + amtEvaluation); //자산 총합
		result.setCashBalance(amtKRW); //잔여 현금
		result.setTotalPurchase(amtToken); //총 매수 금액
		result.setTotalEvaluation(amtEvaluation); //총 평가 금액
		result.setProfitOrLoss(amtEvaluation - amtToken); //평가 손익
		result.setReturnRate(Math.round(((amtEvaluation - amtToken)/amtToken)*1000)/1000.0); //수익률

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

			PriceDTO price = priceRepository.findCurrentPrice(possess.getTokenNo());

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
			userTrade.setExeTime(trade.getTimestamp());
			userTrade.setExecutionTime(getDateTimeFormat(trade.getTimestamp()));
			userTrade.setTokenNo(trade.getTokenNo());
			userTrade.setCode(trade.getTokenCode());
			userTrade.setVolume(trade.getQuantity());
			userTrade.setPrice(trade.getPrice());
			userTrade.setTransactionAmount(trade.getQuantity() * trade.getPrice()); //거래금액 = 수량 * 단가
			userTrade.setFee((trade.getFee()));

			if(trade.getSellerUserNo().equals(userNo)) { //판매자의 경우
				userTrade.setTransactionType(TYPE_OFFER);
				userTrade.setAmount(trade.getQuantity() * trade.getPrice() - trade.getFee()); //정산금액 = 거래금액 - 수수료 (매도)
				userTrade.setOrderTime(getDateTimeFormat(trade.getSellerOrderTime()));
			}
			else if(trade.getBuyerUserNo().equals(userNo)) { //구매자의 경우
				userTrade.setTransactionType(TYPE_BID);
				userTrade.setAmount(trade.getQuantity() * trade.getPrice() + trade.getFee()); //정산금액 = 거래금액 + 수수료 (매수)
				userTrade.setOrderTime(getDateTimeFormat(trade.getBuyerOrderTime()));
			}

			result.add(userTrade);
		}

		for(Account account : accontResult) {
			UserTradeDTO userTrade = new UserTradeDTO();
			userTrade.setExeTime(account.getDatetime());
			userTrade.setExecutionTime(getDateTimeFormat(account.getDatetime()));
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

	@Transactional
	@Override
	public int updatePossessKRW(Long userNo, Long amount) {
		Possess curr = possessRepository.findByUserNoAndTokenNo(userNo, KRW);
		if(curr == null) {
			//TODO: 반환값 바꾸기
			return 0;
		}

		Long currAmount = curr.getTotalAmountPurchase();
		curr.setQuantity(currAmount + amount);
		curr.setTotalAmountPurchase(currAmount + amount);
		possessRepository.save(curr);

		return 1;
	}

	@Override
	public Long saveDepositWithdraw(Long userNo, Long amount) {
		// 현재 날짜/시간
		LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
		String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss"));

		if(updatePossessKRW(userNo, amount) == 0) return 0L;

		Account account = new Account();
		account.setUserNo(userNo);
		account.setAmount(amount);
		account.setDatetime(Timestamp.valueOf(formatedNow));
		accountRepository.save(account);

		return possessRepository.findByUserNoAndTokenNo(userNo, KRW).getTotalAmountPurchase();
	}

	@Transactional
	@Override
	public Possess saveNewAsset(Long userNo, Long amount) {
		Possess nPossess = new Possess();
		nPossess.setTokenNo(KRW);
		nPossess.setUserNo(userNo);
		nPossess.setQuantity(amount);
		nPossess.setTotalAmountPurchase(amount);
		possessRepository.save(nPossess);
		return nPossess;
//		return possessRepository.save(nPossess).getShareNo();
	}

	@Override
	public double findTokenByUserNoAndTokenNo(Long userNo, Long tokenNo) {
		Possess result = possessRepository.findByUserNoAndTokenNo(userNo, tokenNo);
		return result.getQuantity();
	}

	@Override
	public Possess saveTrade(Long userNo, Long tokenNo, double volume, Long currentPrice) {
		Possess curResult = possessRepository.findByUserNoAndTokenNo(userNo, tokenNo);

		if(curResult == null) { //userNo가 tokenNo를 현재 보유하고 있지 않으면
			curResult.setUserNo(userNo);
			curResult.setTokenNo(tokenNo);
			curResult.setQuantity((long)volume);
			curResult.setTotalAmountPurchase((long)volume * currentPrice);

			possessRepository.save(curResult);
			return curResult;
		}

		//userNo가 tokenNo를 현재 보유하고 있으면
		double curVol = curResult.getQuantity();
		Long curTotalAmt = curResult.getTotalAmountPurchase();
		curVol += volume;
		curTotalAmt += ((long)volume * currentPrice);

		curResult.setQuantity((long)curVol);
		curResult.setTotalAmountPurchase(curTotalAmt);

		return curResult;
	}
}
