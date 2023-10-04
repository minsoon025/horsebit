package com.a406.horsebit.dto;

public class TokenDTO {
	private Long tokenNo;
	private String name;
	private String code;
	private long currentPrice;
	private double priceRateOfChange;
	private double volume;
	private long priceOfChange;
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

	public long getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(long currentPrice) {
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

	public long getPriceOfChange() {
		return priceOfChange;
	}

	public void setPriceOfChange(long priceOfChange) {
		this.priceOfChange = priceOfChange;
	}

	public Boolean getNewFlag() {
		return newFlag;
	}

	public void setNewFlag(Boolean newFlag) {
		this.newFlag = newFlag;
	}
}
