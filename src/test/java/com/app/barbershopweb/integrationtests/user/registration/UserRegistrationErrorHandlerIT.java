package com.app.barbershopweb.integrationtests.user.registration;

import com.app.barbershopweb.error.ErrorDto;
import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.user.credentials.UserCredentialsRepository;
import com.app.barbershopweb.user.crud.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static com.app.barbershopweb.user.credentials.error.UserCredentialsErrorMessage_Uk__TestConstants.USER_CREDENTIALS_ERR_UK_USERNAME;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_URL;
import static com.app.barbershopweb.user.registration.constants.UserRegistrationErrorMessage_Dto__TestConstants.*;
import static com.app.barbershopweb.user.registration.constants.UserRegistration_Dto__TestConstants.USER_REGISTRATION_INVALID_DTO;
import static com.app.barbershopweb.user.registration.constants.UserRegistration_Dto__TestConstants.USER_REGISTRATION_VALID_DTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRegistrationErrorHandlerIT extends AbstractIT {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserCredentialsRepository userCredentialsRepository;

    @AfterEach
    void cleanUp() {
        userRepository.truncateAndRestartSequence();
        userCredentialsRepository.truncateAndRestartSequence();
    }

    @Test
    void invalidDto() {
        ResponseEntity<ErrorDto> response = restTemplate.postForEntity(
                USERS_URL, USER_REGISTRATION_INVALID_DTO, ErrorDto.class
        );
        List<String> errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(7, errors.size());
        assertTrue(errors.contains(USER_REGISTRATION_INVALID_DTO_FIRSTNAME));
        assertTrue(errors.contains(USER_REGISTRATION_INVALID_DTO_LASTNAME));
        assertTrue(errors.contains(USER_REGISTRATION_INVALID_DTO_PHONE_NUMBER));
        assertTrue(errors.contains(USER_REGISTRATION_INVALID_DTO_EMAIL));
        assertTrue(errors.contains(USER_REGISTRATION_INVALID_DTO_USERNAME));
        assertTrue(errors.contains(USER_REGISTRATION_INVALID_DTO_PASSWORD));
        assertTrue(errors.contains(USER_REGISTRATION_INVALID_DTO_REGISTRATION_DATE));
    }

    @Test
    void usernameIsAlreadyTaken() {
        restTemplate.postForEntity(
                USERS_URL, USER_REGISTRATION_VALID_DTO, Long.class
        );

        ResponseEntity<ErrorDto> response = restTemplate.postForEntity(
                USERS_URL, USER_REGISTRATION_VALID_DTO, ErrorDto.class
        );

        List<String> errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, errors.size());
        assertTrue(errors.contains(USER_CREDENTIALS_ERR_UK_USERNAME));
    }
}
