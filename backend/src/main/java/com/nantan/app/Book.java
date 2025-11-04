package com.nantan.app;

import jakarta.persistence.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Represents a book in the library.
 * This class is now annotated for both JPA (MySQL) and Spring Data MongoDB.
 */
@Entity // For JPA
@Table(name = "books") // For JPA
@Document(collection = "books") // For MongoDB
public class Book {

    @Id // Common @Id for both JPA and MongoDB
    @Column(name = "id") // For JPA
    @Field("id") // For MongoDB, explicitly map to the 'id' field in the document
    private Integer id;

    @Column(name = "title", nullable = false)
    @Field("title")
    private String title;

    @Column(name = "author")
    @Field("author")
    private String author;

    // A no-arg constructor is required by both JPA and Spring Data
    public Book() {
    }

    // Constructor for creating new books, now requires an ID again
    public Book(Integer id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    // --- Getters and Setters ---
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "书籍编号: " + id + ", 书名: '" + title + "', 作者: '" + author + "'";
    }
}