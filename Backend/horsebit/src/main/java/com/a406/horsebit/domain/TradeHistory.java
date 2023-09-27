package com.a406.horsebit.domain;

import java.sql.Timestamp;

public class TradeHistory {
	private int price;
	private double quantity;
	private double fee;
	private Timestamp timestamp;
	private Long sellerUserNo;
	private Timestamp sellerOrderTime;
	private Long buyerUserNo;
	private Timestamp buyerOrderTime;
	private Long tokenNo;
	private String tokenCode;

	public TradeHistory(int price, double quantity, double fee, Timestamp timestamp, Long sellerUserNo, Timestamp sellerOrderTime,
		Long buyerUserNo, Timestamp buyerOrderTime, Long tokenNo, String tokenCode) {
		this.price = price;
		this.quantity = quantity;
		this.fee = fee;
		this.timestamp = timestamp;
		this.sellerUserNo = sellerUserNo;
		this.sellerOrderTime = sellerOrderTime;
		this.buyerUserNo = buyerUserNo;
		this.buyerOrderTime = buyerOrderTime;
		this.tokenNo = tokenNo;
		this.tokenCode = tokenCode;
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

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public Long getSellerUserNo() {
		return sellerUserNo;
	}

	public void setSellerUserNo(Long sellerUserNo) {
		this.sellerUserNo = sellerUserNo;
	}

	public Timestamp getSellerOrderTime() {
		return sellerOrderTime;
	}

	public void setSellerOrderTime(Timestamp sellerOrderTime) {
		this.sellerOrderTime = sellerOrderTime;
	}

	public Long getBuyerUserNo() {
		return buyerUserNo;
	}

	public void setBuyerUserNo(Long buyerUserNo) {
		this.buyerUserNo = buyerUserNo;
	}

	public Timestamp getBuyerOrderTime() {
		return buyerOrderTime;
	}

	public void setBuyerOrderTime(Timestamp buyerOrderTime) {
		this.buyerOrderTime = buyerOrderTime;
	}

	public Long getTokenNo() {
		return tokenNo;
	}

	public void setTokenNo(Long tokenNo) {
		this.tokenNo = tokenNo;
	}

	public String getTokenCode() {
		return tokenCode;
	}

	public void setTokenCode(String tokenCode) {
		this.tokenCode = tokenCode;
	}
}
