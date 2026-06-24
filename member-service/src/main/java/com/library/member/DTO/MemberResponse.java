package com.library.member.DTO;

import java.time.LocalDate;


public class MemberResponse {

	private String id;
	private String userId;
	//private LocalDate membershipStart;
	private LocalDate membershipEnd;
	private String status;
	
	public MemberResponse(String id, String userId, LocalDate membershipEnd, String status) {
		this.id = id;
		this.userId = userId;
		this.membershipEnd = membershipEnd;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public String getUserId() {
		return userId;
	}

	public LocalDate getMembershipEnd() {
		return membershipEnd;
	}

	public String getStatus() {
		return status;
	}
	
	
	
}
