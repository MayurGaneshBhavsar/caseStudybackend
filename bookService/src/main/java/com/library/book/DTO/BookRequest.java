package com.library.book.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class BookRequest {

	@NotBlank(message = "ISBN is required")
	private String isbn;
	@NotBlank(message = "Author is required")
	private String title;
	@NotBlank(message = "Author is required")
	private String author;
	@NotBlank(message = "Category is required")
	private String category;
	@Min(value=1, message = "Total copies must be atleast 1")
	private Integer totalCopies;
	
	private Integer availableCopies;
	public Integer getAvailableCopies() {
		return availableCopies;
	}

	public BookRequest() {
		
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
	
	
	
}
