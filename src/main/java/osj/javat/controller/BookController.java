package osj.javat.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import osj.javat.Entity.Book;
import osj.javat.repository.BookRepository;
import osj.javat.service.TokenService;

@Slf4j
@RestController
@RequestMapping("/api/books")
public class BookController {
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping("/add")
	public Book addBook(@RequestHeader("Authorization") String accessToken, @RequestBody Book book) {
		Long userId = tokenService.extractUserId(accessToken);
		if (userId == null) {
			throw new RuntimeException("userId not found.");
		}
		log.info("Adding book for userId: {}", userId);
		book.setUserId(userId);
		
		log.info("book : ", book);
		book.setAddedDate(LocalDate.now());
		log.info("book : ", book);
		return bookRepository.save(book);
	}
	
	@GetMapping("/get")
	public List<Book> getBooks(@RequestHeader("Authorization") String accessToken) {
		Long userId = tokenService.extractUserId(accessToken);
		if (userId == null) {
			throw new RuntimeException("userId not found.");
		}
		log.info("Fetching books for userId: {}", userId);
		
		return bookRepository.findByUserId(userId);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteBook(@RequestHeader("Authorization") String accessToken, @RequestBody Book book) {
		Long userId = tokenService.extractUserId(accessToken);
		if (userId == null) {
			return new ResponseEntity<>("User ID not found.", HttpStatus.UNAUTHORIZED);
		}
		
		log.info("Attempting to delete book with ID {} for userId: {}", book.getId(), userId);
		
		try {
			Book existingBook = bookRepository.findById(book.getId())
				.orElseThrow(() -> new RuntimeException("Book not found."));
			
			if (!existingBook.getUserId().equals(userId)) {
				log.warn("Unauthorized deletion attempt for book ID {} by userId {}", book.getId(), userId);
	            return new ResponseEntity<>("Unauthorized deletion attempt.", HttpStatus.FORBIDDEN);
			}
			
			bookRepository.delete(existingBook);
			
			return new ResponseEntity<>("Book deleted successfully.", HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error occurred while deleting book ID {}: {}", book.getId(), e.getMessage());
			return new ResponseEntity<>("Internal server error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
