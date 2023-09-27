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
import com.a406.horsebit.repository.redis.PriceRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class BookmarkServiceImpl implements BookmarkService{
	private final BookmarkRepository bookmarkRepository;

	@Autowired
	public BookmarkServiceImpl(BookmarkRepository bookmarkRepository) {
		this.bookmarkRepository = bookmarkRepository;
	}

	@Override
	public boolean findOne(Long userNo, Long tokenNo) {
		Bookmark bookmark = bookmarkRepository.findByUserNoAndTokenNo(userNo, tokenNo);
		log.info("BookmarkServiceImpl::findOne() bookmark isNull: " + (bookmark == null));

		if(bookmark == null) return false;
		return true;
	}

	@Override
	public List<Long> findAll(Long userNo) {
		log.info("BookmarkServiceImpl::findAll() START");
		List<Long> result = new ArrayList<>();

		List<Bookmark> list = bookmarkRepository.findAllByUserNo(userNo).stream().toList();
		for(Bookmark bookmark : list) {
			result.add(bookmark.getTokenNo());
		}

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
