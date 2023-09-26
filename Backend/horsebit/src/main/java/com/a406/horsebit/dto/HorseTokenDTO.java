package com.a406.horsebit.dto;

public class HorseTokenDTO {
	private Long tokenNo;
	private String name;
	private String code;
	private double profitOrLoss;
	private double returnRate;

	public Long getTokenNo() {
		return tokenNo;
	}

	public void setTokenNo(Long tokenNo) {
		this.tokenNo = tokenNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public double getProfitOrLoss() {
		return profitOrLoss;
	}

	public void setProfitOrLoss(double profitOrLoss) {
		this.profitOrLoss = profitOrLoss;
	}

	public double getReturnRate() {
		return returnRate;
	}

	public void setReturnRate(double returnRate) {
		this.returnRate = returnRate;
	}
}
