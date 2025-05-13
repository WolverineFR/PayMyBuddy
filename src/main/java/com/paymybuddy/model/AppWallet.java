package com.paymybuddy.model;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_wallet")
public class AppWallet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "fee_amount")
	private BigDecimal totalFeesCollected;

	public AppWallet() {

	}

	public AppWallet(int id, BigDecimal totalFeesCollected) {
		this.id = id;
		this.totalFeesCollected = totalFeesCollected;
	}

	// Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BigDecimal getTotalFeesCollected() {
		return totalFeesCollected;
	}

	public void setTotalFeesCollected(BigDecimal totalFeesCollected) {
		this.totalFeesCollected = totalFeesCollected;
	}

}
