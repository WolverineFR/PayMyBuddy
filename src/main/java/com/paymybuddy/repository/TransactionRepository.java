package com.paymybuddy.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.paymybuddy.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer>{
	@Query("SELECT SUM(t.fee) FROM Transaction t")
	BigDecimal getTotalFees();

}
