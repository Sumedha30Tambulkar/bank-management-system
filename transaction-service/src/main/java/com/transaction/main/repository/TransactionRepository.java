package com.transaction.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transaction.main.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySenderAccount(String senderAccount);
    List<Transaction> findByReceiverAccount(String receiverAccount);
    List<Transaction> findBySenderAccountOrReceiverAccount(String senderAccount, String receiverAccount);
}
