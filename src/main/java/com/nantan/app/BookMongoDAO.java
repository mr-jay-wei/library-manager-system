package com.nantan.app;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;

/**
 * MongoDB-specific implementation of the BookRepository interface.
 * 实现了 BookRepository 接口的 MongoDB 版本。
 */
public class BookMongoDAO implements BookRepository { // <-- 关键修改点 1: implements

    private static final Logger logger = Logger.getLogger(BookMongoDAO.class.getName());
    private final MongoCollection<Document> booksCollection;

    public BookMongoDAO() {
        MongoDatabase database = MongoManager.getDatabase();
        this.booksCollection = database.getCollection("books");
    }

    // ... private documentToBook and bookToDocument methods remain unchanged ...
    private Book documentToBook(Document doc) {
        if (doc == null) { return null; }
        return new Book(doc.getString("title"), doc.getString("author"), doc.getInteger("id"));
    }
    private Document bookToDocument(Book book) {
        return new Document("id", book.getId()).append("title", book.getTitle()).append("author", book.getAuthor());
    }


    // vvvvvv 关键修改点 2: 实现接口中的所有方法 vvvvvv
    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        for (Document doc : booksCollection.find()) {
            books.add(documentToBook(doc));
        }
        logger.info("Found " + books.size() + " books in MongoDB.");
        return books;
    }

    @Override
    public void save(Book book) {
        Document doc = bookToDocument(book);
        // In MongoDB, insertOne can be used for saving new documents.
        // For updates, we would need more complex logic (e.g., replaceOne with an upsert option).
        // For now, we assume save is for new books.
        booksCollection.insertOne(doc);
        logger.info("Successfully saved book to MongoDB: " + book);
    }

    @Override
    public boolean deleteById(int bookId) {
        DeleteResult result = booksCollection.deleteOne(eq("id", bookId));
        long deletedCount = result.getDeletedCount();
        if (deletedCount > 0) {
            logger.info("Successfully deleted book with id: " + bookId + " from MongoDB.");
        } else {
            logger.warning("No book found with id: " + bookId + " to delete from MongoDB.");
        }
        return deletedCount > 0;
    }

    @Override
    public void close() {
        // For MongoDB, the closing logic is centralized in MongoManager
        MongoManager.close();
    }
}