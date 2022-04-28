package com.app.barbershopweb.user.avatar.service;

import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.minio.MinioService;
import com.app.barbershopweb.user.avatar.UserAvatarService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static com.app.barbershopweb.minio.constants.MinioService_Metadata__TestConstants.MINIO_SERVICE_BUCKET_NAME;
import static com.app.barbershopweb.user.avatar.constants.UserAvatar_ErrorMessage__TestConstants.USER_AVATAR_ERR_NO_AVATAR_FOUND;
import static com.app.barbershopweb.user.avatar.constants.UserAvatar_Metadata__TestConstants.USERS_AVATAR_IMAGE_MOCK;
import static com.app.barbershopweb.user.avatar.constants.UserAvatar_Metadata__TestConstants.USER_AVATAR_S3_KEY;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_VALID_USER_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAvatarServiceTest {

    MinioService minioService = Mockito.mock(MinioService.class);

    UserAvatarService userAvatarService = new UserAvatarService(
            minioService, MINIO_SERVICE_BUCKET_NAME
    );

    @Test
    @DisplayName("uploads profile avatar successfully")
    void uploadProfileAvatar() {
        userAvatarService.uploadProfileAvatar(USERS_VALID_USER_ID, USERS_AVATAR_IMAGE_MOCK);

        verify(minioService, times(1)).deleteFile(
                MINIO_SERVICE_BUCKET_NAME, USER_AVATAR_S3_KEY);
        verify(minioService, times(1)).uploadFile(
                MINIO_SERVICE_BUCKET_NAME, USER_AVATAR_S3_KEY,
                USERS_AVATAR_IMAGE_MOCK
        );
        assertDoesNotThrow(() -> userAvatarService.uploadProfileAvatar(USERS_VALID_USER_ID, USERS_AVATAR_IMAGE_MOCK));
    }

    @Test
    void downloadProfileAvatar() throws IOException {
        when(minioService.downloadFile(MINIO_SERVICE_BUCKET_NAME, USER_AVATAR_S3_KEY)).thenReturn(
                USERS_AVATAR_IMAGE_MOCK.getBytes()
        );

        byte[] avatar = userAvatarService.downloadProfileAvatar(USERS_VALID_USER_ID);
        assertEquals(USERS_AVATAR_IMAGE_MOCK.getBytes(), avatar);
    }

    @Test
    void downloadProfileAvatar__noImageContent() {
        when(minioService.downloadFile(MINIO_SERVICE_BUCKET_NAME, USER_AVATAR_S3_KEY)).thenReturn(
                new byte[0]
        );

        List<String> errorMessages = assertThrows(NotFoundException.class,
                () -> userAvatarService.downloadProfileAvatar(USERS_VALID_USER_ID)
        ).getMessages();

        assertEquals(1, errorMessages.size());
        assertEquals(USER_AVATAR_ERR_NO_AVATAR_FOUND, errorMessages.get(0));
    }

    @Test
    @DisplayName("deletes profile avatar successfully")
    void deleteProfileAvatar() {
        userAvatarService.deleteProfileAvatar(USERS_VALID_USER_ID);

        verify(minioService, times(1)).deleteFile(
                MINIO_SERVICE_BUCKET_NAME, USER_AVATAR_S3_KEY);
        assertDoesNotThrow(() -> userAvatarService.deleteProfileAvatar(USERS_VALID_USER_ID));
    }

}