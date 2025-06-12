package com.paymybuddy.controller;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.repository.TransactionRepository;
import com.paymybuddy.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AddFriendControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	private DBUser currentUser;
	private DBUser friend;

	@BeforeEach
	void setup() {
		transactionRepository.deleteAll();
		userRepository.deleteAll();

		currentUser = new DBUser();
		currentUser.setUsername("Martin");
		currentUser.setEmail("current@email.com");
		currentUser.setPassword("password");
		currentUser.setRole("USER");
		userRepository.save(currentUser);

		friend = new DBUser();
		friend.setUsername("Pierre");
		friend.setEmail("friend@email.com");
		friend.setPassword("password");
		friend.setRole("USER");
		userRepository.save(friend);
	}

	@Test
	@Transactional
	@WithMockUser(username = "current@email.com", roles = "USER")
	void testAddFriend_SuccessIntegration() throws Exception {
		mockMvc.perform(post("/user/add-friend").param("email", friend.getEmail()).with(csrf()))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/user/add-friend"))
				.andExpect(flash().attributeExists("successMessage"));

		DBUser updatedUser = userRepository.findByEmail(currentUser.getEmail());
		assertThat(updatedUser.getFriends()).extracting(DBUser::getEmail).contains(friend.getEmail());
	}
}
