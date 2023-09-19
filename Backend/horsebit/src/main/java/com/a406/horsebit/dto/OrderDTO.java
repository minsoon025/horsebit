package com.a406.horsebit.dto;

import java.sql.Date;

/**
 * 미체결 내역 조회 응답 DTO
 */
public class OrderDTO {
	private Long orderNo;
	private Long userNo;
	private Long tokenNo;
	private String tokenCode;
	private int price;
	private double quantity;
	private double remainQuantity;
	private Date orderTime;
	private String sellOrBuy;

	public OrderDTO(Long orderNo, Long userNo, Long tokenNo, String tokenCode, int price, double quantity,
		double remainQuantity,
		Date orderTime, String sellOrBuy) {
		this.orderNo = orderNo;
		this.userNo = userNo;
		this.tokenNo = tokenNo;
		this.tokenCode = tokenCode;
		this.price = price;
		this.quantity = quantity;
		this.remainQuantity = remainQuantity;
		this.orderTime = orderTime;
		this.sellOrBuy = sellOrBuy;
	}

	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}

	public Long getUserNo() {
		return userNo;
	}

	public void setUserNo(Long userNo) {
		this.userNo = userNo;
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

	public double getRemainQuantity() {
		return remainQuantity;
	}

	public void setRemainQuantity(double remainQuantity) {
		this.remainQuantity = remainQuantity;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public String getSellOrBuy() {
		return sellOrBuy;
	}

	public void setSellOrBuy(String sellOrBuy) {
		this.sellOrBuy = sellOrBuy;
	}
}
