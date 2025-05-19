package com.paymybuddy.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

	@PostMapping
	public DBUser addUser(@RequestBody DBUser user) {
		return userService.addUser(user);
	}

	@PutMapping("/{id}")
	public Optional<DBUser> editUser(@PathVariable Integer id, @RequestBody DBUser user) {
		return userService.editUser(id, user);
	}

	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable Integer id) {
		userService.deleteUserById(id);
	}

	@PutMapping("/{id}/add/{friendId}")
	public Optional<UserDTO> addFriend(@PathVariable Integer id, @PathVariable int friendId) {
		return userService.addFriend(id, friendId).map(user -> new UserDTO(user));
	}

}
