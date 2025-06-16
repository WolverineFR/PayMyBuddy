package com.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.model.DBUser;

/**
 * Repository pour la gestion des entités DBUser. Étend JpaRepository pour
 * bénéficier des méthodes CRUD.
 */
@Repository
public interface UserRepository extends JpaRepository<DBUser, Integer> {

	/**
	 * Recherche un utilisateur par son email.
	 * 
	 * @param email l'adresse email à rechercher
	 * @return l'utilisateur correspondant, ou null si aucun trouvé
	 */
	public DBUser findByEmail(String email);
}
