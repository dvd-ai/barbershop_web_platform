package com.app.barbershopweb.exception;

import java.util.List;

public class MailException extends RuntimeException {

    private final List<String> messages;

    public MailException(List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }

}
