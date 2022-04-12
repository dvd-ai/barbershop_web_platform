package com.app.barbershopweb.user.avatar.controller;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.app.barbershopweb.exception.FileException;
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

import static com.app.barbershopweb.user.avatar.constants.UserAvatar_Metadata__TestConstants.*;
import static com.app.barbershopweb.user.crud.constants.UserErrorMessage__TestConstants.USER_ERR_NOT_EXISTING_USER_ID;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.*;
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
    @DisplayName("when FileException, returns 500 & error dto")
    void uploadAvatar__FileException() throws Exception {
        doThrow(new FileException(List.of("")))
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
    @DisplayName("when AmazonServiceException, returns 500 & error dto")
    void uploadAvatar__AmazonServiceException() throws Exception {
        doThrow(new AmazonServiceException(""))
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
    @DisplayName("when SdkClientException, returns 500 & error dto")
    void uploadAvatar__SdkClientException() throws Exception {
        doThrow(new SdkClientException(""))
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
    @DisplayName("uploads avatar")
    void uploadAvatar() throws Exception {
        mockMvc.perform(multipart(USER_AVATARS_URL + "/" + USERS_VALID_USER_ID)
                        .file(USERS_AVATAR_IMAGE_MOCK))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }
}