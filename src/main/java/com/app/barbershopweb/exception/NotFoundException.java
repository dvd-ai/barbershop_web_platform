package com.app.barbershopweb.exception;

import java.util.List;

public class NotFoundException extends RuntimeException {

    private final List<String> messages;

    public NotFoundException(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }
}
