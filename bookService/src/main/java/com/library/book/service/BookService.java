package com.library.book.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.book.DTO.BookRequest;
import com.library.book.DTO.BookResponse;
import com.library.book.exception.BookNotFoundException;
import com.library.book.model.Book;
import com.library.book.repo.BookRepo;

@Service
public class BookService {

	private static final Logger log = LoggerFactory.getLogger(BookService.class);

	@Autowired
	private BookRepo bookrepo;

	public BookResponse addBook(BookRequest req) {
		log.info("Adding new book with isbn: {}", req.getIsbn());
		Book book = new Book();
		book.setIsbn(req.getIsbn());
		book.setTitle(req.getTitle());
		book.setAuthor(req.getAuthor());
		book.setCategory(req.getCategory());
		book.setAvailableCopies(req.getTotalCopies());
		book.setTotalCopies(req.getTotalCopies());
		bookrepo.save(book);
		log.info("Book added successfully: {}", req.getTitle());
		return mapToResponse(book);
	}

	public BookResponse updateBook(String id, BookRequest req) {
		log.info("Updating book with isbn: {}", id);
		Book book = bookrepo.findById(id).orElseThrow(() -> {
			log.warn("Book not found for update with isbn: {}", id);
			return new RuntimeException("Book not found");
		});
//		int diff = req.getTotalCopies() - book.getTotalCopies();
		book.setTitle(req.getTitle());
		book.setAuthor(req.getAuthor());
		book.setCategory(req.getCategory());
		book.setTotalCopies(req.getTotalCopies() + book.getTotalCopies());
		book.setAvailableCopies(req.getAvailableCopies() + book.getAvailableCopies());
		bookrepo.save(book);
		log.info("Book updated successfully: {}", id);
		return mapToResponse(book);
	}

	public String deleteBook(String id) {
		log.info("Deleting book with isbn: {}", id);
		bookrepo.deleteById(id);
		log.info("Book deleted successfully: {}", id);
		return "Book deleted successfully!!";
	}

	public List<BookResponse> getAllBooks() {
		log.info("Fetching all books");
		List<Book> books = bookrepo.findAll();
		log.info("Total books found: {}", books.size());
		return books.stream().map(this::mapToResponse).toList();
	}

	public List<BookResponse> getBycategory(String category) throws BookNotFoundException {
		log.info("Fetching books by category: {}", category);
		List<Book> books = bookrepo.findByCategory(category);
		if (books.isEmpty()) {
			log.warn("No books found for category: {}", category);
			throw new BookNotFoundException("No book found with category: " + category);
		}
		return books.stream().map(this::mapToResponse).toList();
	}

	public List<BookResponse> getByTitle(String title) throws BookNotFoundException {
		log.info("Fetching books by title: {}", title);
		List<Book> books = bookrepo.findByTitle(title);
		if (books.isEmpty()) {
			log.warn("No books found for title: {}", title);
			throw new BookNotFoundException("No book found with title: " + title);
		}
		return books.stream().map(this::mapToResponse).toList();
	}

	public List<BookResponse> getByAuthor(String author) throws BookNotFoundException {
		log.info("Fetching books by author: {}", author);
		List<Book> books = bookrepo.findByAuthor(author);
		if (books.isEmpty()) {
			log.warn("No books found for author: {}", author);
			throw new BookNotFoundException("No book found with author: " + author);
		}
		return books.stream().map(this::mapToResponse).toList();
	}

	public void decreaseCopies(String id) throws BookNotFoundException {
		log.info("Decreasing available copies for book: {}", id);
		Book book = bookrepo.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found"));
		if (book.getAvailableCopies() <= 0) {
			log.warn("Cannot decrease copies - no available copies for book: {}", id);
			throw new RuntimeException("No available copies to decrease");
		}
		book.setAvailableCopies(book.getAvailableCopies() - 1);
		bookrepo.save(book);
		log.info("Available copies decreased for book: {} -> {}", id, book.getAvailableCopies());
	}

	public void increaseCopies(String id) throws BookNotFoundException {
		log.info("Increasing available copies for book: {}", id);
		Book book = bookrepo.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found"));
		if (book.getAvailableCopies() >= book.getTotalCopies()) {
			log.warn("Cannot increase copies - already at max for book: {}", id);
			throw new RuntimeException("No sufficient copies");
		}
		book.setAvailableCopies(book.getAvailableCopies() + 1);
		bookrepo.save(book);
		log.info("Available copies increased for book: {} -> {}", id, book.getAvailableCopies());
	}

	private BookResponse mapToResponse(Book book) {
		return new BookResponse(book.getIsbn(), book.getTitle(), book.getAuthor(), book.getCategory(),
				book.getTotalCopies(), book.getAvailableCopies());
	}
}
