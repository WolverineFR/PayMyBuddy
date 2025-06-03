package com.paymybuddy.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AddFriendController.class)
class AddFriendControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserRepository userRepository;

	@Test
	@WithMockUser(username = "current@email.com", roles = "USER")
	void testAddFriend_Success() throws Exception {
		DBUser currentUser = new DBUser();
		currentUser.setId(1);
		currentUser.setEmail("current@email.com");
		currentUser.setFriends(new ArrayList<>());

		DBUser friend = new DBUser();
		friend.setId(2);
		friend.setEmail("friend@email.com");

		when(userRepository.findByEmail("current@email.com")).thenReturn(currentUser);
		when(userRepository.findByEmail("friend@email.com")).thenReturn(friend);

		mockMvc.perform(post("/user/add-friend").param("email", "friend@email.com").with(csrf())
				.principal(() -> "current@email.com")).andExpect(status().isOk()).andExpect(view().name("add-friend"))
				.andExpect(model().attributeExists("successMessage"));

		verify(userRepository).save(currentUser);
		assertThat(currentUser.getFriends()).contains(friend);
	}

	@Test
	@WithMockUser(username = "current@email.com", roles = "USER")
	void testAddFriend_UserNotFound() throws Exception {
		DBUser currentUser = new DBUser();
		currentUser.setId(1);
		currentUser.setEmail("current@email.com");
		currentUser.setFriends(new ArrayList<>());

		when(userRepository.findByEmail("current@email.com")).thenReturn(currentUser);
		when(userRepository.findByEmail("unknown@email.com")).thenReturn(null);

		mockMvc.perform(post("/user/add-friend").param("email", "unknown@email.com").with(csrf())
				.principal(() -> "current@email.com")).andExpect(status().isOk()).andExpect(view().name("add-friend"))
				.andExpect(model().attributeExists("errorMessage"))
				.andExpect(model().attribute("errorMessage", "Cet utilisateur n'existe pas."));
	}

	@Test
	@WithMockUser(username = "current@email.com", roles = "USER")
	void testAddFriend_AddSelf() throws Exception {
		DBUser currentUser = new DBUser();
		currentUser.setId(1);
		currentUser.setEmail("current@email.com");
		currentUser.setFriends(new ArrayList<>());

		when(userRepository.findByEmail("current@email.com")).thenReturn(currentUser);

		mockMvc.perform(post("/user/add-friend").param("email", "current@email.com").with(csrf())
				.principal(() -> "current@email.com")).andExpect(status().isOk()).andExpect(view().name("add-friend"))
				.andExpect(model().attributeExists("errorMessage"))
				.andExpect(model().attribute("errorMessage", "Vous ne pouvez pas vous ajouter vous-même."));
	}

	@Test
	@WithMockUser(username = "current@email.com", roles = "USER")
	void testAddFriend_AlreadyFriend() throws Exception {
		DBUser currentUser = new DBUser();
		currentUser.setId(1);
		currentUser.setEmail("current@email.com");

		DBUser friend = new DBUser();
		friend.setId(2);
		friend.setEmail("friend@email.com");

		currentUser.setFriends(new ArrayList<>());
		currentUser.getFriends().add(friend);

		when(userRepository.findByEmail("current@email.com")).thenReturn(currentUser);
		when(userRepository.findByEmail("friend@email.com")).thenReturn(friend);

		mockMvc.perform(post("/user/add-friend").param("email", "friend@email.com").with(csrf())
				.principal(() -> "current@email.com")).andExpect(status().isOk()).andExpect(view().name("add-friend"))
				.andExpect(model().attributeExists("errorMessage"))
				.andExpect(model().attribute("errorMessage", "Cet utilisateur est déjà votre ami."));
	}

}
