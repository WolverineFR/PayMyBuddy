package com.paymybuddy.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.paymybuddy.model.DBUser;

public class UserDTO {
	private int id;
	private String username;
	private String email;
	private BigDecimal balance;
	private List<FriendDTO> friends;

	public UserDTO(DBUser dbUser) {
		this.id = dbUser.getId();
		this.username = dbUser.getUsername();
		this.email = dbUser.getEmail();
		this.balance = dbUser.getBalance();
		this.friends = dbUser.getFriends().stream().map(FriendDTO::new).collect(Collectors.toList());
	}

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

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public List<FriendDTO> getFriends() {
		return friends;
	}

	public void setFriends(List<FriendDTO> friends) {
		this.friends = friends;
	}

}
