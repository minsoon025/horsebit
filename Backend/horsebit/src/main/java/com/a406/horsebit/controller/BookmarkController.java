package com.a406.horsebit.controller;

import java.util.ArrayList;
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
import com.nimbusds.jose.shaded.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/exchange/favorites")
@RestController
public class BookmarkController {
	private final BookmarkService bookmarkService;

	@Autowired
	public BookmarkController(BookmarkService bookmarkService) {
		this.bookmarkService = bookmarkService;
	}

	//TODO: OAuth 개발 후 아래의 userNo는 삭제 필요
	@GetMapping("")
	public List<TokenDTO> findAll() {
		Long userNo = 1L;
		return bookmarkService.findAll(userNo);
	}

	//TODO: OAuth 개발 후 아래의 userNo는 삭제 필요
	//TODO: DB에 이미 있으면 오류코드(근데 앱에서 이런 요청이 들어올 일이 없긴 함)
	@PostMapping("/{tokenNo}")
	public String addBookmark(@PathVariable("tokenNo") Long tokenNo) {
		log.info("BookmarkController::addBookmark() START");
		JsonObject obj = new JsonObject();

		Long userNo = 1L;
		Bookmark bookmark = new Bookmark();
		bookmark.setUserNo(userNo);
		bookmark.setTokenNo(tokenNo);
		bookmarkService.save(bookmark);

		obj.addProperty("result", "SUCCESS");
		return obj.toString();
	}

	//TODO: OAuth 개발 후 아래의 userNo는 삭제 필요
	//TODO: DB에 없으면 오류코드(근데 앱에서 이런 요청이 들어올 일이 없긴 함)
	@DeleteMapping("/{tokenNo}")
	public String removeBookmark(@PathVariable("tokenNo") Long tokenNo) {
		log.info("BookmarkController::removeBookmark() START");
		JsonObject obj = new JsonObject();

		Long userNo = 1L;
		BookmarkPK bookmarkPK = new BookmarkPK();
		bookmarkPK.setUserNo(userNo);
		bookmarkPK.setTokenNo(tokenNo);
		bookmarkService.remove(bookmarkPK);

		obj.addProperty("result", "SUCCESS");
		return obj.toString();
	}
}
