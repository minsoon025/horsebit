package com.a406.horsebit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

	@Autowired
	public TokenController(TokenService tokenService, BookmarkService bookmarkService) {
		this.tokenService = tokenService;
		this.bookmarkService = bookmarkService;
	}

	/**
	 * [거래소] 전체 토큰 조회
	 */
	@GetMapping("")
	public List<TokenDTO> getTokens() {
		log.info("TokenController::getTokens() START");
		return tokenService.findAllTokens();
	}

	/**
	 * [거래소] 보유 토큰 조회
	 */
	/**
	 * 보유 토큰 조 회
	 * @return
	 */
	//TODO: OAuth 개발 후 아래의 userNo는 삭제 필요
	@GetMapping("/possess")
	public List<TokenDTO> getPossessTokens() {
		Long userNo = 1L;
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
	//TODO: OAuth 개발 후 아래의 userNo는 삭제 필요
	@GetMapping("/favorites")
	public List<TokenDTO> findAllBookmark() {
		Long userNo = 1L;

		log.info("TokenController::findAllBookmark() START");
		List<Long> bookmarks = bookmarkService.findAll(userNo);
		return tokenService.findTokens(bookmarks);
	}

	/**
	 * 관심 토큰 등록
	 * @param tokenNo
	 * @return
	 */
	//TODO: OAuth 개발 후 아래의 userNo는 삭제 필요
	//TODO: DB에 이미 있으면 오류코드(근데 앱에서 이런 요청이 들어올 일이 없긴 함)
	@PostMapping("/favorites/{tokenNo}")
	public String addBookmark(@PathVariable("tokenNo") Long tokenNo) {
		log.info("TokenController::addBookmark() START");
		JsonObject obj = new JsonObject();
		Long userNo = 1L;

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
	//TODO: OAuth 개발 후 아래의 userNo는 삭제 필요
	//TODO: DB에 없으면 오류코드(근데 앱에서 이런 요청이 들어올 일이 없긴 함)
	@DeleteMapping("/favorites/{tokenNo}")
	public String removeBookmark(@PathVariable("tokenNo") Long tokenNo) {
		log.info("TokenController::removeBookmark() START");
		JsonObject obj = new JsonObject();

		Long userNo = 1L;

		if(!bookmarkService.findOne(userNo, tokenNo)) {
			obj.addProperty("result", "FAIL");
		}
		else {
			BookmarkPK bookmarkPK = new BookmarkPK();
			bookmarkPK.setUserNo(userNo);
			bookmarkPK.setTokenNo(tokenNo);
			bookmarkService.remove(bookmarkPK);

			obj.addProperty("result", "SUCCESS");
		}
		return obj.toString();
	}
}
