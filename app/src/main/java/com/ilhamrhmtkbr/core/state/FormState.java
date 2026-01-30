package com.ilhamrhmtkbr.core.state;

import java.util.Map;

public class FormState<T> {
    public enum Status {
        IDLE,           // Belum submit
        LOADING,        // Sedang submit
        SUCCESS,        // Submit berhasil
        ERROR,          // Submit gagal (general error)
        VALIDATION_ERROR // Validation error dari server
    }

    private Status status;
    private T data;
    private String message;
    private Map<String, String> validationErrors; // Field errors

    private FormState(Status status, T data, String message, Map<String, String> validationErrors) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.validationErrors = validationErrors;
    }

    public static <T> FormState<T> idle() {
        return new FormState<>(Status.IDLE, null, null, null);
    }

    public static <T> FormState<T> loading() {
        return new FormState<>(Status.LOADING, null, null, null);
    }

    public static <T> FormState<T> success(String message, T data) {
        return new FormState<>(Status.SUCCESS, data, message, null);
    }

    public static <T> FormState<T> error(String message) {
        return new FormState<>(Status.ERROR, null, message, null);
    }

    public static <T> FormState<T> validationError(Map<String, String> errors) {
        return new FormState<>(Status.VALIDATION_ERROR, null, null, errors);
    }

    // Getters
    public Status getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    // Helper methods
    public boolean isIdle() {
        return status == Status.IDLE;
    }

    public boolean isLoading() {
        return status == Status.LOADING;
    }

    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    public boolean isError() {
        return status == Status.ERROR;
    }

    public boolean isValidationError() {
        return status == Status.VALIDATION_ERROR;
    }
}