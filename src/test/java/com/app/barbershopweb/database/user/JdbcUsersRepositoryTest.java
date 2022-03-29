package com.app.barbershopweb.database.user;

import com.app.barbershopweb.database.AbstractJdbcRepositoryTest;
import com.app.barbershopweb.user.UserTestConstants;
import com.app.barbershopweb.user.Users;
import com.app.barbershopweb.user.repository.JdbcUsersRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JdbcUsersRepositoryTest extends AbstractJdbcRepositoryTest {

    static JdbcUsersRepository usersRepository;
    UserTestConstants utc = new UserTestConstants();

    @BeforeAll
    static void init() {
        usersRepository = new JdbcUsersRepository(getDataSource());
    }

    @Test
    void addUser() {
        Long id = usersRepository.addUser(utc.VALID_USER_ENTITY);
        assertTrue(id > 0);
    }

    @Test
    void findUserByExistedId() {
        Long id = usersRepository.addUser(utc.VALID_USER_ENTITY);
        Optional<Users> userOptional = usersRepository.findUserById(id);

        assertTrue(userOptional.isPresent());
        assertEquals(utc.VALID_USER_ENTITY, userOptional.get());
    }

    @Test
    void findUserByNotExistingId() {
        Optional<Users> userOptional = usersRepository.findUserById(
                utc.NOT_EXISTED_USER_ID
        );

        assertTrue(userOptional.isEmpty());
    }

    @Test
    void updateExistingUser() {
        usersRepository.addUser(
                utc.VALID_USER_ENTITY
        );

        Optional<Users> updUserOptional = usersRepository.updateUser(
                utc.VALID_UPDATED_USER_ENTITY
        );

        assertTrue(updUserOptional.isPresent());
        assertEquals(utc.VALID_UPDATED_USER_ENTITY, updUserOptional.get());
    }

    @Test
    void updateNotExistingUser() {
        Optional<Users> updUserOptional = usersRepository.updateUser(
                utc.VALID_UPDATED_USER_ENTITY
        );

        assertTrue(updUserOptional.isEmpty());
    }

    @Test
    void getUsers() {
        assertTrue(usersRepository.getUsers().isEmpty());

        usersRepository.addUser(utc.VALID_USER_ENTITY);
        usersRepository.addUser(utc.VALID_USER_ENTITY);
        usersRepository.addUser(utc.VALID_USER_ENTITY);

        List<Users> users = usersRepository.getUsers();

        assertEquals(utc.VALID_USER_ENTITY_LIST.size(), users.size());
        assertEquals(utc.VALID_USER_ENTITY_LIST, users);
    }

    @Test
    void deleteUserById() {
        Long id = usersRepository.addUser(utc.VALID_USER_ENTITY);
        usersRepository.deleteUserById(id);

        assertTrue(usersRepository.getUsers().isEmpty());
    }

    @Test
    void userExistsById() {
        assertFalse(usersRepository.userExistsById(utc.NOT_EXISTED_USER_ID));

        Long id = usersRepository.addUser(utc.VALID_USER_ENTITY);

        assertTrue(usersRepository.userExistsById(id));
    }

    @Test
    void truncateAndRestartIdentity() {
        usersRepository.addUser(utc.VALID_USER_ENTITY);
        usersRepository.truncateAndRestartIdentity();

        assertTrue(usersRepository.getUsers().isEmpty());

        Long id = usersRepository.addUser(utc.VALID_USER_ENTITY);

        assertEquals(1, id);
    }

    @AfterEach
    void cleanUpDb() {
        usersRepository.truncateAndRestartIdentity();
    }
}