package com.nantan.app.mongo;

import com.nantan.app.Book;
import com.nantan.app.BookRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Book entity.
 */
@Repository("mongoBookRepository") // 指定一个明确的 bean 名称
public interface BookMongoRepository extends MongoRepository<Book, Integer>, BookRepository {

    // Spring Data MongoDB will automatically implement the methods from MongoRepository.
    // We just need to ensure it conforms to our BookRepository interface.

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
        // Connection management is handled by Spring Boot.
    }
}