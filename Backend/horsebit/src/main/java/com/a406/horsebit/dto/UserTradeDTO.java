package com.a406.horsebit.dto;

import java.sql.Time;
import java.sql.Timestamp;

public class UserTradeDTO implements Comparable<UserTradeDTO> {
	private Timestamp executionTime;
	private String code;
	private String transactionType;
	private double volume;
	private long price;
	private double transactionAmount;
	private double fee;
	private double amount;
	private Timestamp orderTime;

	public UserTradeDTO() {}

	public UserTradeDTO(Timestamp executionTime, String code, String transactionType, double volume, long price,
		double transactionAmount, double fee, double amount, Timestamp orderTime) {
		this.executionTime = executionTime;
		this.code = code;
		this.transactionType = transactionType;
		this.volume = volume;
		this.price = price;
		this.transactionAmount = transactionAmount;
		this.fee = fee;
		this.amount = amount;
		this.orderTime = orderTime;
	}

	public Timestamp getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(Timestamp executionTime) {
		this.executionTime = executionTime;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Timestamp getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Timestamp orderTime) {
		this.orderTime = orderTime;
	}

	@Override
	public int compareTo(UserTradeDTO o) {
		if(executionTime.before(o.executionTime)) return 1;
		else if(executionTime.after(o.executionTime)) return -1;

		return 0;
	}
}
