package com.dias.services.mnemo.exception;

import org.springframework.http.HttpStatus;

public class SchemasException extends RuntimeException {

    private final Object[] args;
    private HttpStatus status;

    public SchemasException(String message, Object ...args) {
        this(message, HttpStatus.INTERNAL_SERVER_ERROR, args);
    }

    public SchemasException(String message, HttpStatus status, Object ...args) {
        super(message);
        this.args = args;
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        if (args != null) {
            message = String.format(message, args);
        }
        return message;
    }
}
