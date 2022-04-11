package com.app.barbershopweb.user.service;

import com.app.barbershopweb.aws.s3.S3Service;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.user.UserService;
import com.app.barbershopweb.user.Users;
import com.app.barbershopweb.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static com.app.barbershopweb.user.constants.UserEntity__TestConstants.USERS_VALID_ENTITY;
import static com.app.barbershopweb.user.constants.UserEntity__TestConstants.USERS_VALID_UPDATED_USER_ENTITY;
import static com.app.barbershopweb.user.constants.UserErrorMessage__TestConstants.USER_ERR_FILE_DOWNLOAD_USER_ID;
import static com.app.barbershopweb.user.constants.UserErrorMessage__TestConstants.USER_ERR_FILE_UPLOAD_USER_ID;
import static com.app.barbershopweb.user.constants.UserList__TestConstants.USERS_USER_VALID_ENTITY_LIST;
import static com.app.barbershopweb.user.constants.UserMetadata__TestConstants.*;
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

    @Test
    @DisplayName("uploads profile avatar successfully")
    void uploadProfileAvatar() {
        when(userRepository.userExistsById(USERS_VALID_USER_ID)).thenReturn(true);
        userService.uploadProfileAvatar(USERS_VALID_USER_ID, USERS_AVATAR_IMAGE_MOCK);

        verify(s3Service, times(1)).deleteFile(USERS_S3_KEY);
        verify(s3Service, times(1)).uploadFile(
                USERS_S3_KEY, USERS_AVATAR_IMAGE_MOCK
        );
        assertDoesNotThrow(() -> userService.uploadProfileAvatar(USERS_VALID_USER_ID, USERS_AVATAR_IMAGE_MOCK));
    }

    @Test
    @DisplayName("throws NotFoundException if user doesn't exist")
    void uploadProfileAvatarUserNotExist() throws NotFoundException{
        when(userRepository.userExistsById(USERS_VALID_USER_ID)).thenReturn(false);

        NotFoundException thrown = assertThrows(NotFoundException.class,
                () ->
                        userService.uploadProfileAvatar(
                                USERS_VALID_USER_ID, USERS_AVATAR_IMAGE_MOCK
                        )
        );

        assertEquals(1, thrown.getMessages().size());
        assertEquals(USER_ERR_FILE_UPLOAD_USER_ID, thrown.getMessages().get(0));

        try {
            userService.uploadProfileAvatar(USERS_VALID_USER_ID, USERS_AVATAR_IMAGE_MOCK);
        }
        catch (NotFoundException ex){
            verify(s3Service, times(0)).deleteFile(USERS_S3_KEY);
            verify(s3Service, times(0)).uploadFile(
                    USERS_S3_KEY, USERS_AVATAR_IMAGE_MOCK
            );
        }
    }

    @Test
    void downloadProfileAvatar() {
        when(userRepository.userExistsById(USERS_VALID_USER_ID)).thenReturn(true);
        when(s3Service.downloadFile(USERS_S3_KEY)).thenReturn(USERS_AVATAR_IMAGE_MOCK);

        MultipartFile avatar = userService.downloadProfileAvatar(USERS_VALID_USER_ID);

        assertEquals(USERS_AVATAR_IMAGE_MOCK, avatar);
    }

    @Test
    @DisplayName("throws NotFoundException if user doesn't exist")
    void downloadProfileAvatarUserNotExist() {
        when(userRepository.userExistsById(USERS_VALID_USER_ID)).thenReturn(false);

        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> userService.downloadProfileAvatar(USERS_VALID_USER_ID)
        );

        assertEquals(1, thrown.getMessages().size());
        assertEquals(USER_ERR_FILE_DOWNLOAD_USER_ID, thrown.getMessages().get(0));

        try {
            userService.downloadProfileAvatar(USERS_VALID_USER_ID);
        }
        catch (NotFoundException ex){
            verify(s3Service, times(0)).downloadFile(
                    USERS_S3_KEY
            );
        }
    }
}
