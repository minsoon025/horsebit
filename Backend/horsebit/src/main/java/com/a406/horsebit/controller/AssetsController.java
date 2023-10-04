package com.a406.horsebit.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.a406.horsebit.config.jwt.TokenProvider;
import com.a406.horsebit.domain.User;
import com.a406.horsebit.repository.UserRepository;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.a406.horsebit.dto.AssetsDTO;
import com.a406.horsebit.dto.HorseTokenDTO;
import com.a406.horsebit.dto.UserTradeDTO;
import com.a406.horsebit.service.AssetsService;
import com.nimbusds.jose.shaded.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/assets")
public class AssetsController {
	private final AssetsService assetsService;
	private final TokenProvider tokenProvider;
	private final UserRepository userRepository;

	@Autowired
	public AssetsController(AssetsService assetsService, TokenProvider tokenProvider, UserRepository userRepository) {
		this.assetsService = assetsService;
		this.tokenProvider = tokenProvider;
		this.userRepository = userRepository;
	}

	//TODO: OAuth 적용 후 userNo 삭제 필요

	/**
	 * 개인자산 전체조회
	 * @return
	 */
	@GetMapping("")
	public AssetsDTO getUserAssets(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		String token = (request.getHeader("Authorization")).substring("Bearer ".length());
		SignedJWT signedJWT = (SignedJWT) tokenProvider.parseAccessToken(token);
		String email = signedJWT.getJWTClaimsSet().getStringClaim("email");
		log.info("사용자 이메일 : "+email);
		User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("토큰에 맞는 사용자정보가 없습니다."));

		Long userNo = user.getId();

		return assetsService.findAssetsByUserNo(userNo);
	}

	//TODO:O OAuth 적용 후 userNo 삭제 필요
	/**
	 * 개인자산 토큰 전체조회
	 * @return
	 */
	@GetMapping("/horses")
	public List<HorseTokenDTO> getUserTokens(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		String token = (request.getHeader("Authorization")).substring("Bearer ".length());
		SignedJWT signedJWT = (SignedJWT) tokenProvider.parseAccessToken(token);
		String email = signedJWT.getJWTClaimsSet().getStringClaim("email");
		log.info("사용자 이메일 : "+email);
		User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("토큰에 맞는 사용자정보가 없습니다."));

		Long userNo = user.getId();

		return assetsService.findTokensByUserNo(userNo);
	}

	//TODO: OAuth 적용 후 userNo 삭제 필요

	/**
	 * 개인 입출금 및 거래내역 전체조회
	 * @return
	 */
	@GetMapping("/investments")
	public List<UserTradeDTO> getUserTrade() {
		Long userNo = 1L;
		log.info("AssetsController::getUserTrade() START");

		return assetsService.findTradeHistoryByUserNo(userNo);
	}
	
	//TODO: OAuth 적용 후 userNo 제거 필요
	/**
	 * 입출금 처리
	 * @param reqAmount
	 * @return
	 */
	@PostMapping("/depositwithdraw")
	public String addDepositWithdraw(@RequestBody Map<String, Long> reqAmount) {
		Long userNo = 1L;
		JsonObject obj = new JsonObject();

		Long result = assetsService.saveDepositWithdraw(userNo, reqAmount.get("reqAmount"));
		obj.addProperty("result", "SUCCESS");
		obj.addProperty("amount", result);
		return obj.toString();
	}
}
