package com.app.barbershopweb.user.dto;

import com.app.barbershopweb.user.UserController;
import com.app.barbershopweb.user.UserConverter;
import com.app.barbershopweb.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.app.barbershopweb.user.constants.UserDto__TestConstants.USERS_INVALID_USER_DTO;
import static com.app.barbershopweb.user.constants.UserErrorMessage__TestConstants.*;
import static com.app.barbershopweb.user.constants.UserMetadata__TestConstants.USERS_URL;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UsersDtoTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    UserConverter userConverter;


    @Autowired
    ObjectMapper objectMapper;

    String json;


    @DisplayName(
            "Testing POST: " + USERS_URL +
                    " when user dto isn't valid " +
                    "returns status code 400 & error dto")
    @Test
    void addUser__whenUserDtoNotValid() throws Exception {
        json = objectMapper.writeValueAsString(
                USERS_INVALID_USER_DTO
        );

        mockMvc
                .perform(post(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors", hasItem(USER_ERR_INVALID_DTO_ID)))
                .andExpect(jsonPath("$.errors", hasItem(USER_ERR_INVALID_DTO_PHONE_NUMBER)))
                .andExpect(jsonPath("$.errors", hasItem(USER_ERR_INVALID_DTO_LAST_NAME)));
    }

    @DisplayName(
            "Testing PUT: " + USERS_URL +
                    "when user dto isn't valid " +
                    "returns status code 400 & error dto")
    @Test
    void updateUser__whenUserDtoNotValid() throws Exception {

        json = objectMapper.writeValueAsString(
                USERS_INVALID_USER_DTO
        );

        mockMvc
                .perform(put(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors", hasItem(USER_ERR_INVALID_DTO_ID)))
                .andExpect(jsonPath("$.errors", hasItem(USER_ERR_INVALID_DTO_PHONE_NUMBER)))
                .andExpect(jsonPath("$.errors", hasItem(USER_ERR_INVALID_DTO_LAST_NAME)));
    }

}