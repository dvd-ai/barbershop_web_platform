package com.app.barbershopweb.exception;

public class DbUniqueConstraintsViolationException extends RuntimeException{
    public DbUniqueConstraintsViolationException(String message) {
        super(message);
    }
}
