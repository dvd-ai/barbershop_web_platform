package com.app.barbershopweb.integrationtests.user.registration;

import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.user.credentials.UserCredentialsRepository;
import com.app.barbershopweb.user.crud.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_URL;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_VALID_USER_ID;
import static com.app.barbershopweb.user.registration.constants.UserRegistration_Dto__TestConstants.USER_REGISTRATION_VALID_DTO;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserRegistrationIT extends AbstractIT {

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
    void shouldAddUser() {
        ResponseEntity<Long> response = restTemplate.postForEntity(
                USERS_URL, USER_REGISTRATION_VALID_DTO, Long.class
        );

        assertEquals(USERS_VALID_USER_ID, response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}
