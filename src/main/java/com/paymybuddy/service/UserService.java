package com.paymybuddy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public List<DBUser> getUsers() {
		return userRepository.findAll();
	}
	
	public Optional<DBUser> getUserById(int id) {
		return userRepository.findById(id);
	}
	
	public DBUser addUser (DBUser user) {
		return userRepository.save(user);
	}
	
	public Optional<DBUser> editUser (int id, DBUser user) {
		
		return userRepository.findById(id).map(editedUser -> {
			editedUser.setUsername(user.getUsername());
			editedUser.setEmail(user.getEmail());
			editedUser.setPassword(user.getPassword());
			return userRepository.save(editedUser);
		});
	}
	
	public void deleteUserById (int id) {
		userRepository.deleteById(id);
	}

}
