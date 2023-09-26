package com.a406.horsebit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.a406.horsebit.domain.Possess;

public interface PossessRepository extends JpaRepository<Possess, Long> {
	List<Possess> findPossessesByUserNo(Long userNo);
}
