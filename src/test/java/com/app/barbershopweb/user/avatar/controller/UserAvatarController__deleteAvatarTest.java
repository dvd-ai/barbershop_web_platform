package com.app.barbershopweb.user.avatar.controller;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.app.barbershopweb.user.avatar.UserAvatarController;
import com.app.barbershopweb.user.avatar.UserAvatarService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.app.barbershopweb.user.avatar.constants.UserAvatar_Metadata__TestConstants.USER_AVATARS_URL;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_VALID_USER_ID;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserAvatarController.class)
class UserAvatarController__deleteAvatarTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserAvatarService avatarService;

    @Test
    @DisplayName("when AmazonServiceException, returns 500 & error dto")
    void removeAvatar__AmazonServiceException() throws Exception {
        doThrow(new AmazonServiceException(""))
                .when(avatarService).deleteProfileAvatar(USERS_VALID_USER_ID);

        mockMvc.perform(delete(USER_AVATARS_URL + "/" + USERS_VALID_USER_ID))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
        ;
    }

    @Test
    @DisplayName("when SdkClientException, returns 500 & error dto")
    void removeAvatar__SdkClientException() throws Exception {
        doThrow(new SdkClientException(""))
                .when(avatarService).deleteProfileAvatar(USERS_VALID_USER_ID);

        mockMvc.perform(delete(USER_AVATARS_URL + "/" + USERS_VALID_USER_ID))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
        ;
    }

    @Test
    @DisplayName("when user doesn't exist / exists, it removes avatar if needed and returns 200")
    void removeAvatar() throws Exception {
        mockMvc.perform(delete(USER_AVATARS_URL + "/" + USERS_VALID_USER_ID))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

}
