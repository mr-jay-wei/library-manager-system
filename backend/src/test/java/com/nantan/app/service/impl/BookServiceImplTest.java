package com.nantan.app.service.impl;

import com.nantan.app.Book;
import com.nantan.app.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository jpaRepository;

    @Mock
    private BookRepository mongoRepository;

    // 移除 @InjectMocks
    private BookServiceImpl bookServiceImpl;

    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        // 手动创建被测试类的实例，并传入 mock 对象
        bookServiceImpl = new BookServiceImpl(jpaRepository, mongoRepository);

        book1 = new Book(1, "The Lord of the Rings", "J.R.R. Tolkien");
        book2 = new Book(2, "The Hobbit", "J.R.R. Tolkien");
    }

    @Test
    @DisplayName("getAllBooks should return list of books from MySQL")
    void getAllBooks_whenDataSourceIsMysql_shouldReturnBookList() throws ExecutionException, InterruptedException {
        // Given
        when(jpaRepository.findAll()).thenReturn(List.of(book1, book2));
        // **重要**: 确保 mongoRepository 在此测试中不被调用时返回空列表，避免交叉影响
        // when(mongoRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        CompletableFuture<List<Book>> futureBooks = bookServiceImpl.getAllBooks("mysql");
        List<Book> books = futureBooks.get();

        // Then
        assertNotNull(books);
        assertEquals(2, books.size()); // <-- 修复点：这个断言现在应该能通过
        assertEquals("The Lord of the Rings", books.get(0).getTitle());

        verify(jpaRepository, times(1)).findAll();
        verify(mongoRepository, never()).findAll();
    }

    @Test
    @DisplayName("getAllBooks should return list of books from MongoDB")
    void getAllBooks_whenDataSourceIsMongo_shouldReturnBookList() throws ExecutionException, InterruptedException {
        // Given
        when(mongoRepository.findAll()).thenReturn(List.of(book1));

        // When
        CompletableFuture<List<Book>> futureBooks = bookServiceImpl.getAllBooks("mongo");
        List<Book> books = futureBooks.get();

        // Then
        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals(1, books.get(0).getId());

        verify(mongoRepository, times(1)).findAll();
        verify(jpaRepository, never()).findAll();
    }

    @Test
    @DisplayName("addBook should save a book to MySQL and return it")
    void addBook_whenDataSourceIsMysql_shouldSaveAndReturnBook() throws ExecutionException, InterruptedException {
        // Given
        Book newBook = new Book(3, "New Book", "New Author");
        // **修复点**: 明确地对传入的 newBook 对象进行打桩
        when(jpaRepository.save(newBook)).thenReturn(newBook);

        // When
        CompletableFuture<Book> futureSavedBook = bookServiceImpl.addBook(newBook, "mysql");
        Book savedBook = futureSavedBook.get();

        // Then
        assertNotNull(savedBook); // <-- 修复点：这个断言现在应该能通过
        assertEquals(3, savedBook.getId());
        assertEquals("New Book", savedBook.getTitle());

        verify(jpaRepository, times(1)).save(newBook);
        verify(mongoRepository, never()).save(any(Book.class));
    }

    @Test
    @DisplayName("deleteBookById should return true when book exists in MySQL")
    void deleteBookById_whenBookExists_shouldReturnTrue() throws ExecutionException, InterruptedException {
        // Given
        int bookIdToDelete = 1;
        when(jpaRepository.deleteById(bookIdToDelete)).thenReturn(true);

        // When
        CompletableFuture<Boolean> futureResult = bookServiceImpl.deleteBookById(bookIdToDelete, "mysql");
        boolean result = futureResult.get();

        // Then
        assertTrue(result);
        verify(jpaRepository, times(1)).deleteById(bookIdToDelete);
        verify(mongoRepository, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("deleteBookById should return false when book does not exist in MySQL")
    void deleteBookById_whenBookDoesNotExist_shouldReturnFalse() throws ExecutionException, InterruptedException {
        // Given
        int bookIdToDelete = 99;
        when(jpaRepository.deleteById(bookIdToDelete)).thenReturn(false);

        // When
        CompletableFuture<Boolean> futureResult = bookServiceImpl.deleteBookById(bookIdToDelete, "mysql");
        boolean result = futureResult.get();

        // Then
        assertFalse(result);
        verify(jpaRepository, times(1)).deleteById(bookIdToDelete);
    }
}