package com.a406.horsebit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.a406.horsebit.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
	List<Account> findAccountsByUserNo(Long userNo);
}
