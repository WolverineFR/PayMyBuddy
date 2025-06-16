package com.paymybuddy.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProfilControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	private DBUser savedUser;

	@BeforeEach
	void setup() {
		transactionRepository.deleteAll();
		userRepository.deleteAll();

		DBUser user = new DBUser();
		user.setUsername("Martin");
		user.setEmail("martin@email.com");
		user.setPassword("password");
		user.setRole("USER");
		savedUser = userService.saveUser(user);
	}

	@Test
	@WithMockUser(username = "martin@email.com", roles = "USER")
	void testShowProfil_ReturnsProfilViewWithUser() throws Exception {
		mockMvc.perform(get("/user/profil")).andExpect(status().isOk()).andExpect(view().name("profil"))
				.andExpect(model().attribute("user", Matchers.hasProperty("email", Matchers.is("martin@email.com"))))
				.andExpect(model().attribute("user", Matchers.hasProperty("username", Matchers.is("Martin"))));

	}

	@Test
	@WithMockUser(username = "martin@email.com", roles = "USER")
	void testUpdateProfil_Success() throws Exception {
		mockMvc.perform(post("/user/profil").param("username", "martin59").param("email", "martin@email.com")
				.param("password", "newpassword").with(csrf())).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user/profil"));

		DBUser updatedUser = userService.getUserByEmail("martin@email.com");
		assert updatedUser.getUsername().equals("martin59");
	}

	@Test
	@WithMockUser(username = "martin@email.com", roles = "USER")
	void testUpdateProfil_EmailAlreadyUsed() throws Exception {
		DBUser otherUser = new DBUser();
		otherUser.setUsername("Other");
		otherUser.setEmail("second@email.com");
		otherUser.setPassword("pass");
		otherUser.setRole("USER");
		userService.saveUser(otherUser);

		mockMvc.perform(post("/user/profil").param("username", "Martin").param("email", "second@email.com")
				.param("password", "newpassword").with(csrf())).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user/profil"));
	}
}
