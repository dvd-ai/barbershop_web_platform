package com.app.barbershopweb.exception;

import java.util.List;

public class InvalidBusinessDataFormatException extends RuntimeException{

    private final List<String> messages;

    public InvalidBusinessDataFormatException(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }
}
