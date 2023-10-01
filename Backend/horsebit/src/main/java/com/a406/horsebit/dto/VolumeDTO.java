package com.a406.horsebit.dto;

public class VolumeDTO {
	private Long price;
	private double volume;
	private double priceRateOfChange;

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public double getPriceRateOfChange() {
		return priceRateOfChange;
	}

	public void setPriceRateOfChange(double priceRateOfChange) {
		this.priceRateOfChange = priceRateOfChange;
	}
}
