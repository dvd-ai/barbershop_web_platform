package com.app.barbershopweb.user.avatar.controller;

import com.app.barbershopweb.exception.MinioClientException;
import com.app.barbershopweb.exception.ValidationException;
import com.app.barbershopweb.user.avatar.UserAvatarController;
import com.app.barbershopweb.user.avatar.UserAvatarService;
import com.app.barbershopweb.user.avatar.validator.AvatarImageValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.app.barbershopweb.user.avatar.constants.UserAvatar_ErrorMessage__TestConstants.*;
import static com.app.barbershopweb.user.avatar.constants.UserAvatar_Metadata__TestConstants.*;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_VALID_USER_ID;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserAvatarController.class)
@MockBean(AuthenticationProvider.class)
class UserAvatarController__uploadAvatarTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserAvatarService avatarService;

    @MockBean
    AvatarImageValidator imageValidator;


    @Test
    @DisplayName("when MinioClientException, returns 500 & error dto")
    @WithMockUser
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
    @WithMockUser
    void uploadAvatar__uploadLimitExceeded() throws Exception {
        ValidationException validationException = new ValidationException(
                List.of(
                        USER_AVATAR_ERR_FILE_SIZE
                )
        );

        doThrow(validationException).when(imageValidator).isValid(USERS_AVATAR_FILE_SIZE_LIMIT_MOCK);

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
    @WithMockUser
    void uploadAvatar__invalidContentType() throws Exception {
        ValidationException validationException = new ValidationException(
                List.of(
                        USER_AVATAR_ERR_INVALID_FILE
                )
        );

        doThrow(validationException).when(imageValidator).isValid(USERS_AVATAR_TEXT_FILE_MOCK);
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
    @WithMockUser
    void uploadAvatar__noFileContent() throws Exception {
        ValidationException validationException = new ValidationException(
                List.of(
                        USER_AVATAR_ERR_EMPTY_FILE
                )
        );

        doThrow(validationException).when(imageValidator).isValid(USERS_AVATAR_NO_FILE_CONTENT_MOCK);

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
    @WithMockUser
    void uploadAvatar() throws Exception {
        mockMvc.perform(multipart(USER_AVATARS_URL + "/" + USERS_VALID_USER_ID)
                        .file(USERS_AVATAR_IMAGE_MOCK))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }
}