package com.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.model.DBUser;

@Repository
public interface UserRepository extends JpaRepository<DBUser, Integer> {
	public DBUser findByUsername (String username);
	public DBUser findByEmail (String email);
}
