package com.paymybuddy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.model.DBUser;
import com.paymybuddy.repository.UserRepository;

/**
 * Implémentation du service de gestion des utilisateurs.
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	/**
	 * Récupère un utilisateur par son email.
	 * 
	 * @param email l'adresse email de l'utilisateur
	 * @return l'utilisateur correspondant ou null s'il n'existe pas
	 */
	@Override
	public DBUser getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	/**
	 * Récupère tous les utilisateurs.
	 * 
	 * @return une liste d'utilisateurs
	 */
	@Override
	public List<DBUser> getAllUsers() {
		return userRepository.findAll();
	}

	/**
	 * Sauvegarde un utilisateur dans la base de données.
	 * 
	 * @param user l'utilisateur à sauvegarder
	 * @return l'utilisateur sauvegardé
	 */
	@Override
	public DBUser saveUser(DBUser user) {
		return userRepository.save(user);
	}
}
