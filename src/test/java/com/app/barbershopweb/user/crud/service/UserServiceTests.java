package com.app.barbershopweb.user.crud.service;

import com.app.barbershopweb.aws.s3.S3Service;
import com.app.barbershopweb.user.crud.UserService;
import com.app.barbershopweb.user.crud.Users;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    UserRepository userRepository;

    @Mock
    S3Service s3Service;

    @InjectMocks
    UserService userService;


    @Test
    void addUser() {
        when(userRepository.addUser(UserEntity__TestConstants.USERS_VALID_ENTITY))
                .thenReturn(UserMetadata__TestConstants.USERS_VALID_USER_ID);

        Long id = userService.addUser(UserEntity__TestConstants.USERS_VALID_ENTITY);

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
        when(userRepository.updateUser(UserEntity__TestConstants.USERS_VALID_UPDATED_USER_ENTITY))
                .thenReturn(Optional.of(UserEntity__TestConstants.USERS_VALID_UPDATED_USER_ENTITY));

        Optional<Users> userUpdOptional = userService
                .updateUser(UserEntity__TestConstants.USERS_VALID_UPDATED_USER_ENTITY);

        assertTrue(userUpdOptional.isPresent());
        Assertions.assertEquals(UserEntity__TestConstants.USERS_VALID_UPDATED_USER_ENTITY, userUpdOptional.get());

    }

    @Test
    void findUserById() {
        when(userRepository.findUserById((UserMetadata__TestConstants.USERS_VALID_USER_ID)))
                .thenReturn(Optional.of(UserEntity__TestConstants.USERS_VALID_ENTITY));

        Optional<Users> foundUserOpt = userService
                .findUserById(UserMetadata__TestConstants.USERS_VALID_USER_ID);

        assertTrue(foundUserOpt.isPresent());
        Assertions.assertEquals(UserEntity__TestConstants.USERS_VALID_ENTITY, foundUserOpt.get());

    }

    @Test
    void getUsers() {

        when(userRepository.getUsers())
                .thenReturn(UserList__TestConstants.USERS_USER_VALID_ENTITY_LIST);

        List<Users> users = userService.getUsers();

        Assertions.assertEquals(UserList__TestConstants.USERS_USER_VALID_ENTITY_LIST.size(), users.size());
        Assertions.assertEquals(UserList__TestConstants.USERS_USER_VALID_ENTITY_LIST, users);
    }
}
