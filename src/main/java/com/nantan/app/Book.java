package com.nantan.app;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Represents a book in the library.
 * This class is now a JPA Entity, meaning it can be directly mapped to a database table.
 * 这个类现在是一个 JPA 实体，可以被直接映射到数据库表。
 */
@Entity // 1. 声明这是一个JPA实体类
@Table(name = "books") // 2. 明确指定它映射到数据库中的 'books' 表
public class Book {

    @Id // 3. 声明这个属性是主键 (Primary Key)
    @Column(name = "id") // 4. 映射到表中的 'id' 列
    private int id;

    @Column(name = "title", nullable = false) // 5. 映射到 'title' 列，并指定该列不应为空
    private String title;

    @Column(name = "author") // 6. 映射到 'author' 列
    private String author;

    // JPA 规范要求实体类必须有一个公共的或受保护的无参构造函数
    public Book() {
    }

    public Book(String title, String author, int id) {
        this.title = title;
        this.author = author;
        this.id = id;
    }

    // --- Getters and Setters remain unchanged ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
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
        return "书籍编号: " + id + ", 书名: " + title + ", 作者: " + author;
    }
}