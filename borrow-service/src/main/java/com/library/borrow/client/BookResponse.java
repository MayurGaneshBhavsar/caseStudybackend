package com.library.borrow.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BookResponse {

	@JsonProperty("isbn")
	private String bookIsbn;
	private int availableCopies;
	public String getBookIsbn() {
		return bookIsbn;
	}
	public int getAvailableCopies() {
		return availableCopies;
	}

}
