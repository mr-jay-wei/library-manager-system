package com.nantan.app.service;
import com.nantan.app.Book;
import java.util.List;
/**
 Service layer interface for book-related business logic.
 This abstracts the business operations from the controller.
 */
public interface BookService {
    /**
     Retrieves all books from the specified data source.
     @param dataSource a string indicating the data source ("mysql" or "mongo").
     @return a list of all books.
     */
    List<Book> getAllBooks(String dataSource);
    /**
     Adds a new book to the specified data source.
     @param book the book entity to save.
     @param dataSource a string indicating the data source.
     @return the saved book entity.
     */
    Book addBook(Book book, String dataSource);
    /**
     Deletes a book by its ID from the specified data source.
     @param bookId the ID of the book to delete.
     @param dataSource a string indicating the data source.
     @return true if the book was deleted successfully, false otherwise.
     */
    boolean deleteBookById(int bookId, String dataSource);
}