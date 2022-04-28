package com.app.barbershopweb.exception;

import java.util.List;

public class ValidationException extends RuntimeException {

    private final List<String> messages;

    public ValidationException(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }
}
