package com.paymybuddy.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "db_transaction")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@ManyToOne
	@JoinColumn(name = "sender_id")
	@JsonBackReference("sentTransactions")
	private DBUser sender;

	@ManyToOne
	@JoinColumn(name = "receiver_id")
	@JsonBackReference("receivedTransactions")
	private DBUser receiver;

	@Column(name="description")
	private String description;
	
	@Column(name="amount")
	private BigDecimal amount;
	
	@Column(name="fee")
	private BigDecimal fee;

	
	private LocalDateTime timestamp;
	
	public Transaction() {
		
	}

	public Transaction(int id, DBUser sender, DBUser receiver, String description, BigDecimal amount, BigDecimal fee,
			LocalDateTime timestamp) {
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		this.description = description;
		this.amount = amount;
		this.fee = fee;
		this.timestamp = timestamp;
	}

	// Getters and Setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public DBUser getSender() {
		return sender;
	}

	public void setSender(DBUser sender) {
		this.sender = sender;
	}

	public DBUser getReceiver() {
		return receiver;
	}

	public void setReceiver(DBUser receiver) {
		this.receiver = receiver;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	

}
