package com.a406.horsebit.dto;

import java.sql.Date;
import java.sql.Timestamp;

public class TradeDTO {
	private Long executionNo;
	private Long tokenNo;
	private String tokenCode;
	private int price;
	private double quantity;
	private Timestamp timestamp;
	private String sellOrBuy;

	public TradeDTO(Long executionNo, Long tokenNo, String tokenCode, int price, double quantity, Timestamp timestamp,
		String sellOrBuy) {
		this.executionNo = executionNo;
		this.tokenNo = tokenNo;
		this.tokenCode = tokenCode;
		this.price = price;
		this.quantity = quantity;
		this.timestamp = timestamp;
		this.sellOrBuy = sellOrBuy;
	}

	public Long getExecutionNo() {
		return executionNo;
	}

	public void setExecutionNo(Long executionNo) {
		this.executionNo = executionNo;
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

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getSellOrBuy() {
		return sellOrBuy;
	}

	public void setSellOrBuy(String sellOrBuy) {
		this.sellOrBuy = sellOrBuy;
	}
}
