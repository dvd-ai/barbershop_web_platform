package com.app.barbershopweb.database.user;

import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.user.crud.User;
import com.app.barbershopweb.user.crud.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static com.app.barbershopweb.user.crud.constants.UserEntity__TestConstants.USER_VALID_ENTITY;
import static com.app.barbershopweb.user.crud.constants.UserEntity__TestConstants.USER_VALID_UPDATED_USER_ENTITY;
import static com.app.barbershopweb.user.crud.constants.UserList__TestConstants.USER_USER_VALID_ENTITY_LIST;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_NOT_EXISTING_USER_ID;
import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest extends AbstractIT {

    @Autowired
    UserRepository usersRepository;


    @Test
    void addUser() {
        Long id = usersRepository.addUser(USER_VALID_ENTITY);
        assertTrue(id > 0);
    }

    @Test
    void findUserByExistedId() {
        Long id = usersRepository.addUser(USER_VALID_ENTITY);
        Optional<User> userOptional = usersRepository.findUserById(id);

        assertTrue(userOptional.isPresent());
        assertEquals(USER_VALID_ENTITY, userOptional.get());
    }

    @Test
    void findUserByNotExistingId() {
        Optional<User> userOptional = usersRepository.findUserById(
                USERS_NOT_EXISTING_USER_ID
        );

        assertTrue(userOptional.isEmpty());
    }

    @Test
    void updateExistingUser() {
        usersRepository.addUser(
                USER_VALID_ENTITY
        );

        Optional<User> updUserOptional = usersRepository.updateUser(
                USER_VALID_UPDATED_USER_ENTITY
        );

        assertTrue(updUserOptional.isPresent());
        assertEquals(USER_VALID_UPDATED_USER_ENTITY, updUserOptional.get());
    }

    @Test
    void updateNotExistingUser() {
        Optional<User> updUserOptional = usersRepository.updateUser(
                USER_VALID_UPDATED_USER_ENTITY
        );

        assertTrue(updUserOptional.isEmpty());
    }

    @Test
    void getUsers() {
        assertTrue(usersRepository.getUsers().isEmpty());

        usersRepository.addUser(USER_VALID_ENTITY);
        usersRepository.addUser(USER_VALID_ENTITY);
        usersRepository.addUser(USER_VALID_ENTITY);

        List<User> users = usersRepository.getUsers();

        assertEquals(USER_USER_VALID_ENTITY_LIST.size(), users.size());
        assertEquals(USER_USER_VALID_ENTITY_LIST, users);
    }

    @Test
    void deleteUserById() {
        Long id = usersRepository.addUser(USER_VALID_ENTITY);
        usersRepository.deleteUserById(id);

        assertTrue(usersRepository.getUsers().isEmpty());
    }

    @Test
    void userExistsById() {
        assertFalse(usersRepository.userExistsById(USERS_NOT_EXISTING_USER_ID));

        Long id = usersRepository.addUser(USER_VALID_ENTITY);

        assertTrue(usersRepository.userExistsById(id));
    }

    @Test
    void truncateAndRestartIdentity() {
        usersRepository.addUser(USER_VALID_ENTITY);
        usersRepository.truncateAndRestartSequence();

        assertTrue(usersRepository.getUsers().isEmpty());

        Long id = usersRepository.addUser(USER_VALID_ENTITY);

        assertEquals(1, id);
    }

    @AfterEach
    void cleanUpDb() {
        usersRepository.truncateAndRestartSequence();
    }
}