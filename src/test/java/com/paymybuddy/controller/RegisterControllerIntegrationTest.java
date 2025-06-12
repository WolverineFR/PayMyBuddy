package com.paymybuddy.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RegisterControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@BeforeEach
	void cleanDb() {
		transactionRepository.deleteAll();
		userRepository.deleteAll();
	}

	@Test
	void testRegisterUser_Integration() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/register").param("email", "martin@email.com")
				.param("username", "martin").param("password", "123").with(csrf()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login"));

		// verifier la sauvegarde de l'user
		DBUser savedUser = userRepository.findByEmail("martin@email.com");
		assert savedUser != null;
		assert savedUser.getUsername().equals("martin");
		assert passwordEncoder.matches("123", savedUser.getPassword());
		assert savedUser.getRole().equals("USER");
	}
}
