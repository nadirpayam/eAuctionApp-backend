package com.project.eAuctionApp.responses;

import com.project.eAuctionApp.entities.User;

public class UserResponse {
	
	Long id;
	String userName;

	public UserResponse(User entity) {
		this.id = entity.getId();
		this.userName = entity.getUserName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	} 
	
	
}