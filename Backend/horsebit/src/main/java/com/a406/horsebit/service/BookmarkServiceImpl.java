package com.a406.horsebit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a406.horsebit.domain.Bookmark;
import com.a406.horsebit.domain.BookmarkPK;
import com.a406.horsebit.dto.TokenDTO;
import com.a406.horsebit.repository.BookmarkRepository;
import com.a406.horsebit.repository.TokenRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class BookmarkServiceImpl implements BookmarkService{
	private final BookmarkRepository bookmarkRepository;
	private final TokenRepository tokenRepository;

	@Autowired
	public BookmarkServiceImpl(BookmarkRepository bookmarkRepository, TokenRepository tokenRepository) {
		this.bookmarkRepository = bookmarkRepository;
		this.tokenRepository = tokenRepository;
	}
	
	@Override
	public List<TokenDTO> findAll(Long userNo) {
		log.info("BookmarkServiceImpl::findAll() START");

		//1)즐겨찾기 목록 조회
		List<Bookmark> list = bookmarkRepository.findAllByUserNo(userNo).stream().toList();
		List<TokenDTO> result = new ArrayList<>();

		//2)각 즐겨찾기의 tokenNo에 맞는 token 정보 조회
		for(Bookmark bookmark : list) {
			TokenDTO token = tokenRepository.findTokenByTokenNo(bookmark.getTokenNo());
			result.add(token);
		}

		log.info("BookmarkServiceImpl::findAll() result : " + result.toString());
		return result;
	}

	@Override
	public void save(Bookmark bookmark) {
		log.info("BookmarkServiceImpl::save() START");
		bookmarkRepository.save(bookmark);
	}

	@Override
	public void remove(BookmarkPK bookmarkPK) {
		log.info("BookmarkServiceImpl::remove() START");
		bookmarkRepository.deleteById(bookmarkPK);
	}
}
