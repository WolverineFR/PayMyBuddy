package com.paymybuddy.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.repository.UserRepository;

import org.springframework.ui.Model;

@Controller
public class BalancePageController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	TransactionRepository transactionRepository;

	@GetMapping("/user/profil/balance")
	public String balancePage(Model model, Authentication auth) {
		String currentUserEmail = auth.getName();
		DBUser currentUser = userRepository.findByEmail(currentUserEmail);
		BigDecimal balance = currentUser.getBalance();
		model.addAttribute("balance", balance);

		return "balance";
	}

	@PostMapping("/user/profil/balance")
	public String updateBalance(@RequestParam BigDecimal amount, @RequestParam String action, Authentication auth,
			RedirectAttributes redirectAttributes) {
		String email = auth.getName();
		DBUser user = userRepository.findByEmail(email);

		if ("credit".equals(action)) {
			user.setBalance(user.getBalance().add(amount));
			redirectAttributes.addFlashAttribute("succesMessage", "Compte crédité de " + amount + " €.");
		} else if ("debit".equals(action)) {
			if (user.getBalance().compareTo(amount) < 0) {
				redirectAttributes.addFlashAttribute("errorMessage", "Fonds insuffisants pour débiter.");
				return "redirect:balance";
			}
			user.setBalance(user.getBalance().subtract(amount));
			redirectAttributes.addFlashAttribute("succesMessage", "Compte débité de " + amount + " €.");
		}

		userRepository.save(user);
		redirectAttributes.addFlashAttribute("balance", user.getBalance());

		return "redirect:balance";
	}
	
	@GetMapping("/admin/profil/balance")
	public String showAdminProfil(Model model) {
	    List<Transaction> allTransaction = transactionRepository.findAll();
	    BigDecimal totalFees= BigDecimal.ZERO;
	    
	    for (Transaction transaction : allTransaction) {
	    	 if (transaction.getFee() != null) {
	             totalFees = totalFees.add(transaction.getFee());
	         }
	    }
	    model.addAttribute("totalFees", totalFees);

	    return "balance";
	}


}
