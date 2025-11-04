package com.nantan.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Configuration class for asynchronous processing.
 * This sets up a dedicated thread pool for @Async tasks.
 */
@Configuration
public class AsyncConfig {

    private static final Logger logger = LoggerFactory.getLogger(AsyncConfig.class);

    public static final String TASK_EXECUTOR_NAME = "taskExecutor";

    @Bean(name = TASK_EXECUTOR_NAME)
    public Executor taskExecutor() {
        logger.debug("Creating Async Task Executor");
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Set the core number of threads.
        // This is the number of threads that are kept alive in the pool.
        executor.setCorePoolSize(4);

        // Set the max number of threads.
        // The pool can grow up to this size when the queue is full.
        executor.setMaxPoolSize(10);

        // Set the queue capacity.
        // This is the number of tasks that can be queued before new threads are created.
        executor.setQueueCapacity(25);

        // Set the name prefix for the threads in this pool.
        // This is very useful for debugging and logging.
        executor.setThreadNamePrefix("BookAsync-");

        // Initialize the executor.
        executor.initialize();

        return executor;
    }
}