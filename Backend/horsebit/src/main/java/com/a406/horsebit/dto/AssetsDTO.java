package com.a406.horsebit.dto;

public class AssetsDTO {
	private double totalAsset;
	private double cashBalance;
	private double totalPurchase;
	private double totalEvaluation;
	private double profitOrLoss;
	private double returnRate;

	public double getTotalAsset() {
		return totalAsset;
	}

	public void setTotalAsset(double totalAsset) {
		this.totalAsset = totalAsset;
	}

	public double getCashBalance() {
		return cashBalance;
	}

	public void setCashBalance(double cashBalance) {
		this.cashBalance = cashBalance;
	}

	public double getTotalPurchase() {
		return totalPurchase;
	}

	public void setTotalPurchase(double totalPurchase) {
		this.totalPurchase = totalPurchase;
	}

	public double getTotalEvaluation() {
		return totalEvaluation;
	}

	public void setTotalEvaluation(double totalEvaluation) {
		this.totalEvaluation = totalEvaluation;
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
