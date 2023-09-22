package com.a406.horsebit.service;

import java.util.List;

import com.a406.horsebit.domain.Bookmark;
import com.a406.horsebit.domain.BookmarkPK;
import com.a406.horsebit.dto.TokenDTO;

public interface BookmarkService {
	/**
	 * 즐겨찾기 목록 전체 조회
	 * @param userNo
	 * @return
	 */
	List<TokenDTO> findAll(Long userNo);

	/**
	 * 즐겨찾기 항목 등록
	 * @param bookmark
	 */
	void save(Bookmark bookmark);

	/**
	 * 즐겨찾기 항목 삭제
	 * @param bookmarkPK
	 */
	void remove(BookmarkPK bookmarkPK);
}
