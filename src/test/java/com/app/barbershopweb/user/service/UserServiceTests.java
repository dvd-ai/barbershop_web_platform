package com.app.barbershopweb.user.service;

import com.app.barbershopweb.user.UserService;
import com.app.barbershopweb.user.UserTestConstants;
import com.app.barbershopweb.user.Users;
import com.app.barbershopweb.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    UserTestConstants utc = new UserTestConstants();

    @Test
    void addUser() {
        when(userRepository.addUser(any()))
                .thenReturn(utc.VALID_USER_ID);

        Long id = userService.addUser(utc.VALID_USER_ENTITY);

        assertEquals(utc.VALID_USER_ID, id);
    }

    @Test
    void deleteUserById() {
        userService.deleteUserById(utc.VALID_USER_ID);

        verify(userRepository, times(1))
                .deleteUserById(any());
    }

    @Test
    void updateUser() {
        when(userRepository.updateUser(any()))
                .thenReturn(Optional.of(utc.VALID_UPDATED_USER_ENTITY));

        Optional<Users> userUpdOptional = userService
                .updateUser(utc.VALID_UPDATED_USER_ENTITY);

        assertTrue(userUpdOptional.isPresent());
        assertEquals(utc.VALID_UPDATED_USER_ENTITY, userUpdOptional.get());

    }

    @Test
    void findUserById() {
        when(userRepository.findUserById((any())))
                .thenReturn(Optional.of(utc.VALID_USER_ENTITY));

        Optional<Users> foundUserOpt = userService
                .findUserById(utc.VALID_USER_ID);

        assertTrue(foundUserOpt.isPresent());
        assertEquals(utc.VALID_USER_ENTITY, foundUserOpt.get());

    }

    @Test
    void getUsers() {

        when(userRepository.getUsers())
                .thenReturn(utc.VALID_USER_ENTITY_LIST);

        List<Users> users = userService.getUsers();

        assertEquals(utc.VALID_USER_ENTITY_LIST.size(), users.size());
        assertEquals(utc.VALID_USER_ENTITY_LIST, users);
    }
}
