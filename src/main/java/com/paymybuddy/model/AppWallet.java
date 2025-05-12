package com.paymybuddy.model;

import java.math.BigDecimal;
import java.util.List;

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
    private int id;

    private BigDecimal totalFeesCollected;

    @OneToMany(mappedBy = "wallet")
    private List<Transaction> transactions;

    public AppWallet (int id, BigDecimal totalFeesCollected) {
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

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

    
}
