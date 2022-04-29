package com.app.barbershopweb.mailservice;

import com.app.barbershopweb.exception.MailException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;

import static com.app.barbershopweb.user.crud.constants.UserEntity__TestConstants.USER_VALID_ENTITY;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    static SimpleMailMessage message;
    @Mock
    JavaMailSender mailSender;
    @InjectMocks
    MailService mailService;

    @BeforeAll
    static void init() {
        message = new SimpleMailMessage();
        message.setSubject("Your appointments canceled because barbershop is out of business");
        message.setText("""
                Dear %s %s, your ...
                """.formatted(USER_VALID_ENTITY.getFirstName(), USER_VALID_ENTITY.getLastName()));
        message.setTo(USER_VALID_ENTITY.getEmail());
        message.setFrom("no-reply@gmail.com");

    }

    @Test
    void notifyAboutOutOfBusiness() {
        mailService.notifyAboutOutOfBusiness(List.of(USER_VALID_ENTITY));

        verify(mailSender).send(message);
    }

    @Test
    @DisplayName("when JavaMailSender throws an exception - the method throws (own) MailException")
    void notifyAboutOutOfBusiness__MailException() {
        doThrow(new MailSendException("")).when(mailSender).send(message);

        assertThrows(MailException.class, () ->
                mailService.notifyAboutOutOfBusiness(List.of(USER_VALID_ENTITY))
        );
    }
}