package com.paymybuddy.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.repository.UserRepository;

@Controller
public class SendTransactionController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	TransactionRepository transactionRepository;

	@GetMapping("/user/transaction")
	public String showTransactionPage(Authentication auth, Model model) {
		String currentUserEmail = auth.getName();
		DBUser currentUser = userRepository.findByEmail(currentUserEmail);

		List<DBUser> friends = currentUser.getFriends();
		BigDecimal balance = currentUser.getBalance();
		model.addAttribute("friends", friends);
		model.addAttribute("balance", balance);

		return "transaction";
	}

	@PostMapping("/user/transaction")
	public String sendTransaction(@RequestParam("friendEmail") String friendEmail,
			@RequestParam(required = false) String description, @RequestParam BigDecimal amount, Authentication auth,
			Model model) {

		String senderEmail = auth.getName();
		DBUser sender = userRepository.findByEmail(senderEmail);
		DBUser receiver = userRepository.findByEmail(friendEmail);

		BigDecimal feeRate = new BigDecimal("0.005");
		BigDecimal fees = amount.multiply(feeRate).setScale(2, RoundingMode.HALF_UP);

		BigDecimal totalDebit = amount.add(fees);
		
		model.addAttribute("friends", sender.getFriends());
		model.addAttribute("balance", sender.getBalance());

		if (sender.getBalance().compareTo(totalDebit) < 0) {
			model.addAttribute("errorMessage", "Fonds insuffisants. Vous devez avoir au moins " + totalDebit + " € pour pouvoir payer les frais de transaction.");

		} else {

			sender.setBalance(sender.getBalance().subtract(totalDebit));
			receiver.setBalance(receiver.getBalance().add(amount));

			Transaction transaction = new Transaction();
			transaction.setSender(sender);
			transaction.setReceiver(receiver);
			transaction.setDescription(description);
			transaction.setAmount(amount);
			transaction.setTimestamp(LocalDateTime.now());
			transaction.setFee(fees);
			transactionRepository.save(transaction);

			userRepository.save(sender);
			userRepository.save(receiver);

			model.addAttribute("successMessage", "Montant envoyé avec succès !");

		}
		return "transaction";
	}
}
