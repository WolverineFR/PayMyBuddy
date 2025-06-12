package com.paymybuddy.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.dto.UserDTO;
import com.paymybuddy.model.DBUser;
import com.paymybuddy.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser(roles = "ADMIN")
@WebMvcTest(UserController.class)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@Autowired
	private ObjectMapper objectMapper; // pour JSON <-> objet

	private DBUser user1;
	private DBUser user2;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);

		user1 = new DBUser();
		user1.setId(1);
		user1.setUsername("user1");
		user1.setEmail("user1@example.com");

		user2 = new DBUser();
		user2.setId(2);
		user2.setUsername("user2");
		user2.setEmail("user2@example.com");
	}

	@Test
	void testGetAllUser() throws Exception {
		when(userService.getUsers()).thenReturn(Arrays.asList(user1, user2));

		mockMvc.perform(get("/admin/all")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(user1.getId()))
				.andExpect(jsonPath("$[0].username").value(user1.getUsername()))
				.andExpect(jsonPath("$[1].email").value(user2.getEmail()));

		verify(userService, times(1)).getUsers();
	}

	@Test
	void testGetUserById() throws Exception {
		when(userService.getUserById(1)).thenReturn(Optional.of(user1));

		mockMvc.perform(get("/admin/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(user1.getId()))
				.andExpect(jsonPath("$.username").value(user1.getUsername()));

		verify(userService, times(1)).getUserById(1);
	}

	@Test
	void testAddUser() throws Exception {
		DBUser newUser = new DBUser();
		newUser.setUsername("newuser");
		newUser.setEmail("newuser@example.com");
		newUser.setPassword("password");

		when(userService.addUser(any(DBUser.class))).thenReturn(newUser);

		mockMvc.perform(post("/admin").with(csrf())
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(newUser)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.username").value("newuser"));

		verify(userService, times(1)).addUser(any(DBUser.class));
	}

	@Test
	void testEditUser() throws Exception {
		DBUser updatedUser = new DBUser();
		updatedUser.setId(1);
		updatedUser.setUsername("updateduser");
		updatedUser.setEmail("updated@example.com");
		updatedUser.setPassword("newpassword");

		when(userService.editUser(eq(1), any(DBUser.class))).thenReturn(Optional.of(updatedUser));

		mockMvc.perform(put("/admin/1").with(csrf())
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedUser)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.username").value("updateduser"));

		verify(userService, times(1)).editUser(eq(1), any(DBUser.class));
	}

	@Test
	void testDeleteUser() throws Exception {
		doNothing().when(userService).deleteUserById(1);

		mockMvc.perform(delete("/admin/1").with(csrf()))
				.andExpect(status().isOk());

		verify(userService, times(1)).deleteUserById(1);
	}

	@Test
	void testAddFriend() throws Exception {
		when(userService.addFriend(1, 2)).thenReturn(Optional.of(user1));

		mockMvc.perform(put("/admin/1/add/2").with(csrf())).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(user1.getId()))
				.andExpect(jsonPath("$.username").value(user1.getUsername()));

		verify(userService, times(1)).addFriend(1, 2);
	}
}
