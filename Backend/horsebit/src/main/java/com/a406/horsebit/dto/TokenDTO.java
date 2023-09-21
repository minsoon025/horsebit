package com.a406.horsebit.dto;

public class TokenDTO {
	private Long tokenNo;
	private String name;
	private String code;
	private String currentPrice;
	private String priceTrend;
	private String volume;

	public TokenDTO(Long tokenNo, String name, String code, String currentPrice, String priceTrend, String volume) {
		this.tokenNo = tokenNo;
		this.name = name;
		this.code = code;
		this.currentPrice = currentPrice;
		this.priceTrend = priceTrend;
		this.volume = volume;
	}

	//TODO: Redis 거래 데이터 로직 추가 후 아래의 필드 3개 삭제 필요
	public TokenDTO(Long tokenNo, String name, String code) {
		this.tokenNo = tokenNo;
		this.name = name;
		this.code = code;
		this.currentPrice = "203400";
		this.priceTrend = "4.3%";
		this.volume = "3";
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

	public String getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(String currentPrice) {
		this.currentPrice = currentPrice;
	}

	public String getPriceTrend() {
		return priceTrend;
	}

	public void setPriceTrend(String priceTrend) {
		this.priceTrend = priceTrend;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}
}
