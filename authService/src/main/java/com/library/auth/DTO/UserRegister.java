package com.library.auth.DTO;


import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public class UserRegister {
	
	private String id;
	@NotBlank(message = "First name is required")
	private String firstName;
	@NotBlank(message = "Last name is required")
	private String lastName;
	@NotBlank(message = "Mobile Number is required")
	@Pattern(regexp = "\\d{10}",message = "Mobile Number must be 10 digits")
	private String mobNo;
	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email!!")
	private String email;
	@NotBlank(message = "Address is required")
	private String addr;
	//private String userName;
	@NotBlank(message = "Password is required")
	private String password;
	
	public UserRegister() {
		
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getMobNo() {
		return mobNo;
	}

	public String getEmail() {
		return email;
	}
//
//	public String getaddr() {
//		return addr;
//	}
	

	public String getPassword() {
		return password;
	}

	public String getAddr() {
		return addr;
	}

//	public String getRole() {
//		return role;
//	}

	public String getId() {
		return id;
	}
	
	
	
	
	
	

	
	

}
