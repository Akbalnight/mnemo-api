package com.dias.services.mnemo.exception;

import org.springframework.http.HttpStatus;

public class SchemasException extends RuntimeException {

    private final String[] args;
    private final HttpStatus status;

    public SchemasException(String message, HttpStatus status, String ...args) {
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
