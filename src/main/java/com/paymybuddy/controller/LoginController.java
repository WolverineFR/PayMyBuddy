package com.paymybuddy.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.ui.Model;

@Controller
public class LoginController {

	private static final Logger logger = LogManager.getLogger(LoginController.class);

	/**
	 * Affiche la page de connexion. Affiche un message d'erreur si la connexion a
	 * échoué.
	 * 
	 * @param error paramètre optionnel indiquant une erreur de login
	 * @param model modèle Spring MVC pour passer des attributs à la vue
	 * @return nom de la vue "login"
	 */
	@GetMapping("/login")
	public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
		if (error != null) {
			model.addAttribute("loginError", "Identifiants invalides");
			logger.warn("Identifiants de connexion incorrects");
		}
		return "login";
	}

}
