package com.a406.horsebit.controller;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;

import com.a406.horsebit.domain.User;
import com.a406.horsebit.dto.CandleDTO;
import com.a406.horsebit.dto.HorseDTO;
import com.a406.horsebit.dto.VolumeDTO;
import com.a406.horsebit.service.CandleService;
import com.a406.horsebit.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import com.a406.horsebit.domain.Bookmark;
import com.a406.horsebit.domain.BookmarkPK;
import com.a406.horsebit.dto.TokenDTO;
import com.a406.horsebit.service.BookmarkService;
import com.a406.horsebit.service.TokenService;
import com.nimbusds.jose.shaded.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/exchange/tokens")
@RestController
public class TokenController {
	private final TokenService tokenService;
	private final BookmarkService bookmarkService;
	private final CandleService candleService;
	private final UserService userService;

	@Autowired
	public TokenController(TokenService tokenService, BookmarkService bookmarkService, CandleService candleService, UserService userService) {
		this.tokenService = tokenService;
		this.bookmarkService = bookmarkService;
		this.candleService = candleService;
		this.userService = userService;
	}

	/**
	 * [거래소] 전체 토큰 조회
	 */
	/**
	 * 전체 토큰 조회
	 * @return
	 */
	@GetMapping("")
	public List<TokenDTO> getTokens() {
		log.info("TokenController::getTokens() START");
		return tokenService.findAllTokens();
	}

	/**
	 * 토큰 상세 조회
	 * @param tokenNo
	 * @return
	 */
	@GetMapping("/{tokenNo}")
	public TokenDTO getTokenDetail(@PathVariable("tokenNo") Long tokenNo) {
		log.info("TokenController::getTokenDetail() START");
		return tokenService.findTokenDetail(tokenNo);
	}

	/**
	 * 토큰 주문 현황 상세 조회
	 * @param tokenNo
	 * @return
	 */
	@GetMapping("/{tokenNo}/volumes")
	public List<VolumeDTO> getTokenVolumes(@PathVariable("tokenNo") Long tokenNo) {
		log.info("TokenController::getTokenVolumes() START");
		return tokenService.findTokenVolumes(tokenNo);
	}

	/**
	 * [거래소] 보유 토큰 조회
	 */
	/**
	 * 보유 토큰 조 회
	 * @return
	 */
	@GetMapping("/possess")
	public List<TokenDTO> getPossessTokens(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		String token = (request.getHeader("Authorization")).substring("Bearer ".length());
		User user = userService.userInfoFromToken(token);
		Long userNo = user.getId();
		log.info("user id : {}", userNo);

		log.info("TokenController::getPossessTokens() START");
		List<Long> possessTokens = tokenService.findPossessTokens(userNo);
		return tokenService.findTokens(possessTokens);
	}

	/**
	 * [거래소] 관심 토큰 조회/등록/삭제 API
	 */
	/**
	 * 관심 토큰 조회
	 * @return
	 */
	@GetMapping("/favorites")
	public List<TokenDTO> findAllBookmark(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		String token = (request.getHeader("Authorization")).substring("Bearer ".length());
		User user = userService.userInfoFromToken(token);
		Long userNo = user.getId();
		log.info("user id : {}", userNo);

		log.info("TokenController::findAllBookmark() START");
		List<Long> bookmarks = bookmarkService.findAll(userNo);
		return tokenService.findTokens(bookmarks);
	}

	/**
	 * 관심 토큰 등록
	 * @param tokenNo
	 * @return
	 */
	//TODO: DB에 이미 있으면 오류코드(근데 앱에서 이런 요청이 들어올 일이 없긴 함)
	@PostMapping("/favorites/{tokenNo}")
	public String addBookmark(HttpServletRequest request, HttpServletResponse response, @PathVariable("tokenNo") Long tokenNo) throws ParseException {
		String token = (request.getHeader("Authorization")).substring("Bearer ".length());
		User user = userService.userInfoFromToken(token);
		Long userNo = user.getId();
		log.info("user id : {}", userNo);

		log.info("TokenController::addBookmark() START");
		JsonObject obj = new JsonObject();

		//동일한 즐겨찾기 목록 존재시 true 반환 -> 즉, 신규 등록 불가
		if(bookmarkService.findOne(userNo, tokenNo)) {
			obj.addProperty("result", "FAIL");
		}
		else {
			Bookmark bookmark = new Bookmark();
			bookmark.setUserNo(userNo);
			bookmark.setTokenNo(tokenNo);
			bookmarkService.save(bookmark);

			obj.addProperty("result", "SUCCESS");
		}

		return obj.toString();
	}

	/**
	 * 관심 토큰 삭제
	 * @param tokenNo
	 * @return
	 */
	//TODO: DB에 없으면 오류코드(근데 앱에서 이런 요청이 들어올 일이 없긴 함)
	@DeleteMapping("/favorites/{tokenNo}")
	public String removeBookmark(HttpServletRequest request, HttpServletResponse response, @PathVariable("tokenNo") Long tokenNo) throws ParseException {
		String token = (request.getHeader("Authorization")).substring("Bearer ".length());
		User user = userService.userInfoFromToken(token);
		Long userNo = user.getId();
		log.info("user id : {}", userNo);

		log.info("TokenController::removeBookmark() START");
		JsonObject obj = new JsonObject();

		if (!bookmarkService.findOne(userNo, tokenNo)) {
			obj.addProperty("result", "FAIL");
		} else {
			BookmarkPK bookmarkPK = new BookmarkPK();
			bookmarkPK.setUserNo(userNo);
			bookmarkPK.setTokenNo(tokenNo);
			bookmarkService.remove(bookmarkPK);

			obj.addProperty("result", "SUCCESS");
		}
		return obj.toString();
	}

	@GetMapping("/{tokenNo}/chart")
	public List<CandleDTO> getCandles(@PathVariable("tokenNo") Long tokenNo, @RequestParam("quantity") Long quantity, @RequestParam("endTime") LocalDateTime endTime, @RequestParam("candleTypeIndex") Integer candleTypeIndex, @RequestParam("margin") Long margin) {
		return candleService.getCandle(tokenNo, endTime, candleTypeIndex, quantity, margin);
	}

	@GetMapping("/{tokenNo}/info")
	public HorseDTO getHorseInfo(@PathVariable("tokenNo") Long tokenNo) {
		return tokenService.findHorse(tokenNo);
	}
}
