package com.app.barbershopweb.user.avatar.validator;

import com.app.barbershopweb.user.avatar.UserAvatarController;
import com.app.barbershopweb.user.avatar.UserAvatarService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.app.barbershopweb.user.avatar.constants.UserAvatar_Metadata__TestConstants.USERS_AVATAR_IMAGE_MOCK;
import static com.app.barbershopweb.user.avatar.constants.UserAvatar_Metadata__TestConstants.USER_AVATARS_URL;
import static com.app.barbershopweb.user.crud.constants.UserErrorMessage__TestConstants.USER_ERR_INVALID_PATH_VAR_USER_ID;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_INVALID_USER_ID;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserAvatarController.class)
@MockBean(AuthenticationProvider.class)
class UserAvatarValidatorTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserAvatarService avatarService;

    @MockBean
    AvatarImageValidator imageValidator;

    @Test
    @DisplayName("when invalid path var returns 400 & error dto")
    @WithMockUser
    void downloadAvatar__InvalidPathVar() throws Exception {
        mockMvc.perform(get(USER_AVATARS_URL + "/" + USERS_INVALID_USER_ID))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(USER_ERR_INVALID_PATH_VAR_USER_ID)));
    }

    @Test
    @DisplayName("when invalid path var returns 400 & error dto")
    @WithMockUser
    void uploadAvatar__InvalidPathVar() throws Exception {
        mockMvc.perform(multipart(USER_AVATARS_URL + "/" + USERS_INVALID_USER_ID)
                        .file(USERS_AVATAR_IMAGE_MOCK))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(USER_ERR_INVALID_PATH_VAR_USER_ID)))
        ;
    }

    @Test
    @DisplayName("when no file returns 400")
    @WithMockUser
    void uploadAvatar__NoFile() throws Exception {
        mockMvc.perform(multipart(USER_AVATARS_URL + "/" + USERS_INVALID_USER_ID))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("when invalid path var returns 400 & error dto")
    @WithMockUser
    void removeAvatar() throws Exception {
        mockMvc.perform(delete(USER_AVATARS_URL + "/" + USERS_INVALID_USER_ID))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(USER_ERR_INVALID_PATH_VAR_USER_ID)));
    }
}