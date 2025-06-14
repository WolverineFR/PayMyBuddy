package com.paymybuddy.service;

import java.util.List;

import com.paymybuddy.model.Transaction;

public interface TransactionService {
    Transaction saveTransaction(Transaction transaction);
    List<Transaction> getAllTransactions();
}
