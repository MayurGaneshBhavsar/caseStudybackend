package com.library.auth.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users_table")
public class User {
	
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	@Column(nullable = false)
	private String firstName;
	@Column(nullable = true)
	private String lastName;
	@Column(nullable = false)
	private String mobNo;
	@Column(nullable = false)
	private String email;
	@Column(nullable = true)
	private String addr;
	
	@Column(unique = true,nullable=false)
	private String userName;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String role;
	
	public User()
	{
		
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobNo() {
		return mobNo;
	}

	public void setMobNo(String mobNo) {
		this.mobNo = mobNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

//	public String getaddr() {
//		return addr;
//	}
//
//	public void setaddr(String addr) {
//		this.addr = addr;
//	}
	

	public String getUserName() {
		return userName;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		// TODO Auto-generated method stub
		this.id=id;
		
	}
	
	

	

	
	
	
	

}
