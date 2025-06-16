package com.paymybuddy.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

/**
 * Écouteur d'événements de succès d'authentification. Permet de logger les
 * connexions réussies des utilisateurs.
 */
@Component
public class AuthenticationSuccessListener {

	private static final Logger logger = LogManager.getLogger(AuthenticationSuccessListener.class);

	/**
	 * Méthode appelée automatiquement lorsqu'une authentification réussit.
	 *
	 * @param event l'événement de succès d'authentification
	 */
	@EventListener
	public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
		String email = event.getAuthentication().getName();
		logger.info("L'utilisateur {} s'est connecté avec succès.", email);
	}
}