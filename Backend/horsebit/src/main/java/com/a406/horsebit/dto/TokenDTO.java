package com.a406.horsebit.dto;

public class TokenDTO {
	private Long tokenNo;
	private String name;
	private String code;
	private double currentPrice;
	private double priceRateOfChange;
	private double volume;
	private Boolean newFlag;

	public TokenDTO(Long tokenNo, String name, String code, double currentPrice, double priceRateOfChange, double volume, Boolean newFlag) {
		this.tokenNo = tokenNo;
		this.name = name;
		this.code = code;
		this.currentPrice = currentPrice;
		this.priceRateOfChange = priceRateOfChange;
		this.volume = volume;
		this.newFlag = newFlag;
	}

	//TODO: Redis 거래 데이터 로직 추가 후 아래의 필드 3개 삭제 필요
	public TokenDTO(Long tokenNo, String name, String code, Boolean newFlag) {
		this.tokenNo = tokenNo;
		this.name = name;
		this.code = code;
		this.newFlag = newFlag;
		this.currentPrice = 203400;
		this.priceRateOfChange = 4.3;
		this.volume = 3;
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

	public Boolean getNewFlag() {
		return newFlag;
	}

	public void setNewFlag(Boolean newFlag) {
		this.newFlag = newFlag;
	}
}
