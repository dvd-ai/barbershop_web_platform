package com.app.barbershopweb.user.crud.service;

import com.app.barbershopweb.minio.MinioService;
import com.app.barbershopweb.user.crud.User;
import com.app.barbershopweb.user.crud.UserService;
import com.app.barbershopweb.user.crud.constants.UserEntity__TestConstants;
import com.app.barbershopweb.user.crud.constants.UserList__TestConstants;
import com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants;
import com.app.barbershopweb.user.crud.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    UserRepository userRepository;

    @Mock
    MinioService minioService;

    @InjectMocks
    UserService userService;


    @Test
    void addUser() {
        when(userRepository.addUser(UserEntity__TestConstants.USER_VALID_ENTITY))
                .thenReturn(UserMetadata__TestConstants.USERS_VALID_USER_ID);

        Long id = userService.addUser(UserEntity__TestConstants.USER_VALID_ENTITY);

        Assertions.assertEquals(UserMetadata__TestConstants.USERS_VALID_USER_ID, id);
    }

    @Test
    void deleteUserById() {
        userService.deleteUserById(UserMetadata__TestConstants.USERS_VALID_USER_ID);

        verify(userRepository, times(1))
                .deleteUserById(UserMetadata__TestConstants.USERS_VALID_USER_ID);
    }

    @Test
    void updateUser() {
        when(userRepository.updateUser(UserEntity__TestConstants.USER_VALID_UPDATED_USER_ENTITY))
                .thenReturn(Optional.of(UserEntity__TestConstants.USER_VALID_UPDATED_USER_ENTITY));

        Optional<User> userUpdOptional = userService
                .updateUser(UserEntity__TestConstants.USER_VALID_UPDATED_USER_ENTITY);

        assertTrue(userUpdOptional.isPresent());
        Assertions.assertEquals(UserEntity__TestConstants.USER_VALID_UPDATED_USER_ENTITY, userUpdOptional.get());

    }

    @Test
    void findUserById() {
        when(userRepository.findUserById((UserMetadata__TestConstants.USERS_VALID_USER_ID)))
                .thenReturn(Optional.of(UserEntity__TestConstants.USER_VALID_ENTITY));

        Optional<User> foundUserOpt = userService
                .findUserById(UserMetadata__TestConstants.USERS_VALID_USER_ID);

        assertTrue(foundUserOpt.isPresent());
        Assertions.assertEquals(UserEntity__TestConstants.USER_VALID_ENTITY, foundUserOpt.get());

    }

    @Test
    void getUsers() {

        when(userRepository.getUsers())
                .thenReturn(UserList__TestConstants.USER_USER_VALID_ENTITY_LIST);

        List<User> users = userService.getUsers();

        Assertions.assertEquals(UserList__TestConstants.USER_USER_VALID_ENTITY_LIST.size(), users.size());
        Assertions.assertEquals(UserList__TestConstants.USER_USER_VALID_ENTITY_LIST, users);
    }
}
