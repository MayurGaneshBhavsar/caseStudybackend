package com.library.book.DTO;

public class BookResponse {

	private String isbn;
	private String title;
	private String author;
	private String category;
	private Integer totalCopies;
	private Integer availableCopies;
	
	public BookResponse(String isbn, String title, String author, String category, Integer totalCopies,
			Integer availableCopies) {
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.category = category;
		this.totalCopies = totalCopies;
		this.availableCopies = availableCopies;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public String getCategory() {
		return category;
	}

	public Integer getTotalCopies() {
		return totalCopies;
	}

	public Integer getAvailableCopies() {
		return availableCopies;
	}
	
	
}
