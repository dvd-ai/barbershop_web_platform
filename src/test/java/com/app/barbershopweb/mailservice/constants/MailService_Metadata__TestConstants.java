package com.app.barbershopweb.mailservice.constants;

import org.springframework.mail.SimpleMailMessage;

import static com.app.barbershopweb.barbershop.crud.constants.BarbershopEntity__TestConstants.BARBERSHOP_VALID_ENTITY;
import static com.app.barbershopweb.user.crud.constants.UserEntity__TestConstants.USERS_VALID_ENTITY;

public final class MailService_Metadata__TestConstants {

    public static final SimpleMailMessage MAIL_SERVICE_SIMPLE_MAIL_MESSAGE = getSimpleMailMessage();
    public static final String MAIL_SERVICE_MESSAGES_URL = "/api/v1/messages";

    private static SimpleMailMessage getSimpleMailMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Your appointments canceled because barbershop is out of business");
        message.setText("""
                Dear %s %s, your ...
                """.formatted(USERS_VALID_ENTITY.getFirstName(), USERS_VALID_ENTITY.getLastName()));
        message.setTo(USERS_VALID_ENTITY.getEmail());
        message.setFrom("no-reply@" + BARBERSHOP_VALID_ENTITY.getEmail().split("@")[1]);

        return message;
    }

}
