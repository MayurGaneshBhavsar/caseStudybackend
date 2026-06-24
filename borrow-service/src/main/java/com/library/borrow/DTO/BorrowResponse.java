package com.library.borrow.DTO;

import java.time.LocalDate;

public class BorrowResponse {

	private Long id;
	private String bookIsbn;
	//private String title;
	private LocalDate issueDate;
	private LocalDate dueDate;
	private String status;
	public BorrowResponse(Long id, String bookIsbn,LocalDate issueDate, LocalDate dueDate, String status) {
		this.id = id;
		this.bookIsbn = bookIsbn;
		this.issueDate=issueDate;
		this.dueDate = dueDate;
		this.status = status;
	}
	public LocalDate getIssueDate() {
		return issueDate;
	}
	public Long getId() {
		return id;
	}
	public String getBookIsbn() {
		return bookIsbn;
	}
	public LocalDate getDueDate() {
		return dueDate;
	}
	public String getStatus() {
		return status;
	}
	
	
}
