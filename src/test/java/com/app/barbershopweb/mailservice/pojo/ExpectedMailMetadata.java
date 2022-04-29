package com.app.barbershopweb.mailservice.pojo;

public record ExpectedMailMetadata(
        String from, String to, String subject, String body
) {
}
