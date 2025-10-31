package com.nantan.app.controller;

import com.nantan.app.Book;
import com.nantan.app.dto.ApiResponse;
import com.nantan.app.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Book Management", description = "APIs for managing books in the library") // 1. Add a tag for grouping APIs
@CrossOrigin
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Get all books", description = "Retrieves a list of all books from the specified data source.") // 2. Describe the operation
    @GetMapping
    public ApiResponse<List<Book>> getAllBooks(
            @Parameter(description = "The data source to use ('mysql' or 'mongo')", example = "mysql") // 3. Describe the parameter
            @RequestParam(name = "dataSource", defaultValue = "mysql") String dataSource) {
        List<Book> books = bookService.getAllBooks(dataSource);
        return ApiResponse.success(books);
    }

    @Operation(summary = "Add a new book", description = "Creates a new book in the specified data source.")
    @PostMapping
    public ResponseEntity<ApiResponse<Book>> addBook(
            @RequestBody Book book,
            @Parameter(description = "The data source to use ('mysql' or 'mongo')", example = "mysql")
            @RequestParam(name = "dataSource", defaultValue = "mysql") String dataSource) {
        Book savedBook = bookService.addBook(book, dataSource);
        ApiResponse<Book> responseBody = ApiResponse.success(savedBook);
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a book by ID", description = "Deletes a book from the specified data source using its unique ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(
            @Parameter(description = "The unique ID of the book to delete", required = true)
            @PathVariable("id") int bookId,
            @Parameter(description = "The data source to use ('mysql' or 'mongo')", example = "mysql")
            @RequestParam(name = "dataSource", defaultValue = "mysql") String dataSource) {
        boolean deleted = bookService.deleteBookById(bookId, dataSource);
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.success());
        } else {
            ApiResponse<Void> failureResponse = ApiResponse.failure(4040, "Book not found with id: " + bookId);
            return new ResponseEntity<>(failureResponse, HttpStatus.NOT_FOUND);
        }
    }
}