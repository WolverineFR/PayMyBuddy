package com.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.model.AppWallet;

@Repository
public interface AppWalletRepository extends JpaRepository<AppWallet, Integer>{

}
