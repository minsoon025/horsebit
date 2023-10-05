package com.a406.horsebit.domain;

import java.sql.Date;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString(exclude = {"token"})
@Table(name = "TRADE_HISTORY")
public class Trade {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "execution_no", nullable = false)
	private Long executionNo;

	// @Column(name = "token_no", nullable = false)
	// private int tokenNo;

	@Column(name = "price", nullable = false)
	private int price;

	@Column(name = "quantity", nullable = false)
	private double quantity;

	@Column(name = "fee")
	private double fee;

	@Column(name = "timestamp", nullable = false)
	private Timestamp timestamp;

	@Column(name = "seller_order_no", nullable = false)
	private Long sellerOrderNo;

	@Column(name = "seller_user_no", nullable = false)
	private Long sellerUserNo;

	@Column(name = "seller_order_time", nullable = false)
	private Timestamp sellerOrderTime;

	@Column(name = "buyer_order_no", nullable = false)
	private Long buyerOrderNo;

	@Column(name = "buyer_user_no", nullable = false)
	private Long buyerUserNo;

	@Column(name = "buyer_order_time", nullable = false)
	private Timestamp buyerOrderTime;

	@Column(name = "sell_buy_flag")
	private String sellBuyFlag;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "token_no")
	private Token token;
}
