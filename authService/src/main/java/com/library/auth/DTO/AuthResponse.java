package com.library.auth.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {

	private String message;
	private String userName;
	private String token;
	private String role;
	private String userId;
	
	public AuthResponse() {
		
	}

	public AuthResponse(String message, String userName) {
		this.message = message;
		this.userName = userName;
	}

	public AuthResponse(String message, String userName, String token, String role) {
		this.message = message;
		this.userName = userName;
		this.token = token;
		this.role = role;
	}

	public AuthResponse(String message, String userName, String token, String role, String userId) {
		this.message = message;
		this.userName = userName;
		this.token = token;
		this.role = role;
		this.userId = userId;
	}

	public String getMessage() {
		return message;
	}

	public String getUserName() {
		return userName;
	}

	public String getToken() {
		return token;
	}

	public String getRole() {
		return role;
	}

	public String getUserId() {
		return userId;
	}
}
