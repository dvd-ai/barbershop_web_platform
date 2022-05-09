package com.app.barbershopweb.user.crud.controller;

import com.app.barbershopweb.user.crud.UserController;
import com.app.barbershopweb.user.crud.UserConverter;
import com.app.barbershopweb.user.crud.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.app.barbershopweb.user.crud.constants.UserDto__TestConstants.USERS_VALID_USER_DTO;
import static com.app.barbershopweb.user.crud.constants.UserEntity__TestConstants.USER_VALID_ENTITY;
import static com.app.barbershopweb.user.crud.constants.UserErrorMessage__TestConstants.USER_ERR_INVALID_PATH_VAR_USER_ID;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@DisplayName("Testing GET: " + USERS_URL + "/" + "{userId}")
@ExtendWith(MockitoExtension.class)
@MockBean(AuthenticationProvider.class)
class UserControllerGetUserByIdTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    UserConverter userConverter;


    @DisplayName("When path variable input 'userId' isn't valid" +
            " returns status code 400 (BAD_REQUEST) & error dto")
    @Test
    void whenUserIdNotValid() throws Exception {
        mockMvc
                .perform(get(USERS_URL + "/" + USERS_INVALID_USER_ID))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(USER_ERR_INVALID_PATH_VAR_USER_ID)));
    }

    @DisplayName("when there's no user with id 'userId' " +
            "returns status code 404 & error dto")
    @Test
    void whenNotExistedUserId() throws Exception {
        mockMvc
                .perform(get(USERS_URL + "/" + USERS_NOT_EXISTING_USER_ID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(
                        "User with id '" + USERS_NOT_EXISTING_USER_ID + "' not found.")
                ));
    }

    @DisplayName("returns corresponding user dto")
    @Test
    void shouldReturnUser() throws Exception {
        when(userService.findUserById(USERS_VALID_USER_ID)).thenReturn(
                Optional.of(USER_VALID_ENTITY)
        );
        when(userConverter.mapToDto(USER_VALID_ENTITY)).thenReturn(
                USERS_VALID_USER_DTO
        );

        mockMvc
                .perform(get(USERS_URL + "/" + USERS_VALID_USER_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(USERS_VALID_USER_DTO.id().intValue())))
                .andExpect(jsonPath("$.firstName", is(USERS_VALID_USER_DTO.firstName())))
                .andExpect(jsonPath("$.lastName", is(USERS_VALID_USER_DTO.lastName())))
                .andExpect(jsonPath("$.phoneNumber", is(USERS_VALID_USER_DTO.phoneNumber())))
                .andExpect(jsonPath("$.email", is(USERS_VALID_USER_DTO.email())))
                .andExpect(jsonPath("$.role", is(USERS_VALID_USER_DTO.role())))
                .andExpect(jsonPath("$.registrationDate",
                        is(USERS_VALID_USER_DTO.registrationDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                )

                .andExpect(jsonPath("$", aMapWithSize(USERS_FIELD_AMOUNT)));
    }
}
