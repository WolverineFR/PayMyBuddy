package com.paymybuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.repository.UserRepository;

@Controller
public class ProfilController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/user/profil")
	public String showProfilPage(Authentication auth, Model model) {
		String currentUserEmail = auth.getName();
		DBUser currentUser = userRepository.findByEmail(currentUserEmail);
		model.addAttribute("user", currentUser);
		return "profil";
	}

	@PostMapping("/user/profil")
	public String updateProfil(@RequestParam String username, @RequestParam String email, @RequestParam String password,
			Authentication auth, RedirectAttributes redirectAttributes) {
		String currentEmail = auth.getName();
		DBUser currentUser = userRepository.findByEmail(currentEmail);

		if (!email.equals(currentUser.getEmail()) && userRepository.findByEmail(email) != null) {
			redirectAttributes.addFlashAttribute("updateError", "Cet email est déjà utilisé.");
			return "redirect:/user/profil";
		}

		currentUser.setUsername(username);
		currentUser.setEmail(email);
		currentUser.setPassword(passwordEncoder.encode(password));

		userRepository.save(currentUser);

		redirectAttributes.addFlashAttribute("successMessage", "Profil mis à jour avec succès.");

		return "redirect:/user/profil";
	}

	@GetMapping("/user/profil/")
	public RedirectView redirectProfilWithSlash() {
		return new RedirectView("/user/profil");
	}
}
