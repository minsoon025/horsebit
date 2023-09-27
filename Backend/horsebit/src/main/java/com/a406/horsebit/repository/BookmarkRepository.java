package com.a406.horsebit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.a406.horsebit.domain.Bookmark;
import com.a406.horsebit.domain.BookmarkPK;
import com.a406.horsebit.dto.TokenDTO;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkPK> {
	/**
	 * 즐겨찾기 목록 전체 조회 JPA
	 * @param userNo
	 * @return
	 */
	List<Bookmark> findAllByUserNo(Long userNo);

	Bookmark findByUserNoAndTokenNo(Long userNo, Long tokenNo);

	//void deleteByUserNoAndTokenNo(Long userNo, Long tokenNo);
}
