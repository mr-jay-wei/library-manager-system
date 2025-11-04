package com.nantan.app;

import java.util.List;

/**
 * An interface that defines the standard operations to be performed on a collection of books.
 * This acts as a contract for any data persistence implementation.
 * The method signatures are now aligned with Spring Data's conventions.
 */
public interface BookRepository {

    /**
     * Retrieves all books from the repository.
     * @return A list of all books.
     */
    List<Book> findAll();

    /**
     * Saves a new book or updates an existing one.
     *
     * @param book The book object to save.
     * @return The saved book, which may include updates from the persistence layer (e.g., generated ID).
     */
    // 关键修改: 返回类型从 void 改为 Book，以匹配 Spring Data CrudRepository 的规范
    Book save(Book book);

    /**
     * Deletes a book from the repository by its ID.
     * @param bookId The ID of the book to delete.
     * @return true if a book was deleted, false otherwise.
     */
    boolean deleteById(int bookId);

    /**
     * A method to perform any necessary cleanup, like closing connections.
     * In Spring Boot, this is managed automatically, so the implementation will be empty.
     */
    void close();
}