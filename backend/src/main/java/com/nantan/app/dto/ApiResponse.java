package com.nantan.app.dto;

import java.io.Serializable;

/**
 * A generic class for wrapping all API responses.
 * This provides a consistent structure for both successful and failed responses.
 *
 * @param <T> the type of the data payload.
 */
public class ApiResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Business status code. e.g., 200 for success, 5000 for server error.
     */
    private int code;

    /**
     * A human-readable message providing details about the response.
     */
    private String message;

    /**
     * The actual data payload of the response.
     */
    private T data;

    // --- Constructors ---

    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(int code, String message) {
        this(code, message, null);
    }

    // --- Static factory methods for convenience ---

    /**
     * Creates a success response with data.
     *
     * @param data the payload to return.
     * @param <T>  the type of the payload.
     * @return a success ApiResponse instance.
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "Success", data);
    }

    /**
     * Creates a success response without data.
     *
     * @return a success ApiResponse instance.
     */
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(200, "Success", null);
    }

    /**
     * Creates a failure response.
     *
     * @param code    the business error code.
     * @param message the error message.
     * @param <T>     the type of the data (usually null for errors).
     * @return a failure ApiResponse instance.
     */
    public static <T> ApiResponse<T> failure(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    // --- Getters and Setters ---

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}