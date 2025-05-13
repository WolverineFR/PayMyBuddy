package com.paymybuddy.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.dto.UserDTO;
import com.paymybuddy.model.DBUser;
import com.paymybuddy.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	private UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/all")
	public List<UserDTO> getAllUser() {
		List<DBUser> users = userService.getUsers();
		return users.stream().map(UserDTO::new).collect(Collectors.toList());

	}
	
	@GetMapping("{id}")
	public UserDTO getUserById(@PathVariable Integer id) {
		Optional<DBUser> userById = userService.getUserById(id);
		return new UserDTO(userById.get());
	}
}
