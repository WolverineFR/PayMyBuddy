package com.paymybuddy.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.dto.TransactionDTO;
import com.paymybuddy.model.DBUser;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {

	private static final BigDecimal FEE_PERCENTAGE = new BigDecimal("0.005"); // 0.5% de frais
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TransactionRepository transactionRepository;
	

	public List<Transaction> getAll() {
		return transactionRepository.findAll();
	}

	public Optional<Transaction> getTransactionById(int id) {
		return transactionRepository.findById(id);
	}

	@Transactional
	public Transaction addTransaction(TransactionDTO transactionDTO) {
		DBUser sender = userRepository.findById(transactionDTO.getSenderId())
				.orElseThrow(() -> new IllegalArgumentException("Sender not found"));
		DBUser receiver = userRepository.findById(transactionDTO.getReceiverId())
				.orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

		BigDecimal fee = transactionDTO.getAmount().multiply(FEE_PERCENTAGE);
		BigDecimal totalAmount = transactionDTO.getAmount().add(fee);

		if (sender.getBalance().compareTo(totalAmount) < 0) {
			throw new IllegalArgumentException("Fonds insuffisant");
		}

		sender.setBalance(sender.getBalance().subtract(totalAmount));
		receiver.setBalance(receiver.getBalance().add(transactionDTO.getAmount()));

		userRepository.save(sender);
		userRepository.save(receiver);

		Transaction transaction = new Transaction();
		transaction.setSender(sender);
		transaction.setReceiver(receiver);
		transaction.setDescription(transactionDTO.getDescription());
		transaction.setAmount(transactionDTO.getAmount());
		transaction.setFee(fee);
		transaction.setTimestamp(LocalDateTime.now());

		return transactionRepository.save(transaction);
	}

}
