package com.app.barbershopweb.mailservice;

import com.app.barbershopweb.exception.MailException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;

import static com.app.barbershopweb.barbershop.crud.constants.BarbershopEntity__TestConstants.BARBERSHOP_VALID_ENTITY;
import static com.app.barbershopweb.mailservice.constants.MailService_Metadata__TestConstants.MAIL_SERVICE_SIMPLE_MAIL_MESSAGE;
import static com.app.barbershopweb.user.crud.constants.UserEntity__TestConstants.USERS_VALID_ENTITY;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Mock
    JavaMailSender mailSender;

    @InjectMocks
    MailService mailService;

    @Test
    void notifyAboutOutOfBusiness() {
        mailService.notifyAboutOutOfBusiness(List.of(USERS_VALID_ENTITY),
                BARBERSHOP_VALID_ENTITY.getEmail()
        );

        verify(mailSender).send(MAIL_SERVICE_SIMPLE_MAIL_MESSAGE);
    }

    @Test
    @DisplayName("when JavaMailSender throws an exception - the method throws (own) MailException")
    void notifyAboutOutOfBusiness__MailException() {
        doThrow(new MailSendException("")).when(mailSender).send(MAIL_SERVICE_SIMPLE_MAIL_MESSAGE);

        assertThrows(MailException.class, () ->
                mailService.notifyAboutOutOfBusiness(List.of(USERS_VALID_ENTITY),
                        BARBERSHOP_VALID_ENTITY.getEmail()
                )
        );
    }
}