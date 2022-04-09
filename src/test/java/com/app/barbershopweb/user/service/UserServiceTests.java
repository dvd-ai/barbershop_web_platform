package com.app.barbershopweb.user.service;

import com.app.barbershopweb.user.UserService;
import com.app.barbershopweb.user.Users;
import com.app.barbershopweb.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.app.barbershopweb.user.constants.UserEntity__TestConstants.USERS_VALID_ENTITY;
import static com.app.barbershopweb.user.constants.UserEntity__TestConstants.USERS_VALID_UPDATED_USER_ENTITY;
import static com.app.barbershopweb.user.constants.UserList__TestConstants.USERS_USER_VALID_ENTITY_LIST;
import static com.app.barbershopweb.user.constants.UserMetadata__TestConstants.USERS_VALID_USER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;


    @Test
    void addUser() {
        when(userRepository.addUser(USERS_VALID_ENTITY))
                .thenReturn(USERS_VALID_USER_ID);

        Long id = userService.addUser(USERS_VALID_ENTITY);

        assertEquals(USERS_VALID_USER_ID, id);
    }

    @Test
    void deleteUserById() {
        userService.deleteUserById(USERS_VALID_USER_ID);

        verify(userRepository, times(1))
                .deleteUserById(USERS_VALID_USER_ID);
    }

    @Test
    void updateUser() {
        when(userRepository.updateUser(USERS_VALID_UPDATED_USER_ENTITY))
                .thenReturn(Optional.of(USERS_VALID_UPDATED_USER_ENTITY));

        Optional<Users> userUpdOptional = userService
                .updateUser(USERS_VALID_UPDATED_USER_ENTITY);

        assertTrue(userUpdOptional.isPresent());
        assertEquals(USERS_VALID_UPDATED_USER_ENTITY, userUpdOptional.get());

    }

    @Test
    void findUserById() {
        when(userRepository.findUserById((USERS_VALID_USER_ID)))
                .thenReturn(Optional.of(USERS_VALID_ENTITY));

        Optional<Users> foundUserOpt = userService
                .findUserById(USERS_VALID_USER_ID);

        assertTrue(foundUserOpt.isPresent());
        assertEquals(USERS_VALID_ENTITY, foundUserOpt.get());

    }

    @Test
    void getUsers() {

        when(userRepository.getUsers())
                .thenReturn(USERS_USER_VALID_ENTITY_LIST);

        List<Users> users = userService.getUsers();

        assertEquals(USERS_USER_VALID_ENTITY_LIST.size(), users.size());
        assertEquals(USERS_USER_VALID_ENTITY_LIST, users);
    }
}
