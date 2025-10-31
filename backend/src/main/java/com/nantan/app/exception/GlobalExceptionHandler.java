package com.nantan.app.exception;

import com.nantan.app.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleAllExceptions(Exception ex) {
        // 2. Use logger.error to record the exception with its full stack trace.
        // The '{}' is a placeholder for the exception message, and 'ex' provides the stack trace.
        logger.error("An unexpected error occurred: {}", ex.getMessage(), ex);

        ApiResponse<Void> failureResponse = ApiResponse.failure(5000, "Internal Server Error. Please contact support.");
        return new ResponseEntity<>(failureResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}