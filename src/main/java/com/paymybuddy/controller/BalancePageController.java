package com.paymybuddy.controller;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.service.TransactionService;
import com.paymybuddy.service.UserService;

import org.springframework.ui.Model;

@Controller
public class BalancePageController {

	private static final Logger logger = LogManager.getLogger(BalancePageController.class);

	@Autowired
	UserService userService;

	@Autowired
	TransactionService transactionService;

	@GetMapping("/user/profil/balance")
	public String balancePage(Model model, Authentication auth) {
		String currentUserEmail = auth.getName();
		DBUser currentUser = userService.getUserByEmail(currentUserEmail);
		BigDecimal balance = currentUser.getBalance();
		model.addAttribute("balance", balance);

		return "balance";
	}

	@PostMapping("/user/profil/balance")
	public String updateBalance(@RequestParam BigDecimal amount, @RequestParam String action, Authentication auth,
			RedirectAttributes redirectAttributes) {
		String email = auth.getName();
		DBUser user = userService.getUserByEmail(email);

		if ("credit".equals(action)) {
			user.setBalance(user.getBalance().add(amount));
			redirectAttributes.addFlashAttribute("succesMessage", "Compte crédité de " + amount + " €.");
			logger.info("Le compte {} à bien été crédité de {} €", user.getEmail(), amount);
		} else if ("debit".equals(action)) {
			if (user.getBalance().compareTo(amount) < 0) {
				redirectAttributes.addFlashAttribute("errorMessage", "Fonds insuffisants pour débiter.");
				logger.warn("Fonds insuffisants pour débiter");
				return "redirect:balance";
			}
			user.setBalance(user.getBalance().subtract(amount));
			redirectAttributes.addFlashAttribute("succesMessage", "Compte débité de " + amount + " €.");
			logger.info("Le compte {} a bien été débité de {} €", user.getEmail(), amount);
		}

		userService.saveUser(user);
		redirectAttributes.addFlashAttribute("balance", user.getBalance());

		return "redirect:balance";
	}

	@GetMapping("/admin/profil/balance")
	public String showAdminProfil(Model model) {
		List<DBUser> allUser = userService.getAllUsers();
		List<Transaction> allTransaction = transactionService.getAllTransactions();
		BigDecimal totalFees = BigDecimal.ZERO;

		for (Transaction transaction : allTransaction) {
			if (transaction.getFee() != null) {
				totalFees = totalFees.add(transaction.getFee());
			}
		}
		model.addAttribute("totalFees", totalFees);
		model.addAttribute("users", allUser);

		return "balance";
	}

}
