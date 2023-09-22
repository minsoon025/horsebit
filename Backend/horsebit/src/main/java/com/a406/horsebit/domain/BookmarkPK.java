package com.a406.horsebit.domain;

import java.io.Serializable;

import lombok.Data;

/**
 * 즐겨찾기 목록의 PK
 */
@Data
public class BookmarkPK implements Serializable {
	private Long userNo;
	private Long tokenNo;
}
