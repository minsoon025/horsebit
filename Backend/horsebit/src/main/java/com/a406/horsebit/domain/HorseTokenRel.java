package com.a406.horsebit.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(HorseTokenRelPK.class)
@Table(name = "REL_HORSE_TOKEN")
public class HorseTokenRel {
	@Id
	@Column(name = "token_no")
	private Long tokenNo;

	@Id
	@Column(name = "hr_no")
	private Long hrNo;
}
