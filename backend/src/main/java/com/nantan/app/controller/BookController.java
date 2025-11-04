package com.nantan.app.controller;

import com.nantan.app.Book;
import com.nantan.app.dto.ApiResponse;
import com.nantan.app.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Tag(name = "Book Management", description = "APIs for managing books in the library")
@CrossOrigin
@RestController
@RequestMapping("/api/books")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Get all books asynchronously", description = "Asynchronously retrieves a list of all books.")
    @GetMapping
    public CompletableFuture<ApiResponse<List<Book>>> getAllBooks(
            @Parameter(description = "The data source to use ('mysql' or 'mongo')", example = "mysql")
            @RequestParam(name = "dataSource", defaultValue = "mysql") String dataSource) {

        logger.info("Received getAllBooks request on thread: {}", Thread.currentThread().getName());

        return bookService.getAllBooks(dataSource)
                .thenApply(books -> {
                    logger.info("Completing getAllBooks request on thread: {}", Thread.currentThread().getName());
                    return ApiResponse.success(books);
                });
    }

    @Operation(summary = "Add a new book asynchronously", description = "Asynchronously creates a new book.")
    @PostMapping
    public CompletableFuture<ResponseEntity<ApiResponse<Book>>> addBook(
            @RequestBody Book book,
            @Parameter(description = "The data source to use ('mysql' or 'mongo')", example = "mysql")
            @RequestParam(name = "dataSource", defaultValue = "mysql") String dataSource) {

        logger.info("Received addBook request on thread: {}", Thread.currentThread().getName());

        return bookService.addBook(book, dataSource)
                .thenApply(savedBook -> {
                    logger.info("Completing addBook request on thread: {}", Thread.currentThread().getName());
                    ApiResponse<Book> responseBody = ApiResponse.success(savedBook);
                    return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
                });
    }

    @Operation(summary = "Delete a book by ID asynchronously", description = "Asynchronously deletes a book by its ID.")
    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<ApiResponse<Void>>> deleteBook(
            @Parameter(description = "The unique ID of the book to delete", required = true)
            @PathVariable("id") int bookId,
            @Parameter(description = "The data source to use ('mysql' or 'mongo')", example = "mysql")
            @RequestParam(name = "dataSource", defaultValue = "mysql") String dataSource) {

        logger.info("Received deleteBook request on thread: {}", Thread.currentThread().getName());

        return bookService.deleteBookById(bookId, dataSource)
                .thenApply(deleted -> {
                    logger.info("Completing deleteBook request on thread: {}", Thread.currentThread().getName());
                    if (deleted) {
                        return ResponseEntity.ok(ApiResponse.success());
                    } else {
                        ApiResponse<Void> failureResponse = ApiResponse.failure(4040, "Book not found with id: " + bookId);
                        return new ResponseEntity<>(failureResponse, HttpStatus.NOT_FOUND);
                    }
                });
    }
}