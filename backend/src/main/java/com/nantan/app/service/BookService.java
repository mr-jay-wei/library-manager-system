package com.nantan.app.service;

import com.nantan.app.Book;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Asynchronous service layer interface for book-related business logic.
 * All methods now return a CompletableFuture to represent a future result.
 */
public interface BookService {

    /**
     * Asynchronously retrieves all books from the specified data source.
     *
     * @param dataSource a string indicating the data source ("mysql" or "mongo").
     * @return a CompletableFuture containing a list of all books.
     */
    CompletableFuture<List<Book>> getAllBooks(String dataSource);

    /**
     * Asynchronously adds a new book to the specified data source.
     *
     * @param book       the book entity to save.
     * @param dataSource a string indicating the data source.
     * @return a CompletableFuture containing the saved book entity.
     */
    CompletableFuture<Book> addBook(Book book, String dataSource);

    /**
     * Asynchronously deletes a book by its ID from the specified data source.
     *
     * @param bookId     the ID of the book to delete.
     * @param dataSource a string indicating the data source.
     * @return a CompletableFuture containing a boolean indicating if the book was deleted.
     */
    CompletableFuture<Boolean> deleteBookById(int bookId, String dataSource);
}