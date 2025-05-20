package com.paymybuddy.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.repository.UserRepository;

import org.springframework.ui.Model;


@Controller
public class LoginController {

	@Autowired
	private UserRepository userRepository;
	
	/*
	@GetMapping("/login")
	public String showLoginPage() {
		return "login";
	}

	*/
	
	@GetMapping("/user")
	public String getUserPage(Authentication authentication, Model model) {
	    String username = authentication.getName();
	    DBUser user = userRepository.findByUsername(username);
	    model.addAttribute("user", user);
	    return "user";
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



}
