package com.paymybuddy.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.repository.UserRepository;

@Controller
public class SendTransactionController {

	private static final Logger logger = LogManager.getLogger(SendTransactionController.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	TransactionRepository transactionRepository;

	@GetMapping("/user/transaction")
	public String showTransactionPage(Authentication auth, Model model) {
		String currentUserEmail = auth.getName();
		DBUser currentUser = userRepository.findByEmail(currentUserEmail);
		int userId = currentUser.getId();
		List<Transaction> transactions = transactionRepository.findAll();
		List<Transaction> userTransactions = new ArrayList<>();

		for (Transaction transaction : transactions) {
			DBUser senderUser = transaction.getSender();

			if (senderUser.getId() == userId) {
				userTransactions.add(transaction);
			}
		}

		model.addAttribute("transactions", userTransactions);

		List<DBUser> friends = currentUser.getFriends();
		BigDecimal balance = currentUser.getBalance();
		model.addAttribute("friends", friends);
		model.addAttribute("balance", balance);

		return "transaction";
	}

	@PostMapping("/user/transaction")
	public String sendTransaction(@RequestParam("friendEmail") String friendEmail,
			@RequestParam(required = false) String description, @RequestParam BigDecimal amount, Authentication auth,
			RedirectAttributes redirectAttributes) {

		String senderEmail = auth.getName();
		logger.info("Début de l'envoi de transaction de {} vers {}", senderEmail, friendEmail);

		DBUser sender = userRepository.findByEmail(senderEmail);
		if (sender == null) {
			logger.error("L'utilisateur {} est introuvable.", senderEmail);
			redirectAttributes.addFlashAttribute("errorMessage", "Utilisateur introuvable.");
			return "redirect:/user/transaction";
		}
		boolean isFriend = sender.getFriends().stream()
				.anyMatch(friend -> friend.getEmail().equalsIgnoreCase(friendEmail));

		if (!isFriend) {
			logger.warn("Transaction impossible car l'utilisateur {} n'est pas ami avec {}", friendEmail, senderEmail);
			redirectAttributes.addFlashAttribute("errorMessage", "Destinataire non valide");
			return "redirect:/user/transaction";
		}

		DBUser receiver = userRepository.findByEmail(friendEmail);
		if (receiver == null) {
			logger.warn("Destinataire introuvable : {}", friendEmail);
			redirectAttributes.addFlashAttribute("errorMessage", "Destinataire introuvable.");
			return "redirect:/user/transaction";
		}

		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			logger.warn("Montant invalide : {} envoyé par {}", amount, senderEmail);
			redirectAttributes.addFlashAttribute("errorMessage", "Montant invalide.");
			return "redirect:/user/transaction";
		}

		BigDecimal feeRate = new BigDecimal("0.005");
		BigDecimal fees = amount.multiply(feeRate).setScale(2, RoundingMode.HALF_UP);
		BigDecimal totalDebit = amount.add(fees);

		if (sender.getBalance().compareTo(totalDebit) < 0) {
			logger.warn("Transaction échouée : solde insuffisant pour l'utilisateur {}", senderEmail);
			redirectAttributes.addFlashAttribute("errorMessage",
					"Fonds insuffisants. Il vous manque " + fees + " € pour pouvoir payer les frais de transaction.");
			return "redirect:/user/transaction";
		}

		try {
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

			logger.info("Transaction enregistrée : {} envoie {} € à {}", senderEmail, amount, friendEmail);
			logger.info("Frais appliqués : {} €", fees);

			redirectAttributes.addFlashAttribute("successMessage", "Montant envoyé avec succès !");

		} catch (Exception e) {
			logger.error("Erreur lors de la transaction de {} vers {} : {}", senderEmail, friendEmail, e.getMessage(),
					e);
			redirectAttributes.addFlashAttribute("errorMessage", "Erreur interne lors de la transaction.");
		}

		return "redirect:/user/transaction";
	}

	@GetMapping("/user/transaction/")
	public RedirectView redirectTransactionWithSlash() {
		return new RedirectView("/user/transaction");
	}
}
