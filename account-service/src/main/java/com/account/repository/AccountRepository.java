package com.account.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.account.model.Account;

public interface AccountRepository extends JpaRepository <Account, Long> {
	Optional<Account> findByUserEmail(String userEmail);
}
