package com.app.barbershopweb.user.avatar.service;

import com.app.barbershopweb.aws.s3.S3Service;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.user.avatar.UserAvatarService;
import com.app.barbershopweb.user.crud.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;

import static com.app.barbershopweb.aws.s3.constants.S3Service_Metadata__TestConstants.S3_SERVICE_BUCKET_NAME;
import static com.app.barbershopweb.user.avatar.constants.UserAvatar_Metadata__TestConstants.USERS_AVATAR_IMAGE_MOCK;
import static com.app.barbershopweb.user.avatar.constants.UserAvatar_Metadata__TestConstants.USER_AVATAR_S3_KEY;
import static com.app.barbershopweb.user.crud.constants.UserErrorMessage__TestConstants.*;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_NOT_EXISTED_USER_ID;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_VALID_USER_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAvatarServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    S3Service s3Service;

    @InjectMocks
    UserAvatarService userAvatarService;


    @Test
    @DisplayName("uploads profile avatar successfully")
    void uploadProfileAvatar() {
        when(userRepository.userExistsById(USERS_VALID_USER_ID)).thenReturn(true);
        userAvatarService.uploadProfileAvatar(USERS_VALID_USER_ID, USERS_AVATAR_IMAGE_MOCK);

        verify(s3Service, times(1)).deleteFile(
                S3_SERVICE_BUCKET_NAME, USER_AVATAR_S3_KEY);
        verify(s3Service, times(1)).uploadFile(
                S3_SERVICE_BUCKET_NAME, USER_AVATAR_S3_KEY,
                USERS_AVATAR_IMAGE_MOCK
        );
        assertDoesNotThrow(() -> userAvatarService.uploadProfileAvatar(USERS_VALID_USER_ID, USERS_AVATAR_IMAGE_MOCK));
    }

    @Test
    @DisplayName("throws NotFoundException if user doesn't exist")
    void uploadProfileAvatar__UserNotExist() throws NotFoundException {
        when(userRepository.userExistsById(USERS_NOT_EXISTED_USER_ID)).thenReturn(false);

        NotFoundException thrown = assertThrows(NotFoundException.class,
                () ->
                        userAvatarService.uploadProfileAvatar(
                                USERS_NOT_EXISTED_USER_ID, USERS_AVATAR_IMAGE_MOCK
                        )
        );

        assertEquals(1, thrown.getMessages().size());
        assertEquals(USER_ERR_NOT_EXISTED_USER_ID, thrown.getMessages().get(0));

        try {
            userAvatarService.uploadProfileAvatar(USERS_NOT_EXISTED_USER_ID, USERS_AVATAR_IMAGE_MOCK);
        } catch (NotFoundException ex) {
            verify(s3Service, times(0)).deleteFile(
                    S3_SERVICE_BUCKET_NAME,
                    USER_AVATAR_S3_KEY
            );
            verify(s3Service, times(0)).uploadFile(
                    USER_AVATAR_S3_KEY, S3_SERVICE_BUCKET_NAME,
                    USERS_AVATAR_IMAGE_MOCK
            );
        }
    }

    @Test
    void downloadProfileAvatar() throws IOException {
        when(userRepository.userExistsById(USERS_VALID_USER_ID)).thenReturn(true);
        when(s3Service.downloadFile(S3_SERVICE_BUCKET_NAME, USER_AVATAR_S3_KEY)).thenReturn(
                USERS_AVATAR_IMAGE_MOCK.getBytes()
        );

        ByteArrayResource avatar = userAvatarService.downloadProfileAvatar(USERS_VALID_USER_ID);
        assertEquals(USERS_AVATAR_IMAGE_MOCK.getBytes(), avatar.getByteArray());
    }

    @Test
    @DisplayName("throws NotFoundException if user doesn't exist")
    void downloadProfileAvatar__UserNotExist() {
        when(userRepository.userExistsById(USERS_NOT_EXISTED_USER_ID)).thenReturn(false);

        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> userAvatarService.downloadProfileAvatar(USERS_NOT_EXISTED_USER_ID)
        );

        assertEquals(1, thrown.getMessages().size());
        assertEquals(USER_ERR_NOT_EXISTED_USER_ID, thrown.getMessages().get(0));

        try {
            userAvatarService.downloadProfileAvatar(USERS_NOT_EXISTED_USER_ID);
        } catch (NotFoundException ex) {
            verify(s3Service, times(0)).downloadFile(
                    S3_SERVICE_BUCKET_NAME, USER_AVATAR_S3_KEY
            );
        }
    }

    @Test
    @DisplayName("throws NotFoundException if user doesn't exist")
    void deleteProfileAvatar__UserNotExist() {
        when(userRepository.userExistsById(USERS_NOT_EXISTED_USER_ID)).thenReturn(false);

        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> userAvatarService.deleteProfileAvatar(USERS_NOT_EXISTED_USER_ID)
        );

        assertEquals(1, thrown.getMessages().size());
        assertEquals(USER_ERR_NOT_EXISTED_USER_ID, thrown.getMessages().get(0));

        try {
            userAvatarService.deleteProfileAvatar(USERS_NOT_EXISTED_USER_ID);
        } catch (NotFoundException ex) {
            verify(s3Service, times(0)).downloadFile(
                    S3_SERVICE_BUCKET_NAME, USER_AVATAR_S3_KEY
            );
        }
    }

    @Test
    @DisplayName("deletes profile avatar successfully")
    void deleteProfileAvatar() {
        when(userRepository.userExistsById(USERS_VALID_USER_ID)).thenReturn(true);
        userAvatarService.deleteProfileAvatar(USERS_VALID_USER_ID);

        verify(s3Service, times(1)).deleteFile(
                S3_SERVICE_BUCKET_NAME, USER_AVATAR_S3_KEY);
        assertDoesNotThrow(() -> userAvatarService.deleteProfileAvatar(USERS_VALID_USER_ID));
    }

}