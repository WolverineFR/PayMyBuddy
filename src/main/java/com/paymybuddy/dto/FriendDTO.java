package com.paymybuddy.dto;

import com.paymybuddy.model.DBUser;

public class FriendDTO {
	private int id;
	private String username;
	private String email;

	public FriendDTO(DBUser user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.email = user.getEmail();
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

}
