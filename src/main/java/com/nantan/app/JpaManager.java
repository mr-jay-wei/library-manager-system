package com.nantan.app;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.logging.Logger;

/**
 * Manages the JPA EntityManagerFactory and provides EntityManager instances.
 * Follows a Singleton pattern for the EntityManagerFactory.
 */
public final class JpaManager {
    private static final Logger logger = Logger.getLogger(JpaManager.class.getName());
    private static final String PERSISTENCE_UNIT_NAME = "mysql-persistence-unit";
    private static volatile EntityManagerFactory emfInstance = null;

    // Static initializer to create the expensive EntityManagerFactory
    static {
        try {
            // Create the EntityManagerFactory from the persistence.xml configuration
            emfInstance = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            logger.info("EntityManagerFactory created successfully for persistence unit: " + PERSISTENCE_UNIT_NAME);
        } catch (Throwable ex) {
            logger.severe("Initial EntityManagerFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private JpaManager() {}

    /**
     * Creates and returns a new EntityManager instance.
     * Each database operation should use its own EntityManager.
     * @return A new EntityManager.
     */
    public static EntityManager getEntityManager() {
        if (emfInstance == null) {
            throw new IllegalStateException("EntityManagerFactory has not been initialized.");
        }
        return emfInstance.createEntityManager();
    }

    /**
     * Closes the EntityManagerFactory. This should be called on application shutdown.
     */
    public static void close() {
        if (emfInstance != null && emfInstance.isOpen()) {
            emfInstance.close();
            logger.info("EntityManagerFactory closed.");
        }
    }
}