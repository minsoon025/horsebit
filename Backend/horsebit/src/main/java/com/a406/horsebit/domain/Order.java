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

/**
 * 주문 내역(미체결)
 */
@Getter
@Setter
@Entity
@ToString(exclude = {"token"})
@Table(name = "ORDER_HISTORY")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_no", nullable = false)
	private Long orderNo;

	@Column(name = "user_no", nullable = false)
	private Long userNo;

	@Column(name = "hr_no", nullable = false)
	private Long hrNo;

	@Column(name = "price", nullable = false)
	private int price;

	@Column(name = "quantity", nullable = false)
	private double quantity;

	@Column(name = "remain", nullable = false)
	private double remain;

	@Column(name = "order_time", nullable = false)
	private Timestamp orderTime;

	@Column(name = "sell_buy_flag", nullable = false)
	private String sellBuyFlag;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "token_no")
	private Token token;

}
