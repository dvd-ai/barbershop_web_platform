package com.app.barbershopweb.user.controller;

import com.app.barbershopweb.user.UserController;
import com.app.barbershopweb.user.UserConverter;
import com.app.barbershopweb.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.app.barbershopweb.user.constants.UserDto__TestConstants.USERS_VALID_USER_DTO;
import static com.app.barbershopweb.user.constants.UserEntity__TestConstants.USERS_VALID_ENTITY;
import static com.app.barbershopweb.user.constants.UserMetadata__TestConstants.USERS_URL;
import static com.app.barbershopweb.user.constants.UserMetadata__TestConstants.USERS_VALID_USER_ID;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@DisplayName("Testing POST: " + USERS_URL)
@ExtendWith(MockitoExtension.class)
class UserControllerAddUserTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    UserConverter userConverter;


    @Autowired
    ObjectMapper objectMapper;


    @DisplayName("after saving user entity returns its id and status code 201")
    @Test
    void shouldAddUser() throws Exception {
        String json = objectMapper.writeValueAsString(
                USERS_VALID_USER_DTO
        );

        when(userConverter.mapToEntity(USERS_VALID_USER_DTO)).thenReturn(
                USERS_VALID_ENTITY
        );
        when(userService.addUser(USERS_VALID_ENTITY)).thenReturn(
                USERS_VALID_USER_ID
        );

        mockMvc
                .perform(post(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(String.valueOf(USERS_VALID_USER_ID)));
    }

}
