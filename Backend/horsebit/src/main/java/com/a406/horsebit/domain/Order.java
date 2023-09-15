package com.a406.horsebit.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ORDER_HISTORY")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_no", nullable = false)
	private int orderNo;
	@Column(name = "user_no", nullable = false)
	private int userNo;
	@Column(name = "hr_no", nullable = false)
	private int hrNo;
	@Column(name = "token_no", nullable = false)
	private int tokenNo;
	@Column(name = "price", nullable = false)
	private int price;
	@Column(name = "quantity", nullable = false)
	private double quantity;
	@Column(name = "remain", nullable = false)
	private double remain;
	@Column(name = "order_time", nullable = false)
	private Date orderTime;

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public int getUserNo() {
		return userNo;
	}

	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	public int getHrNo() {
		return hrNo;
	}

	public void setHrNo(int hrNo) {
		this.hrNo = hrNo;
	}

	public int getTokenNo() {
		return tokenNo;
	}

	public void setTokenNo(int tokenNo) {
		this.tokenNo = tokenNo;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getRemain() {
		return remain;
	}

	public void setRemain(double remain) {
		this.remain = remain;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
}
