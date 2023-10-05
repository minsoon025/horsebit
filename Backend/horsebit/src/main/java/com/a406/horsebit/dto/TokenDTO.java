package com.a406.horsebit.dto;

import java.sql.Date;

import jakarta.persistence.Column;

public class TokenDTO {
	private Long tokenNo;
	private String name;
	private String code;
	private long currentPrice;
	private double priceRateOfChange;
	private double volume;
	private long priceOfChange;
	private int supply;
	private Date publishDate;
	private Boolean newFlag;

	public TokenDTO(Long tokenNo, String name, String code, Boolean newFlag) {
		this.tokenNo = tokenNo;
		this.name = name;
		this.code = code;
		this.newFlag = newFlag;
	}

	public TokenDTO(Long tokenNo, String name, String code, int supply, Date publishDate, Boolean newFlag) {
		this.tokenNo = tokenNo;
		this.name = name;
		this.code = code;
		this.supply = supply;
		this.publishDate = publishDate;
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

	public int getSupply() {
		return supply;
	}

	public void setSupply(int supply) {
		this.supply = supply;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public Boolean getNewFlag() {
		return newFlag;
	}

	public void setNewFlag(Boolean newFlag) {
		this.newFlag = newFlag;
	}
}
