package com.a406.horsebit.dto;

import java.sql.Timestamp;
import java.util.Date;

public class HorseDTO {
	private Long hrNo;
	private String hrName;
	private String code;
	private String content;
	private Date publishDate;
	private double supply;
	private double marketCap;
	private String owName;
	private String birthPlace;
	private Long fatherHrNo;
	private String fatherHrName;
	private Long motherHrNo;
	private String motherHrName;
	private String raceRank;
	private boolean raceHorseFlag;

	public HorseDTO(Long hrNo, String hrName, String birthPlace, String owName, Long fatherHrNo, Long motherHrNo, String raceRank, String content, boolean raceHorseFlag) {
		this.hrNo = hrNo;
		this.hrName = hrName;
		this.birthPlace = birthPlace;
		this.owName = owName;
		this.fatherHrNo = fatherHrNo;
		this.motherHrNo = motherHrNo;
		this.raceRank = raceRank;
		this.content = content;
		this.raceHorseFlag = raceHorseFlag;
	}

	public Long getHrNo() {
		return hrNo;
	}

	public void setHrNo(Long hrNo) {
		this.hrNo = hrNo;
	}

	public String getHrName() {
		return hrName;
	}

	public void setHrName(String hrName) {
		this.hrName = hrName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public double getSupply() {
		return supply;
	}

	public void setSupply(double supply) {
		this.supply = supply;
	}

	public double getMarketCap() {
		return marketCap;
	}

	public void setMarketCap(double marketCap) {
		this.marketCap = marketCap;
	}

	public String getOwName() {
		return owName;
	}

	public void setOwName(String owName) {
		this.owName = owName;
	}

	public String getBirthPlace() {
		return birthPlace;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	public Long getFatherHrNo() {
		return fatherHrNo;
	}

	public void setFatherHrNo(Long fatherHrNo) {
		this.fatherHrNo = fatherHrNo;
	}

	public String getFatherHrName() {
		return fatherHrName;
	}

	public void setFatherHrName(String fatherHrName) {
		this.fatherHrName = fatherHrName;
	}

	public Long getMotherHrNo() {
		return motherHrNo;
	}

	public void setMotherHrNo(Long motherHrNo) {
		this.motherHrNo = motherHrNo;
	}

	public String getMotherHrName() {
		return motherHrName;
	}

	public void setMotherHrName(String motherHrName) {
		this.motherHrName = motherHrName;
	}

	public String getRaceRank() {
		return raceRank;
	}

	public void setRaceRank(String raceRank) {
		this.raceRank = raceRank;
	}

	public boolean isRaceHorseFlag() {
		return raceHorseFlag;
	}

	public void setRaceHorseFlag(boolean raceHorseFlag) {
		this.raceHorseFlag = raceHorseFlag;
	}
}
