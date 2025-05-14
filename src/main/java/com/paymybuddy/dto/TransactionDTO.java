package com.paymybuddy.dto;

import java.math.BigDecimal;

public class TransactionDTO {
	private int senderId;
	private int receiverId;
	private String description;
	private BigDecimal amount;
	private BigDecimal fee;

	public TransactionDTO(int senderId, int receiverId, String description, BigDecimal amount, BigDecimal fee) {
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.description = description;
		this.amount = amount;
		this.fee = fee;
	}

	// Getters and Setters

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public int getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
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

}
