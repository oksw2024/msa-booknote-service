package osj.javat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import osj.javat.Entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{
	List<Book> findByUserId(Long userId);
}
