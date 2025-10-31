package com.nantan.app.service.impl;

import com.nantan.app.Book;
import com.nantan.app.BookRepository;
import com.nantan.app.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    // 1. Get a logger instance for this class
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

    @Override
    public List<Book> getAllBooks(String dataSource) {
        logger.info("Fetching all books from data source: {}", dataSource);
        List<Book> books = selectRepository(dataSource).findAll();
        logger.info("Found {} books from data source: {}", books.size(), dataSource);
        return books;
    }

    @Override
    @Transactional
    public Book addBook(Book book, String dataSource) {
        logger.info("Adding new book to data source: {}. Book details: {}", dataSource, book);
        Book savedBook = selectRepository(dataSource).save(book);
        logger.info("Successfully added book with new ID: {}", savedBook.getId());
        return savedBook;
    }

    @Override
    @Transactional
    public boolean deleteBookById(int bookId, String dataSource) {
        logger.info("Attempting to delete book with ID: {} from data source: {}", bookId, dataSource);
        boolean deleted = selectRepository(dataSource).deleteById(bookId);
        if (deleted) {
            logger.info("Successfully deleted book with ID: {}", bookId);
        } else {
            logger.warn("Failed to delete book with ID: {}. It might not exist.", bookId);
        }
        return deleted;
    }

    private BookRepository selectRepository(String dataSource) {
        if ("mongo".equalsIgnoreCase(dataSource)) {
            return mongoRepository;
        }
        return jpaRepository;
    }
}