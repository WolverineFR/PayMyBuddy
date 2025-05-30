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
	
	/*
	 * private OAuth2AuthorizedClientService authorizedClientService;
	 * 
	 * public LoginController (OAuth2AuthorizedClientService
	 * authorizedClientService) { this.authorizedClientService =
	 * authorizedClientService; }
	 */
/*
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody DBUser user) {
		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
			String token = jwtService.generateToken(authentication);
			return ResponseEntity.ok(token);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiants invalides");
		}
	}
*/



}
