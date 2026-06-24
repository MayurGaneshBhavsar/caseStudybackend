package com.library.borrow.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;


@FeignClient(name = "bookService")
public interface BookClient {
	
	@GetMapping("/books/title/{title}")
    List<BookResponse> getBookByTitle(@PathVariable String title);
	
	@PutMapping("/books/decrease/{id}")
	void decreaseCopies(@PathVariable String id);
	
	@PutMapping("/books/increase/{id}")
	void increaseCopies(@PathVariable String id);
	

}
