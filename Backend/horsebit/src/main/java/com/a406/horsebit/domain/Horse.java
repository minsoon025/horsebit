package com.a406.horsebit.domain;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "HORSE_INFO")
public class Horse {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "hr_no")
	private Long hrNo;

	@Column(name = "hr_name")
	private String hrName;

	@Column(name = "birth_place")
	private String birthPlace;

	@Column(name = "sex")
	private String sex;

	@Column(name = "hr_birth")
	private Timestamp hrBirth;

	@Column(name = "ow_name")
	private String owName;

	@Column(name = "hr_last_amt")
	private Long hrLastAmt;

	@Column(name = "father_hr_no")
	private Long fatherHrNo;

	@Column(name = "mother_hr_no")
	private Long motherHrNo;

	@Column(name = "race_rank")
	private String raceRank;

	@Column(name = "race_horse_flag")
	private boolean raceHorseFlag;

	@Column(name = "content")
	private String content;
}
