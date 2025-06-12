package com.paymybuddy.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.repository.UserRepository;

@WebMvcTest(controllers = ProfilController.class)
class ProfilControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private BCryptPasswordEncoder passwordEncoder;

	@Test
	@WithMockUser(username = "martin@email.com")
	void testShowProfil_ReturnsView() throws Exception {
		DBUser mockUser = new DBUser();
		mockUser.setUsername("Martin");
		mockUser.setEmail("martin@email.com");

		when(userRepository.findByEmail("martin@email.com")).thenReturn(mockUser);

		mockMvc.perform(get("/user/profil")).andExpect(status().isOk()).andExpect(view().name("profil"))
				.andExpect(model().attributeExists("user"));
	}

	@Test
	@WithMockUser(username = "martin@email.com", roles = "USER")
	void testUpdateProfil_SuccessMessage() throws Exception {
		DBUser currentUser = new DBUser();
		currentUser.setEmail("martin@email.com");

		when(userRepository.findByEmail("martin@email.com")).thenReturn(currentUser);
		when(userRepository.findByEmail("nouveau@email.com")).thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders.post("/user/profil").param("username", "martin")
				.param("email", "nouveau@email.com").param("password", "123").with(csrf()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/profil"));

		verify(userRepository).save(any(DBUser.class));
	}

	@Test
	@WithMockUser(username = "martin@email.com", roles = "USER")
	void testUpdateProfil_EmailAlreadyUsed() throws Exception {
		DBUser currentUser = new DBUser();
		currentUser.setEmail("martin@email.com");

		DBUser secondUser = new DBUser();
		secondUser.setEmail("second@email.com");

		when(userRepository.findByEmail("martin@email.com")).thenReturn(currentUser);
		when(userRepository.findByEmail("second@email.com")).thenReturn(secondUser);

		mockMvc.perform(MockMvcRequestBuilders.post("/user/profil").param("username", "martin")
				.param("email", "second@email.com").param("password", "123").with(csrf()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/profil"));
	}

	@Test
	@WithMockUser(username = "martin@email.com")
	void testUpdateProfil_EmptyUsername() throws Exception {
		DBUser currentUser = new DBUser();
		currentUser.setEmail("martin@email.com");

		when(userRepository.findByEmail("martin@email.com")).thenReturn(currentUser);

		mockMvc.perform(post("/user/profil").param("username", "   ") // vide après trim
				.param("email", "nouveau@email.com").param("password", "123456").with(csrf()))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/profil"))
				.andExpect(flash().attribute("updateError", "Le nom d'utilisateur ne peut pas être vide."));
	}

	@Test
	@WithMockUser(username = "martin@email.com")
	void testUpdateProfil_EmptyEmail() throws Exception {
		DBUser currentUser = new DBUser();
		currentUser.setEmail("martin@email.com");

		when(userRepository.findByEmail("martin@email.com")).thenReturn(currentUser);

		mockMvc.perform(post("/user/profil").param("username", "martin").param("email", "  ") // vide après trim
				.param("password", "123456").with(csrf())).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user/profil"))
				.andExpect(flash().attribute("updateError", "L'adresse e-mail ne peut pas être vide."));
	}

	@Test
	@WithMockUser(username = "martin@email.com")
	void testUpdateProfil_EmptyPassword() throws Exception {
		DBUser currentUser = new DBUser();
		currentUser.setEmail("martin@email.com");

		when(userRepository.findByEmail("martin@email.com")).thenReturn(currentUser);

		mockMvc.perform(post("/user/profil").param("username", "martin").param("email", "nouveau@email.com")
				.param("password", "  ") // vide après trim
				.with(csrf())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/profil"))
				.andExpect(flash().attribute("updateError", "Le nouveau mot de passe ne peut pas être vide."));
	}
}
