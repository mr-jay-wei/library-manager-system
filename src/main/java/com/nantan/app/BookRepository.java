package com.nantan.app;

import java.util.List;

/**
 * An interface that defines the standard operations to be performed on a collection of books.
 * This acts as a contract for any data persistence implementation (e.g., MongoDB, JPA/SQL).
 * 这是数据访问层的抽象“契约”。
 */
public interface BookRepository {

    /**
     * Retrieves all books from the repository.
     * @return A list of all books.
     */
    List<Book> findAll();

    /**
     * Saves a new book or updates an existing one.
     * @param book The book object to save.
     */
    void save(Book book);

    /**
     * Deletes a book from the repository by its ID.
     * @param bookId The ID of the book to delete.
     * @return true if a book was deleted, false otherwise.
     */
    boolean deleteById(int bookId);

    /**
     * A method to perform any necessary cleanup, like closing connections.
     * This allows the main application to handle shutdown gracefully regardless of implementation.
     */
    void close();
}