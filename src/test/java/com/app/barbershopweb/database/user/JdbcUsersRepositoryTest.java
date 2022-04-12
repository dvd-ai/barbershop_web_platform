package com.app.barbershopweb.database.user;

import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.user.crud.Users;
import com.app.barbershopweb.user.crud.repository.JdbcUsersRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static com.app.barbershopweb.user.crud.constants.UserEntity__TestConstants.USERS_VALID_ENTITY;
import static com.app.barbershopweb.user.crud.constants.UserEntity__TestConstants.USERS_VALID_UPDATED_USER_ENTITY;
import static com.app.barbershopweb.user.crud.constants.UserList__TestConstants.USERS_USER_VALID_ENTITY_LIST;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_NOT_EXISTED_USER_ID;
import static org.junit.jupiter.api.Assertions.*;

class JdbcUsersRepositoryTest extends AbstractIT {

    @Autowired
    JdbcUsersRepository usersRepository;


    @Test
    void addUser() {
        Long id = usersRepository.addUser(USERS_VALID_ENTITY);
        assertTrue(id > 0);
    }

    @Test
    void findUserByExistedId() {
        Long id = usersRepository.addUser(USERS_VALID_ENTITY);
        Optional<Users> userOptional = usersRepository.findUserById(id);

        assertTrue(userOptional.isPresent());
        assertEquals(USERS_VALID_ENTITY, userOptional.get());
    }

    @Test
    void findUserByNotExistingId() {
        Optional<Users> userOptional = usersRepository.findUserById(
                USERS_NOT_EXISTED_USER_ID
        );

        assertTrue(userOptional.isEmpty());
    }

    @Test
    void updateExistingUser() {
        usersRepository.addUser(
                USERS_VALID_ENTITY
        );

        Optional<Users> updUserOptional = usersRepository.updateUser(
                USERS_VALID_UPDATED_USER_ENTITY
        );

        assertTrue(updUserOptional.isPresent());
        assertEquals(USERS_VALID_UPDATED_USER_ENTITY, updUserOptional.get());
    }

    @Test
    void updateNotExistingUser() {
        Optional<Users> updUserOptional = usersRepository.updateUser(
                USERS_VALID_UPDATED_USER_ENTITY
        );

        assertTrue(updUserOptional.isEmpty());
    }

    @Test
    void getUsers() {
        assertTrue(usersRepository.getUsers().isEmpty());

        usersRepository.addUser(USERS_VALID_ENTITY);
        usersRepository.addUser(USERS_VALID_ENTITY);
        usersRepository.addUser(USERS_VALID_ENTITY);

        List<Users> users = usersRepository.getUsers();

        assertEquals(USERS_USER_VALID_ENTITY_LIST.size(), users.size());
        assertEquals(USERS_USER_VALID_ENTITY_LIST, users);
    }

    @Test
    void deleteUserById() {
        Long id = usersRepository.addUser(USERS_VALID_ENTITY);
        usersRepository.deleteUserById(id);

        assertTrue(usersRepository.getUsers().isEmpty());
    }

    @Test
    void userExistsById() {
        assertFalse(usersRepository.userExistsById(USERS_NOT_EXISTED_USER_ID));

        Long id = usersRepository.addUser(USERS_VALID_ENTITY);

        assertTrue(usersRepository.userExistsById(id));
    }

    @Test
    void truncateAndRestartIdentity() {
        usersRepository.addUser(USERS_VALID_ENTITY);
        usersRepository.truncateAndRestartSequence();

        assertTrue(usersRepository.getUsers().isEmpty());

        Long id = usersRepository.addUser(USERS_VALID_ENTITY);

        assertEquals(1, id);
    }

    @AfterEach
    void cleanUpDb() {
        usersRepository.truncateAndRestartSequence();
    }
}