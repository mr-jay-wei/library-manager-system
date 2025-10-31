package com.nantan.app.jpa;
import com.nantan.app.Book;
import com.nantan.app.BookRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 Spring Data JPA repository for the Book entity.
 By extending JpaRepository, we get a lot of CRUD functionality for free.
 JpaRepository<Book, Integer>
 Book: The domain type the repository manages.
 Integer: The type of the id of the domain type.
 */
@Repository("jpaBookRepository") // 指定一个明确的 bean 名称，方便后续注入
public interface BookJpaRepository extends JpaRepository<Book, Integer>, BookRepository {
    // Spring Data JPA will automatically implement:
    // - save(Book entity)
    // - findById(Integer id)
    // - findAll()
    // - deleteById(Integer id)
    // - and many more...
    // We need to provide implementations for our custom BookRepository methods
    // that don't map directly to JpaRepository's conventions.
    @Override
    default boolean deleteById(int bookId) {
        if (existsById(bookId)) {
            deleteById((Integer) bookId);
            return true;
        }
        return false;
    }
    @Override
    default void close() {
    // With Spring Boot, connection management is handled automatically.
    // This method is now empty.
    }
}