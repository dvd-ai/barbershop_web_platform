package com.app.barbershopweb.user.crud.controller;

import com.app.barbershopweb.user.crud.UserController;
import com.app.barbershopweb.user.crud.UserConverter;
import com.app.barbershopweb.user.crud.UserService;
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

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.app.barbershopweb.user.crud.constants.UserDto__TestConstants.USERS_USER_DTO_NOT_EXISTED_ID;
import static com.app.barbershopweb.user.crud.constants.UserDto__TestConstants.USERS_VALID_USER_DTO;
import static com.app.barbershopweb.user.crud.constants.UserEntity__TestConstants.USER_ENTITY_NOT_EXISTED_ID;
import static com.app.barbershopweb.user.crud.constants.UserEntity__TestConstants.USER_VALID_ENTITY;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@DisplayName("Testing PUT: " + USERS_URL)
@ExtendWith(MockitoExtension.class)
class UserControllerUpdateUserTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserConverter userConverter;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @DisplayName("when entity with 'id' in userDto doesn't exist " +
            "returns status code 404 and error dto")
    void whenNotFoundUserId() throws Exception {
        String json = objectMapper.writeValueAsString(
                USERS_USER_DTO_NOT_EXISTED_ID
        );

        when(userConverter.mapToEntity(USERS_USER_DTO_NOT_EXISTED_ID)).thenReturn(
                USER_ENTITY_NOT_EXISTED_ID
        );
        when(userService.updateUser(USER_ENTITY_NOT_EXISTED_ID)).thenReturn(Optional.empty());

        mockMvc
                .perform(put(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(
                        "User with id '" + USERS_NOT_EXISTING_USER_ID + "' not found."
                )))
                .andExpect(jsonPath("$", aMapWithSize(1)));
    }

    @Test
    @DisplayName("should return updated user (dto)")
    void shouldReturnUpdatedUser() throws Exception {
        String json = objectMapper.writeValueAsString(
                USERS_VALID_USER_DTO
        );

        when(userConverter.mapToEntity(USERS_VALID_USER_DTO)).thenReturn(
                USER_VALID_ENTITY
        );
        when(userService.updateUser(USER_VALID_ENTITY)).thenReturn(
                Optional.of(USER_VALID_ENTITY)
        );
        when(userConverter.mapToDto(USER_VALID_ENTITY)).thenReturn(
                USERS_VALID_USER_DTO
        );

        mockMvc
                .perform(put(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(USERS_VALID_USER_DTO.id().intValue())))
                .andExpect(jsonPath("$.firstName", is(USERS_VALID_USER_DTO.firstName())))
                .andExpect(jsonPath("$.lastName", is(USERS_VALID_USER_DTO.lastName())))
                .andExpect(jsonPath("$.phoneNumber", is(USERS_VALID_USER_DTO.phoneNumber())))
                .andExpect(jsonPath("$.email", is(USERS_VALID_USER_DTO.email())))
                .andExpect(jsonPath("$.role", is(USERS_VALID_USER_DTO.role())))
                .andExpect(jsonPath("$.registrationDate", is(USERS_VALID_USER_DTO.registrationDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$", aMapWithSize(USERS_FIELD_AMOUNT)));

    }
}
