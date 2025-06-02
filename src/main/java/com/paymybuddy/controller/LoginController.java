package com.paymybuddy.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.repository.UserRepository;

import org.springframework.ui.Model;


@Controller
public class LoginController {
	
	@GetMapping("/login")
	public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
	    if (error != null) {
	        model.addAttribute("loginError", "Identifiants invalides");
	    }
	    return "login";
	}

}
