package com.paymybuddy.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.*;

@Entity
@Table(name = "db_user")
public class DBUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="username")
	private String username;

	@Column(unique = true, name = "email")
	private String email;

	@Column(name="password")
	private String password;
	
	@Column(name="balance")
	private BigDecimal balance = BigDecimal.ZERO;

	@OneToMany(mappedBy = "sender")
	@JsonManagedReference("sentTransactions")
	private List<Transaction> sentTransactions = new ArrayList<>();

	@OneToMany(mappedBy = "receiver")
	@JsonManagedReference("receivedTransactions")
	private List<Transaction> receivedTransactions = new ArrayList<>();

	@OneToMany
	@JoinTable(name = "user_friend", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "friend_id"))
	private List<DBUser> friends = new ArrayList<>();

	public DBUser() {

	}

	public DBUser(int id, String username, String email, String password, BigDecimal balance) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.balance = balance;

	}

	// Getters and Setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public List<Transaction> getSentTransactions() {
		return sentTransactions;
	}

	public void setSentTransactions(List<Transaction> sentTransactions) {
		this.sentTransactions = sentTransactions;
	}

	public List<Transaction> getReceivedTransactions() {
		return receivedTransactions;
	}

	public void setReceivedTransactions(List<Transaction> receivedTransactions) {
		this.receivedTransactions = receivedTransactions;
	}

	public List<DBUser> getFriends() {
		return friends;
	}

	public void setFriends(List<DBUser> friends) {
		this.friends = friends;
	}

}
