package com.paymybuddy.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.dto.TransactionDTO;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.service.TransactionService;

@RestController
@RequestMapping("/admin/transaction")
@PreAuthorize("hasRole('ADMIN')")
public class TransactionController {

	private TransactionService transactionService;
	
	@Autowired
	public TransactionController (TransactionService transactionService) {
		this.transactionService = transactionService;
	}
	
	@GetMapping("/all")
	public List<Transaction> getAll() {
		return transactionService.getAll();
	}
	
	@GetMapping("/{id}")
	public Optional<Transaction> getTransactionById (@PathVariable Integer id) {
		Optional<Transaction> transactionById = transactionService.getTransactionById(id);
		return transactionById;
	}
	
	@PostMapping
	public Transaction addTransaction(@RequestBody TransactionDTO transactionDTO) {
		return transactionService.addTransaction(transactionDTO);
	}
	
	
}
