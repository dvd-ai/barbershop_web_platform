package com.app.barbershopweb.user.avatar.controller;

import com.app.barbershopweb.exception.MinioClientException;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.user.avatar.UserAvatarController;
import com.app.barbershopweb.user.avatar.UserAvatarService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.app.barbershopweb.user.avatar.constants.UserAvatar_ErrorMessage__TestConstants.*;
import static com.app.barbershopweb.user.avatar.constants.UserAvatar_Metadata__TestConstants.*;
import static com.app.barbershopweb.user.crud.constants.UserErrorMessage__TestConstants.USER_ERR_NOT_EXISTING_USER_ID;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_NOT_EXISTING_USER_ID;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_VALID_USER_ID;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserAvatarController.class)
class UserAvatarController__uploadAvatarTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserAvatarService avatarService;

    @Test
    @DisplayName("when user doesn't exist, returns 404 & error dto")
    void uploadAvatar__UserNotExist() throws Exception {
        doThrow(new NotFoundException(List.of(USER_ERR_NOT_EXISTING_USER_ID)))
                .when(avatarService).uploadProfileAvatar(USERS_NOT_EXISTING_USER_ID, USERS_AVATAR_IMAGE_MOCK);

        mockMvc.perform(multipart(USER_AVATARS_URL + "/" + USERS_NOT_EXISTING_USER_ID)
                        .file(USERS_AVATAR_IMAGE_MOCK))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(USER_ERR_NOT_EXISTING_USER_ID)))
        ;
    }

    @Test
    @DisplayName("when MinioClientException, returns 500 & error dto")
    void uploadAvatar__MinioClientException() throws Exception {
        doThrow(new MinioClientException(""))
                .when(avatarService).uploadProfileAvatar(USERS_VALID_USER_ID, USERS_AVATAR_IMAGE_MOCK);

        mockMvc.perform(multipart(USER_AVATARS_URL + "/" + USERS_VALID_USER_ID)
                        .file(USERS_AVATAR_IMAGE_MOCK))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
        ;
    }

    @Test
    @DisplayName("when upload size exceeds maximum - returns 400 and error dto")
    void uploadAvatar__uploadLimitExceeded() throws Exception {
        mockMvc.perform(multipart(USER_AVATARS_URL + "/" + USERS_VALID_USER_ID)
                        .file(USERS_AVATAR_FILE_SIZE_LIMIT_MOCK))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(USER_AVATAR_ERR_FILE_SIZE)))
        ;
    }

    @Test
    @DisplayName("when invalid content type - returns 400 and error dto")
    void uploadAvatar__invalidContentType() throws Exception {
        mockMvc.perform(multipart(USER_AVATARS_URL + "/" + USERS_VALID_USER_ID)
                        .file(USERS_AVATAR_TEXT_FILE_MOCK))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(USER_AVATAR_ERR_INVALID_FILE)))
        ;
    }

    @Test
    @DisplayName("when no file content - returns 400 and error dto")
    void uploadAvatar__noFileContent() throws Exception {
        mockMvc.perform(multipart(USER_AVATARS_URL + "/" + USERS_VALID_USER_ID)
                        .file(USERS_AVATAR_NO_FILE_CONTENT_MOCK))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(USER_AVATAR_ERR_EMPTY_FILE)))
        ;
    }

    @Test
    @DisplayName("uploads avatar")
    void uploadAvatar() throws Exception {
        mockMvc.perform(multipart(USER_AVATARS_URL + "/" + USERS_VALID_USER_ID)
                        .file(USERS_AVATAR_IMAGE_MOCK))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }
}