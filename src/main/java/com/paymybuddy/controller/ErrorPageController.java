package com.paymybuddy.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorPageController implements ErrorController{
/*
 * Affichage de la page d'erreur en cas de mauvais chemin d'acces url.
 */
	@RequestMapping("/error")
	public String errorPage() {
		return "error";
	}
}
