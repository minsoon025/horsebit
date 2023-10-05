package com.a406.horsebit.domain;

import java.io.Serializable;

import lombok.Data;

/**
 * Horse Token 관계테이블의 PK
 */
@Data
public class HorseTokenRelPK implements Serializable {
	private Long tokenNo;
	private Long hrNo;
}
