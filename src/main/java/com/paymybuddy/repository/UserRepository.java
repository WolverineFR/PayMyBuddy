package com.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paymybuddy.model.DBUser;

public interface UserRepository extends JpaRepository<DBUser, Integer> {
	public DBUser findByUsername (String username);
}
