package com.library.member.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "members")
public class Member {

	@Id
	@Column(nullable = false)
	private String id;
	@Column(nullable = false)
	private String userId;
	private LocalDate membershipStart;
	@Column(nullable = false)
	private LocalDate membershipEnd;
	@Column(nullable = false)
	private String status;
	
	public Member()
	{
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public LocalDate getMembershipStart() {
		return membershipStart;
	}

	public void setMembershipStart(LocalDate membershipStart) {
		this.membershipStart = membershipStart;
	}

	public LocalDate getMembershipEnd() {
		return membershipEnd;
	}

	public void setMembershipEnd(LocalDate membershipEnd) {
		this.membershipEnd = membershipEnd;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
