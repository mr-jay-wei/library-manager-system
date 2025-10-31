package com.nantan.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * This class is a Spring Component that runs on application startup.
 * It implements CommandLineRunner, so its `run` method will be executed
 * after the Spring application context is loaded.
 * This is the new home for our application's main logic.
 */
@Component // 1. Mark this class as a Spring component so it's detected and managed by Spring.
public class LibraryCommandLineRunner implements CommandLineRunner {

    // 2. Use Dependency Injection to get repository instances from the Spring context.
    // We are injecting both implementations.
    private final BookRepository jpaRepository;
    private final BookRepository mongoRepository;

    @Autowired
    public LibraryCommandLineRunner(
            @Qualifier("jpaBookRepository") BookRepository jpaRepository,
            @Qualifier("mongoBookRepository") BookRepository mongoRepository) {
        this.jpaRepository = jpaRepository;
        this.mongoRepository = mongoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        BookRepository repository = null;

        try (Scanner sc = new Scanner(System.in)) {
            // Step 1: Let the user choose the data source
            System.out.println("======================================================");
            System.out.println(" Welcome to the Spring Boot Library Manager ");
            System.out.println("======================================================");
            while (repository == null) {
                System.out.println("Please select the database to use:");
                System.out.println("1. MongoDB");
                System.out.println("2. MySQL (JPA)");
                System.out.print("Enter your choice [1-2]: ");
                try {
                    int dbChoice = sc.nextInt();
                    sc.nextLine(); // Consume newline

                    // Step 2: Assign the correct injected bean based on user choice
                    if (dbChoice == 1) {
                        repository = mongoRepository; // Use the injected Mongo repository
                        System.out.println("--> MongoDB repository selected.");
                    } else if (dbChoice == 2) {
                        repository = jpaRepository; // Use the injected JPA repository
                        System.out.println("--> MySQL (JPA) repository selected.");
                    } else {
                        System.out.println("--> Invalid choice. Please try again.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("--> Invalid input! Please enter a valid number.");
                    sc.nextLine(); // Clear the invalid input
                }
            }

            // Step 3: All subsequent operations are performed through the abstract repository interface
            System.out.println(
                    """
                    
                    ----------------------------------------------
                    1. List all books
                    2. Add a new book
                    3. Delete a book
                    4. Exit application
                    ----------------------------------------------
                    """
            );

            boolean running = true;
            while (running) {
                System.out.print("\nEnter your option: ");
                try {
                    int choice = sc.nextInt();
                    sc.nextLine(); // Consume newline

                    switch (choice) {
                        case 1 -> {
                            List<Book> allBooks = repository.findAll();
                            if (allBooks.isEmpty()) {
                                System.out.println("--> No books found in the database.");
                            } else {
                                System.out.println("--> Current list of books:");
                                allBooks.forEach(System.out::println);
                            }
                        }
                        case 2 -> {
                            System.out.print("Enter book ID: "); // <-- 恢复输入 ID
                            int id = sc.nextInt();
                            sc.nextLine(); // consume newline

                            System.out.print("Enter book title: ");
                            String title = sc.nextLine();

                            System.out.print("Enter book author: ");
                            String author = sc.nextLine();

                            Book newBook = new Book(id, title, author); // <-- 使用带 ID 的构造函数
                            Book savedBook = repository.save(newBook);
                            System.out.println("--> Book successfully saved: " + savedBook);
                        }
                        case 3 -> {
                            System.out.print("Enter the ID of the book to delete: ");
                            int deleteId = sc.nextInt();
                            sc.nextLine();

                            boolean success = repository.deleteById(deleteId);
                            if (success) {
                                System.out.println("--> Book with ID " + deleteId + " has been deleted.");
                            } else {
                                System.out.println("--> Book with ID " + deleteId + " not found.");
                            }
                        }
                        case 4 -> {
                            System.out.println("--> Exiting application. Goodbye!");
                            repository.close(); // This now calls the default empty method
                            running = false;
                        }
                        default -> System.out.println("--> Invalid option. Please enter a number between 1-4.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("--> Invalid input! Please enter a valid number.");
                    sc.nextLine();
                } catch (Exception e) {
                    System.err.println("--> An error occurred during the operation:");
                    e.printStackTrace();
                }
            }
        }
        // The application will exit after the run method completes.
    }
}