package com.paymybuddy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public DBUser getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	@Override
	public List<DBUser> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public DBUser saveUser(DBUser user) {
		return userRepository.save(user);
	}
}
