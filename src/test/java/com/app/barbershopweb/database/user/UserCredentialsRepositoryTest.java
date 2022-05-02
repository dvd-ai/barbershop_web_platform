package com.app.barbershopweb.database.user;

import com.app.barbershopweb.exception.DbUniqueConstraintsViolationException;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.user.credentials.UserCredentialsRepository;
import com.app.barbershopweb.user.crud.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.app.barbershopweb.user.credentials.UserCredentials_Entities__TestConstants.USER_CREDENTIALS_VALID_ENTITY;
import static com.app.barbershopweb.user.credentials.error.UserCredentialsErrorMessage_Fk__TestConstants.USER_CREDENTIALS_ERR_FK_USER_ID;
import static com.app.barbershopweb.user.credentials.error.UserCredentialsErrorMessage_Uk__TestConstants.USER_CREDENTIALS_ERR_UK_USERNAME;
import static com.app.barbershopweb.user.credentials.error.UserCredentialsErrorMessage_Uk__TestConstants.USER_CREDENTIALS_ERR_UK_USER_ID;
import static com.app.barbershopweb.user.crud.constants.UserEntity__TestConstants.USER_VALID_ENTITY;
import static org.junit.jupiter.api.Assertions.*;

class UserCredentialsRepositoryTest extends AbstractIT {

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
    void addUserCredentials() {

        Long id = userRepository.addUser(USER_VALID_ENTITY);
        Long userId = userCredentialsRepository.addUserCredentials(USER_CREDENTIALS_VALID_ENTITY);

        assertEquals(id, userId);
    }

    @Test
    void addUserCredentials__fk() {
        List<String> messages = assertThrows(NotFoundException.class,
                () -> userCredentialsRepository.addUserCredentials(USER_CREDENTIALS_VALID_ENTITY)
        ).getMessages();

        assertEquals(1, messages.size());
        assertEquals(USER_CREDENTIALS_ERR_FK_USER_ID, messages.get(0));
    }

    @Test
    void addUserCredentials__uk() {
        userRepository.addUser(USER_VALID_ENTITY);
        userCredentialsRepository.addUserCredentials(USER_CREDENTIALS_VALID_ENTITY);

        List<String> messages = assertThrows(DbUniqueConstraintsViolationException.class,
                () -> userCredentialsRepository.addUserCredentials(USER_CREDENTIALS_VALID_ENTITY)
        ).getMessages();

        System.out.println(messages.get(0));
        System.out.println(USER_CREDENTIALS_ERR_UK_USERNAME);
        System.out.println(messages.get(1));
        System.out.println(USER_CREDENTIALS_ERR_UK_USER_ID);

        assertEquals(2, messages.size());
        assertTrue(messages.contains(USER_CREDENTIALS_ERR_UK_USERNAME));
        assertTrue(messages.contains(USER_CREDENTIALS_ERR_UK_USER_ID));
    }

    @Test
    void truncateAndRestartIdentity() {
        userRepository.addUser(USER_VALID_ENTITY);
        userCredentialsRepository.addUserCredentials(USER_CREDENTIALS_VALID_ENTITY);
        userCredentialsRepository.truncateAndRestartSequence();

        assertFalse(userCredentialsRepository.credentialsExistByUsername(USER_CREDENTIALS_VALID_ENTITY.getUsername()));

        Long id = userCredentialsRepository.addUserCredentials(USER_CREDENTIALS_VALID_ENTITY);

        assertEquals(1, id);
    }


}