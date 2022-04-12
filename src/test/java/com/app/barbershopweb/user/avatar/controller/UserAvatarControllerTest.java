package com.app.barbershopweb.user.avatar.controller;

import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.user.avatar.UserAvatarController;
import com.app.barbershopweb.user.avatar.UserAvatarService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.List;

import static com.app.barbershopweb.user.avatar.constants.UserAvatar_ErrorMessage__TestConstants.USER_AVATAR_ERR_NO_AVATAR_FOUND;
import static com.app.barbershopweb.user.avatar.constants.UserAvatar_Metadata__TestConstants.USERS_AVATAR_IMAGE_MOCK;
import static com.app.barbershopweb.user.avatar.constants.UserAvatar_Metadata__TestConstants.USER_AVATARS_URL;
import static com.app.barbershopweb.user.crud.constants.UserErrorMessage__TestConstants.USER_ERR_NOT_EXISTING_USER_ID;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_VALID_USER_ID;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserAvatarController.class)
class UserAvatarControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserAvatarService avatarService;



    @Test
    void uploadAvatar() {
    }

    @Test
    void removeAvatar() {
    }
}