package com.a406.horsebit.domain;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * 토큰 정보
 */
@Getter
@Setter
@Entity
@Table(name = "TOKEN_INFO")
public class Token {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "token_no", nullable = false)
	private Long tokenNo;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "code", nullable = false)
	private String code;

	@Column(name = "supply", nullable = false)
	private int supply;

	@Column(name = "publish_date", nullable = false)
	private Date publishDate;
}
