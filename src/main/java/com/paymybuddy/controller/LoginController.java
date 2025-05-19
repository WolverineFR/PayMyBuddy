package com.paymybuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.service.UserService;

@RestController
public class LoginController {
	
	@Autowired
	private  UserRepository userRepository;
	
	@Autowired
	private UserService userSerice;
	
	private final AuthenticationManager authenticationManager;
	
	public LoginController (AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	

	/*
	private OAuth2AuthorizedClientService authorizedClientService;
	
	public LoginController (OAuth2AuthorizedClientService authorizedClientService) {
		this.authorizedClientService = authorizedClientService;
	}
	*/
	
	@PostMapping
	public ResponseEntity<?> regiusterUser(@RequestBody DBUser user) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
			return ResponseEntity.ok("Connexion réussi");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mauvais nom d'utilisateur ou mot de passe");
		}
	}
	
	
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody DBUser user) {
		if (userRepository.findByUsername(user.getUsername()) != null) {
			return ResponseEntity.badRequest().body("Username already exists");
		}
		
		if (userRepository.findByEmail(user.getEmail()) != null) {
	        return ResponseEntity.badRequest().body("Email already exists");
	    }
		DBUser newUser = userSerice.addUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
	}
}
