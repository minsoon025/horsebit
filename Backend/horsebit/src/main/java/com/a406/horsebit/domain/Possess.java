package com.a406.horsebit.domain;

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
@Table(name = "TOKEN_STATUS")
public class Possess {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "share_no")
	private Long shareNo;

	@Column(name = "token_no")
	private Long tokenNo;

	@Column(name = "user_no")
	private Long userNo;

	@Column(name = "quantity")
	private double quantity;

	@Column(name = "total_amount_of_purchase")
	private double totalAmountPurchase;
}
