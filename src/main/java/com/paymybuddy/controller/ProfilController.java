package com.paymybuddy.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

	private static final Logger logger = LogManager.getLogger(ProfilController.class);

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
			logger.warn("L'email {} existe déjà", email);
			return "redirect:/user/profil";
		}

		if (username == null || username.trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("updateError", "Le nom d'utilisateur ne peut pas être vide.");
			logger.warn("Tentative de mise à jour de profil avec un nom d'utilisateur vide pour {}",
					currentUser.getEmail());
			return "redirect:/user/profil";
		}

		if (email == null || email.trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("updateError", "L'adresse e-mail ne peut pas être vide.");
			logger.warn("Tentative de mise à jour de profil avec un e-mail vide pour {}", currentUser.getEmail());
			return "redirect:/user/profil";
		}

		if (password != null && !password.trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("updateError", "Le nouveau mot de passe ne peut pas être vide.");
			logger.warn("Tentative de mise à jour du mot de passe vide pour {}", currentUser.getEmail());
			return "redirect:/user/profil";
		}

		currentUser.setUsername(username);
		currentUser.setEmail(email);
		currentUser.setPassword(passwordEncoder.encode(password));

		userRepository.save(currentUser);

		Authentication newAuth = new UsernamePasswordAuthenticationToken(currentUser.getEmail(),
				currentUser.getPassword(), auth.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(newAuth);

		redirectAttributes.addFlashAttribute("successMessage", "Profil mis à jour avec succès.");
		logger.info("Les données de l'utilisateur {} ont bien été modifiées", currentUser.getEmail());
		return "redirect:/user/profil";
	}

	@GetMapping("/user/profil/")
	public RedirectView redirectProfilWithSlash() {
		return new RedirectView("/user/profil");
	}
}
