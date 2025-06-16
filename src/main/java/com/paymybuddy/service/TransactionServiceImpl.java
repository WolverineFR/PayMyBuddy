package com.paymybuddy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.model.Transaction;
import com.paymybuddy.repository.TransactionRepository;

/**
 * Implémentation du service de gestion des transactions.
 */
@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	 /**
     * Enregistre une transaction dans la base de données.
     * 
     * @param transaction la transaction à sauvegarder
     * @return la transaction enregistrée
     */
	@Override
	public Transaction saveTransaction(Transaction transaction) {
		return transactionRepository.save(transaction);
	}
	
	/**
     * Récupère toutes les transactions de la base de données.
     * 
     * @return la liste des transactions
     */
	@Override
	public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
