package com.library.book.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.book.DTO.BookRequest;
import com.library.book.DTO.BookResponse;
import com.library.book.exception.BookNotFoundException;
import com.library.book.service.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/books")
@Tag(name = "Books", description = "Book catalog management")
public class BookController {

    @Autowired
    private BookService bookService;

    @Operation(description = "Returns list of all books in the library")
    @GetMapping
    public List<BookResponse> getAllBooks() {
        return bookService.getAllBooks();
    }

    @Operation(description = "Returns books matching the given title")
    @GetMapping("/title/{title}")
    public List<BookResponse> getByTitle(@PathVariable String title) throws BookNotFoundException {
        return bookService.getByTitle(title);
    }

    @Operation(description = "Returns books matching the given category")
    @GetMapping("/category/{category}")
    public List<BookResponse> getByCategory(@PathVariable String category) throws BookNotFoundException {
        return bookService.getBycategory(category);
    }

    @Operation(description = "Returns books matching the given author")
    @GetMapping("/author/{author}")
    public List<BookResponse> getByAuthor(@PathVariable String author) throws BookNotFoundException {
        return bookService.getByAuthor(author);
    }

    @Operation(description = "Admin only - adds a new book to the catalog")
    @PostMapping
    public String addBook(@Valid @RequestBody BookRequest req) {
        bookService.addBook(req);
        return "Book Added Successfully!!";
    }

    @Operation(description = "Admin only - updates book details by ISBN")
    @PutMapping("/{id}")
    public String updateBook(@PathVariable String id, @RequestBody BookRequest req) {
        bookService.updateBook(id, req);
        return "Book updated successfully!!";
    }

    @Operation(description = "Admin only - deletes a book by ISBN")
    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable String id) {
        return bookService.deleteBook(id);
    }

    @PutMapping("/decrease/{id}")
    public void decreaseCopies(@PathVariable String id) throws BookNotFoundException {
        bookService.decreaseCopies(id);
    }

    @PutMapping("/increase/{id}")
    public void increaseCopies(@PathVariable String id) throws BookNotFoundException {
        bookService.increaseCopies(id);
    }
}
