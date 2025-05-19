package com.paymybuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.service.JWTService;
import com.paymybuddy.service.UserService;

@RestController
public class LoginController {

	public JWTService jwtService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userSerice;

	private final AuthenticationManager authenticationManager;

	public LoginController(JWTService jwtService, AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	@PostMapping("/token")
	public String getToken(Authentication auth) {
		String token = jwtService.generateToken(auth);
		return token;
	}

	/*
	 * private OAuth2AuthorizedClientService authorizedClientService;
	 * 
	 * public LoginController (OAuth2AuthorizedClientService
	 * authorizedClientService) { this.authorizedClientService =
	 * authorizedClientService; }
	 */

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

	/*
	 * 
	 * @PostMapping("/register") public ResponseEntity<?> registerUser(@RequestBody
	 * DBUser user) { if (userRepository.findByUsername(user.getUsername()) != null)
	 * { return ResponseEntity.badRequest().body("Username already exists"); }
	 * 
	 * if (userRepository.findByEmail(user.getEmail()) != null) { return
	 * ResponseEntity.badRequest().body("Email already exists"); } DBUser newUser =
	 * userSerice.addUser(user); return
	 * ResponseEntity.status(HttpStatus.CREATED).body(newUser); }
	 * 
	 */

	@GetMapping("/")
	public String getResource() {
		return "a value...";
	}

	@GetMapping("/user")
	public String getUser() {
		return "Bienvenue, user";
	}

	@GetMapping("/admin")
	public String getAdmin() {
		return "Bienvenue, Admin";
	}
}
