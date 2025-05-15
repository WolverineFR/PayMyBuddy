package com.paymybuddy.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.model.AppWallet;
import com.paymybuddy.repository.AppWalletRepository;

@Service
public class AppWalletService {

	@Autowired
	private AppWalletRepository appWalletRepository;
	
	
	public BigDecimal getBalance() {
		return appWalletRepository.findById(1).orElseThrow().getTotalFeesCollected();
	}
	
	public void addFee (BigDecimal fee) {
		AppWallet wallet = appWalletRepository.findById(1).orElseThrow();
		wallet.setTotalFeesCollected(wallet.getTotalFeesCollected().add(fee));
		appWalletRepository.save(wallet);
	}
}
