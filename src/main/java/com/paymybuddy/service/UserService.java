package com.paymybuddy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@PreAuthorize("hasRole('ADMIN')")
	public List<DBUser> getUsers() {
		return userRepository.findAll();
	}

	@PreAuthorize("hasRole('ADMIN')")
	public Optional<DBUser> getUserById(int id) {
		return userRepository.findById(id);
	}

	@PreAuthorize("hasRole('ADMIN')")
	public DBUser addUser(DBUser user) {
		String passwordCrypted = passwordEncoder.encode(user.getPassword());
		user.setPassword(passwordCrypted);
		return userRepository.save(user);
	}

	@PreAuthorize("hasRole('ADMIN')")
	public Optional<DBUser> editUser(int id, DBUser user) {
		String passwordCrypted = passwordEncoder.encode(user.getPassword());
		return userRepository.findById(id).map(editedUser -> {
			editedUser.setUsername(user.getUsername());
			editedUser.setEmail(user.getEmail());
			editedUser.setPassword(passwordCrypted);
			return userRepository.save(editedUser);
		});
	}

	@PreAuthorize("hasRole('ADMIN')")
	public void deleteUserById(int id) {
		userRepository.deleteById(id);
	}

	@PreAuthorize("hasRole('ADMIN')")
	public Optional<DBUser> addFriend(int id, int friendId) {
		if (id != friendId) {
			Optional<DBUser> alreadyExist = getUserById(friendId);
			Optional<DBUser> userId = getUserById(id);
			
			if (alreadyExist.isEmpty()) {
				throw new IllegalArgumentException("La personne ajouté n'existe pas");
			}
			
			DBUser friend = alreadyExist.get();
			DBUser user = userId.get();
			
				if (!user.getFriends().contains(friend)) {
					user.getFriends().add(friend);
					userRepository.save(user);
				} else throw new IllegalArgumentException("Vous avez déjà ajouté cet ami.");
				return Optional.of(user);
			
		} else throw new IllegalArgumentException("Impossible de s'ajouter soi-même en ami.");
		
	}

}
