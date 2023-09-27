package com.a406.horsebit.dto;

public class TokenDTO {
	private Long tokenNo;
	private String name;
	private String code;
	private double currentPrice;
	private double priceRateOfChange;
	private double volume;
	private double priceOfChange;
	private Boolean newFlag;

	public TokenDTO(Long tokenNo, String name, String code, Boolean newFlag) {
		this.tokenNo = tokenNo;
		this.name = name;
		this.code = code;
		this.newFlag = newFlag;
	}

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

	public double getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}

	public double getPriceRateOfChange() {
		return priceRateOfChange;
	}

	public void setPriceRateOfChange(double priceRateOfChange) {
		this.priceRateOfChange = priceRateOfChange;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public double getPriceOfChange() {
		return priceOfChange;
	}

	public void setPriceOfChange(double priceOfChange) {
		this.priceOfChange = priceOfChange;
	}

	public Boolean getNewFlag() {
		return newFlag;
	}

	public void setNewFlag(Boolean newFlag) {
		this.newFlag = newFlag;
	}
}
