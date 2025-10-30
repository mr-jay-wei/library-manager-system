package com.nantan.app;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.logging.Logger;

/**
 * JPA-based implementation of the BookRepository interface for MySQL.
 */
public class BookJpaDAO implements BookRepository {
    private static final Logger logger = Logger.getLogger(BookJpaDAO.class.getName());

    @Override
    public List<Book> findAll() {
        EntityManager em = JpaManager.getEntityManager();
        try {
            // Using JPQL (Java Persistence Query Language), similar to SQL but for entities.
            String jpql = "SELECT b FROM Book b";
            TypedQuery<Book> query = em.createQuery(jpql, Book.class);
            List<Book> books = query.getResultList();
            logger.info("Found " + books.size() + " books in MySQL.");
            return books;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void save(Book book) {
        EntityManager em = JpaManager.getEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();

            // persist() is used for saving a new entity.
            // merge() would be used for updating an existing one.
            em.persist(book);

            tx.commit();
            logger.info("Successfully saved book to MySQL: " + book);
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            logger.severe("Failed to save book to MySQL: " + e.getMessage());
            throw e; // Re-throw the exception after rolling back
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public boolean deleteById(int bookId) {
        EntityManager em = JpaManager.getEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();

            // First, find the entity by its primary key
            Book bookToDelete = em.find(Book.class, bookId);

            if (bookToDelete != null) {
                // If found, remove it
                em.remove(bookToDelete);
                tx.commit();
                logger.info("Successfully deleted book with id: " + bookId + " from MySQL.");
                return true;
            } else {
                tx.commit(); // Still need to commit the (empty) transaction
                logger.warning("No book found with id: " + bookId + " to delete from MySQL.");
                return false;
            }
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            logger.severe("Failed to delete book from MySQL: " + e.getMessage());
            throw e;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void close() {
        // The closing logic is centralized in JpaManager
        JpaManager.close();
    }
}