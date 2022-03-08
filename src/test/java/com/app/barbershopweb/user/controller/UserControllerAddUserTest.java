package com.app.barbershopweb.user.controller;

import com.app.barbershopweb.user.UserController;
import com.app.barbershopweb.user.UserConverter;
import com.app.barbershopweb.user.UserService;
import com.app.barbershopweb.user.UserTestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.app.barbershopweb.user.UserTestConstants.USERS_URL;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@DisplayName("Testing POST: " + USERS_URL)
class UserControllerAddUserTest {
    
    @Autowired
    MockMvc mockMvc;
    
    @MockBean
    UserService userService;
    
    @MockBean
    UserConverter userConverter;
    
    UserTestConstants utc = new UserTestConstants();
    
    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("when user dto isn't valid " +
            "returns status code 400 & error dto")
    @Test
    void whenUserDtoNotValid() throws Exception {
        String json = objectMapper.writeValueAsString(
                utc.INVALID_USER_DTO
        );

        mockMvc
                .perform(post(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors", hasItem(utc.DTO_CV_ID_ERR_MSG)))
                .andExpect(jsonPath("$.errors", hasItem(utc.DTO_CV_PHONE_NUMBER_ERR_MSG)))
                .andExpect(jsonPath("$.errors", hasItem(utc.DTO_CV_LAST_NAME_ERR_MSG)));
    }

    @DisplayName("after saving user entity returns its id and status code 201")
    @Test
    void shouldAddUser() throws Exception {
        String json = objectMapper.writeValueAsString(
                utc.VALID_USER_DTO
        );

        when(userConverter.mapToEntity(any())).thenReturn(
                utc.VALID_USER_ENTITY
        );
        when(userService.addUser(any())).thenReturn(
                utc.VALID_USER_ID
        );

        mockMvc
                .perform(post(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(String.valueOf(utc.VALID_USER_ID)));
    }
    
}
