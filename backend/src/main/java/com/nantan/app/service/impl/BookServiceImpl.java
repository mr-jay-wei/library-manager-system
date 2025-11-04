package com.nantan.app.service.impl;

import com.nantan.app.Book;
import com.nantan.app.BookRepository;
import com.nantan.app.config.AsyncConfig;
import com.nantan.app.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Asynchronous implementation of the BookService interface.
 * Each method is annotated with @Async to be executed in a background thread pool,
 * and returns a CompletableFuture to handle the asynchronous result.
 */
@Service
public class BookServiceImpl implements BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository jpaRepository;
    private final BookRepository mongoRepository;

    @Autowired
    public BookServiceImpl(
            @Qualifier("jpaBookRepository") BookRepository jpaRepository,
            @Qualifier("mongoBookRepository") BookRepository mongoRepository) {
        this.jpaRepository = jpaRepository;
        this.mongoRepository = mongoRepository;
    }

    /**
     * Asynchronously retrieves all books.
     * The database query is executed in a background thread from our custom task executor.
     *
     * @param dataSource The data source to use.
     * @return A CompletableFuture that will eventually hold the list of books.
     */
    @Override
    @Async(AsyncConfig.TASK_EXECUTOR_NAME)
    public CompletableFuture<List<Book>> getAllBooks(String dataSource) {
        logger.info("Executing getAllBooks on thread: {}", Thread.currentThread().getName());
        List<Book> books = selectRepository(dataSource).findAll();
        logger.info("Found {} books from data source: {}", books.size(), dataSource);
        return CompletableFuture.completedFuture(books);
    }

    /**
     * Asynchronously adds a new book.
     * The save operation is executed in a background thread and is transactional.
     *
     * @param book       The book to add.
     * @param dataSource The data source to use.
     * @return A CompletableFuture that will eventually hold the saved book.
     */
    @Override
    @Async(AsyncConfig.TASK_EXECUTOR_NAME)
    @Transactional
    public CompletableFuture<Book> addBook(Book book, String dataSource) {
        logger.info("Executing addBook on thread: {}", Thread.currentThread().getName());
        Book savedBook = selectRepository(dataSource).save(book);
        logger.info("Successfully added book with new ID: {}", savedBook.getId());
        return CompletableFuture.completedFuture(savedBook);
    }

    /**
     * Asynchronously deletes a book by its ID.
     * The delete operation is executed in a background thread and is transactional.
     *
     * @param bookId     The ID of the book to delete.
     * @param dataSource The data source to use.
     * @return A CompletableFuture that will eventually hold a boolean indicating success.
     */
    @Override
    @Async(AsyncConfig.TASK_EXECUTOR_NAME)
    @Transactional
    public CompletableFuture<Boolean> deleteBookById(int bookId, String dataSource) {
        logger.info("Executing deleteBookById on thread: {}", Thread.currentThread().getName());
        boolean deleted = selectRepository(dataSource).deleteById(bookId);
        if (deleted) {
            logger.info("Successfully deleted book with ID: {}", bookId);
        } else {
            logger.warn("Failed to delete book with ID: {}. It might not exist.", bookId);
        }
        return CompletableFuture.completedFuture(deleted);
    }

    /**
     * Private helper method to select the correct repository based on the dataSource string.
     * This method is called from within the async methods, so it executes on the background thread.
     *
     * @param dataSource The string identifier for the data source ("mysql" or "mongo").
     * @return The corresponding BookRepository bean.
     */
    private BookRepository selectRepository(String dataSource) {
        if ("mongo".equalsIgnoreCase(dataSource)) {
            return mongoRepository;
        }
        // Default to MySQL/JPA if the source is "mysql" or anything else.
        return jpaRepository;
    }
}