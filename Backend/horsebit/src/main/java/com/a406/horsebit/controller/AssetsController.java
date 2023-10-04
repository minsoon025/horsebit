package com.a406.horsebit.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.a406.horsebit.domain.User;
import com.a406.horsebit.service.UserService;
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
	private final UserService userService;

	@Autowired
	public AssetsController(AssetsService assetsService, UserService userService) {
		this.assetsService = assetsService;
		this.userService = userService;
	}

	/**
	 * 개인자산 전체조회
	 * @return
	 */
	@GetMapping("")
	public AssetsDTO getUserAssets(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		String accessToken = (request.getHeader("Authorization")).substring("Bearer ".length());
		User user = userService.userInfoFromToken(accessToken);
		Long userNo = user.getId();
		log.info("user id : {}", userNo);

		return assetsService.findAssetsByUserNo(userNo);
	}

	/**
	 * 개인자산 토큰 전체조회
	 * @return
	 */
	@GetMapping("/horses")
	public List<HorseTokenDTO> getUserTokens(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		String accessToken = (request.getHeader("Authorization")).substring("Bearer ".length());
		User user = userService.userInfoFromToken(accessToken);
		Long userNo = user.getId();
		log.info("user id : {}", userNo);

		return assetsService.findTokensByUserNo(userNo);
	}


	/**
	 * 개인 입출금 및 거래내역 전체조회
	 * @return
	 */
	@GetMapping("/investments")
	public List<UserTradeDTO> getUserTrade(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		String accessToken = (request.getHeader("Authorization")).substring("Bearer ".length());
		User user = userService.userInfoFromToken(accessToken);
		Long userNo = user.getId();
		log.info("user id : {}", userNo);
		log.info("AssetsController::getUserTrade() START");

		return assetsService.findTradeHistoryByUserNo(userNo);
	}

	/**
	 * 입출금 처리
	 * @param reqAmount
	 * @return
	 */
	@PostMapping("/depositwithdraw")
	public String addDepositWithdraw(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Long> reqAmount) throws ParseException {
		String accessToken = (request.getHeader("Authorization")).substring("Bearer ".length());
		User user = userService.userInfoFromToken(accessToken);
		Long userNo = user.getId();
		log.info("user id : {}", userNo);
		JsonObject obj = new JsonObject();

		Long result = assetsService.saveDepositWithdraw(userNo, reqAmount.get("reqAmount"));
		obj.addProperty("result", "SUCCESS");
		obj.addProperty("amount", result);
		return obj.toString();
	}
}
