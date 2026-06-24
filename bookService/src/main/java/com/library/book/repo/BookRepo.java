package com.library.book.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.book.model.Book;
import java.util.List;


public interface BookRepo extends JpaRepository<Book, String>{
	
	List<Book> findByTitle(String title);
	List<Book> findByAuthor(String author);
	List<Book> findByCategory(String category);

}
