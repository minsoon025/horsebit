package com.a406.horsebit.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * 즐겨찾기 목록
 */
@Entity
@Getter
@Setter
@IdClass(BookmarkPK.class)
@Table(name = "BOOKMARK")
public class Bookmark {
	@Id
	@Column(name = "user_no")
	private Long userNo;
	@Id
	@Column(name = "token_no")
	private Long tokenNo;
}
