package com.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.model.Transaction;

/**
 * Repository Spring Data JPA pour la gestion des entités Transaction. Fournit
 * des méthodes CRUD de base, héritées de JpaRepository.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

}
