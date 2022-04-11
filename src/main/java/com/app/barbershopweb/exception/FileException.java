package com.app.barbershopweb.exception;

import java.util.List;

public class FileException extends RuntimeException{
    private final List<String> messages;

    public FileException(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }
}
