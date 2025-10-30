package com.nantan.app;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the connection to the MongoDB database using a Singleton pattern.
 * This class is responsible for initializing the MongoClient and providing
 * access to the database.
 */
public final class MongoManager {

    private static final Logger logger = Logger.getLogger(MongoManager.class.getName());
    private static final String CONFIG_FILE = "application.properties";
    private static final String MONGO_URI_KEY = "mongodb.uri";
    private static final String DATABASE_NAME = "libraryDB"; // 定义我们的数据库名称

    // Volatile keyword ensures that multiple threads handle the instance variable correctly.
    private static volatile MongoClient instance = null;

    static {
        // Suppress verbose MongoDB driver logging
        Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);

        // Load connection string from properties file
        String connectionString = loadConnectionString();
        if (connectionString == null) {
            logger.severe("Database connection string is not configured. Halting application.");
            // In a real app, you might throw a custom configuration exception.
            // For this console app, we'll exit to prevent NullPointerExceptions later.
            System.exit(1);
        }

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();

        // Initialize the MongoClient instance
        try {
            instance = MongoClients.create(settings);
            // Optional: Ping the database to confirm connection on startup.
            instance.getDatabase("admin").runCommand(new org.bson.Document("ping", 1));
            logger.info("Successfully connected to MongoDB Atlas.");
        } catch (MongoException e) {
            logger.log(Level.SEVERE, "Failed to connect to MongoDB", e);
            System.exit(1);
        }
    }

    // Private constructor to prevent instantiation
    private MongoManager() {
    }



    /**
     * Reads the MongoDB connection string from the application.properties file.
     * @return The connection string, or null if not found or an error occurs.
     */
    private static String loadConnectionString() {
        Properties properties = new Properties();
        try (InputStream input = MongoManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                logger.severe("Sorry, unable to find " + CONFIG_FILE);
                return null;
            }
            properties.load(input);
            return properties.getProperty(MONGO_URI_KEY);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error reading properties file", ex);
            return null;
        }
    }

    /**
     * Returns the singleton instance of the MongoClient.
     *
     * @return The active MongoClient.
     */
    public static MongoClient getClient() {
        return instance;
    }

    /**
     * Provides access to our specific application database.
     *
     * @return The MongoDatabase instance for our library application.
     */
    public static MongoDatabase getDatabase() {
        return instance.getDatabase(DATABASE_NAME);
    }

    /**
     * Closes the MongoDB connection. This should be called on application shutdown.
     */
    public static void close() {
        if (instance != null) {
            instance.close();
            logger.info("MongoDB connection closed.");
        }
    }
}