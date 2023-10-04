package com.a406.horsebit.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.a406.horsebit.domain.Token;
import com.a406.horsebit.dto.HorseDTO;
import com.a406.horsebit.dto.TokenDTO;

import jakarta.persistence.Column;

public interface TokenRepository extends JpaRepository<Token, Long> {
	@Query("select t.tokenNo from Token t where not t.code like 'KRW'")
	List<Long> findAllTokenNos();

	@Query("select NEW com.a406.horsebit.dto.TokenDTO(t.tokenNo, t.name, t.code, t.newFlag) from Token t where not t.code like 'KRW'")
	List<TokenDTO> findAllTokens();

	@Query("select NEW com.a406.horsebit.dto.TokenDTO(t.tokenNo, t.name, t.code, t.newFlag) from Token t where t.tokenNo = :tokenNo")
	TokenDTO findTokenByTokenNo(Long tokenNo);

	@Query("select NEW com.a406.horsebit.dto.TokenDTO(t.tokenNo, t.name, t.code, t.supply, t.publishDate, t.newFlag) from Token t where t.tokenNo = :tokenNo")
	TokenDTO findTokenInfoByTokenNo(Long tokenNo);

	@Query("select r.hrNo from HorseTokenRel r where r.tokenNo = :tokenNo")
	Long findHrNoByTokenNo(Long tokenNo);

	@Query("select NEW com.a406.horsebit.dto.HorseDTO(h.hrNo, h.hrName, h.birthPlace, h.owName, h.fatherHrNo, h.motherHrNo, h.raceRank, h.content, h.raceHorseFlag) from Horse h where h.hrNo = :hrNo")
	HorseDTO findHorseByTokenNo(Long hrNo);

	@Query("select h.hrName from Horse h where h.hrNo = :hrNo")
	String findHrNameByTokenNo(Long hrNo);
}
