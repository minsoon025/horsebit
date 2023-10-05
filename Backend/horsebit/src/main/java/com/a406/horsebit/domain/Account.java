package com.a406.horsebit.domain;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "ACCOUNT_HISTORY")
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "acc_history_no")
	private Long accountHistoryNumber;

	@Column(name = "user_no")
	private Long userNo;

	@Column(name = "amount")
	private Long amount;

	@Column(name = "timestamp")
	private Timestamp datetime;
}
