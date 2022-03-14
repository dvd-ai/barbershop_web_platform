package com.app.barbershopweb.exception;

import java.util.List;

public class DbUniqueConstraintsViolationException extends RuntimeException{

    private final List<String> messages;

    public DbUniqueConstraintsViolationException(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }


}
