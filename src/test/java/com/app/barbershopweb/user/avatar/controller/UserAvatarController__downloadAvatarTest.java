package com.app.barbershopweb.user.avatar.controller;

import com.app.barbershopweb.exception.MinioClientException;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.user.avatar.UserAvatarController;
import com.app.barbershopweb.user.avatar.UserAvatarService;
import com.app.barbershopweb.user.avatar.validator.AvatarImageValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.app.barbershopweb.user.avatar.constants.UserAvatar_ErrorMessage__TestConstants.USER_AVATAR_ERR_NO_AVATAR_FOUND;
import static com.app.barbershopweb.user.avatar.constants.UserAvatar_Metadata__TestConstants.USERS_AVATAR_IMAGE_MOCK;
import static com.app.barbershopweb.user.avatar.constants.UserAvatar_Metadata__TestConstants.USER_AVATARS_URL;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_VALID_USER_ID;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserAvatarController.class)
class UserAvatarController__downloadAvatarTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserAvatarService avatarService;

    @MockBean
    AvatarImageValidator imageValidator;

    @Test
    @DisplayName("when there's no profile avatar returns 404 & error dto")
    void downloadAvatar__NoAvatar() throws Exception {
        NotFoundException notFoundException = new NotFoundException(
                List.of(
                        "No profile avatar for user with id " + USERS_VALID_USER_ID
                )
        );

        when(avatarService.downloadProfileAvatar(USERS_VALID_USER_ID))
                .thenThrow(notFoundException);

        mockMvc.perform(get(USER_AVATARS_URL + "/" + USERS_VALID_USER_ID)).andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(USER_AVATAR_ERR_NO_AVATAR_FOUND)));
    }

    @Test
    @DisplayName("returns file")
    void downloadAvatar() throws Exception {
        when(avatarService.downloadProfileAvatar(USERS_VALID_USER_ID))
                .thenReturn(USERS_AVATAR_IMAGE_MOCK.getBytes());

        MockHttpServletResponse response = mockMvc.perform(get(USER_AVATARS_URL + "/" + USERS_VALID_USER_ID)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());
        assertArrayEquals(USERS_AVATAR_IMAGE_MOCK.getBytes(), response.getContentAsByteArray());
        assertEquals("application/octet-stream", response.getContentType());
    }

    @Test
    @DisplayName("when MinioClientException, returns 500 & error dto")
    void downloadAvatar__MinioClientException() throws Exception {
        when(avatarService.downloadProfileAvatar(USERS_VALID_USER_ID))
                .thenThrow(
                        new MinioClientException(anyString())
                );
        mockMvc.perform(get(USER_AVATARS_URL + "/" + USERS_VALID_USER_ID)).andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
        ;
    }

}
