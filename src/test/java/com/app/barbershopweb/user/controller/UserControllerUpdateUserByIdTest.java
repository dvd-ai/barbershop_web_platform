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

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.app.barbershopweb.user.UserTestConstants.USERS_URL;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@DisplayName("Testing PUT: " + USERS_URL)
class UserControllerUpdateUserByIdTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserConverter userConverter;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    UserTestConstants utc = new UserTestConstants();


    @DisplayName("when user dto isn't valid " +
            "returns status code 400 & error dto")
    @Test
    void whenUserDtoNotValid() throws Exception {


        String json = objectMapper.writeValueAsString(
                utc.INVALID_USER_DTO
        );

        mockMvc
                .perform(put(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors", hasItem(utc.DTO_CV_ID_ERR_MSG)))
                .andExpect(jsonPath("$.errors", hasItem(utc.DTO_CV_PHONE_NUMBER_ERR_MSG)))
                .andExpect(jsonPath("$.errors", hasItem(utc.DTO_CV_LAST_NAME_ERR_MSG)));
    }


    @Test
    @DisplayName("when entity with 'id' in userDto doesn't exist " +
            "returns status code 404 and error dto")
    void whenNotFoundUserId() throws Exception {
        String json = objectMapper.writeValueAsString(
                utc.USER_DTO_NOT_EXISTED_ID
        );

        when(userConverter.mapToEntity(any())).thenReturn(
                utc.USER_ENTITY_NOT_EXISTED_ID
        );
        when(userService.updateUser(any())).thenReturn(Optional.empty());

        mockMvc
                .perform(put(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(
                        "User with id '" + utc.NOT_EXISTED_USER_ID + "' not found."
                )))
                .andExpect(jsonPath("$", aMapWithSize(1)));
    }

    @Test
    @DisplayName("should return updated user (dto)")
    void shouldReturnUpdatedUser() throws Exception {
        String json = objectMapper.writeValueAsString(
                utc.VALID_USER_DTO
        );

        when(userConverter.mapToEntity(any())).thenReturn(
                utc.VALID_USER_ENTITY
        );
        when(userService.updateUser(any())).thenReturn(
                Optional.of(utc.VALID_USER_ENTITY)
        );
        when(userConverter.mapToDto(any())).thenReturn(
                utc.VALID_USER_DTO
        );

        mockMvc
                .perform(put(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",  is(utc.VALID_USER_DTO.id().intValue())))
                .andExpect(jsonPath("$.firstName", is(utc.VALID_USER_DTO.firstName())))
                .andExpect(jsonPath("$.lastName", is(utc.VALID_USER_DTO.lastName())))
                .andExpect(jsonPath("$.phoneNumber", is(utc.VALID_USER_DTO.phoneNumber())))
                .andExpect(jsonPath("$.email", is(utc.VALID_USER_DTO.email())))
                .andExpect(jsonPath("$.role", is(utc.VALID_USER_DTO.role())))
                .andExpect(jsonPath("$.registrationDate", is(utc.VALID_USER_DTO.registrationDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$", aMapWithSize(utc.USERS_FIELD_AMOUNT)));

    }
}
