package com.paymybuddy.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.repository.UserRepository;

@Controller
public class RegisterController {

	private static final Logger logger = LogManager.getLogger(RegisterController.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/register")
	public String getRegisterPage(@RequestParam(value = "error", required = false) String error, Model model) {
		if (error != null) {
			model.addAttribute("registrationError", "Un compte existe déjà à cette adresse mail");
		}
		model.addAttribute("user", new DBUser());
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(@ModelAttribute("user") DBUser user, RedirectAttributes redirectAttributes) {
		if (userRepository.findByEmail(user.getEmail()) != null) {
			redirectAttributes.addAttribute("error", "true");
			logger.warn("Un compte existe déjà pour l'adresse {}", user.getEmail());
			return "redirect:/register";
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole("USER");
		userRepository.save(user);
		logger.info("L'utilisateur {} est enregistré avec succès", user.getEmail());
		return "redirect:/login";
	}
}
