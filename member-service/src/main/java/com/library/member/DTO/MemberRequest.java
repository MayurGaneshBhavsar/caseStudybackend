package com.library.member.DTO;


import jakarta.validation.constraints.NotBlank;



public class MemberRequest {

	@NotBlank(message = "User id is required")
	private String userId;
//	@NotBlank(message = "Membership Start date is required")
//	private LocalDate membershipStart;
//	@NotBlank(message = "Membership End date is required")
//	private LocalDate membershipEnd;
//	@NotBlank(message = "Status is required")
//	private String status;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
}
