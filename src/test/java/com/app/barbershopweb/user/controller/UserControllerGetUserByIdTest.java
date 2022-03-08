package com.app.barbershopweb.user.controller;

import com.app.barbershopweb.user.UserController;
import com.app.barbershopweb.user.UserConverter;
import com.app.barbershopweb.user.UserService;
import com.app.barbershopweb.user.UserTestConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.app.barbershopweb.user.UserTestConstants.USERS_URL;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@DisplayName("Testing GET: " + USERS_URL + "/" + "{userId}")
class UserControllerGetUserByIdTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    UserConverter userConverter;

    UserTestConstants utc = new UserTestConstants();

    @DisplayName("When path variable input 'userId' isn't valid" +
            " returns status code 400 (BAD_REQUEST) & error dto")
    @Test
    void whenUserIdNotValid() throws Exception {
        mockMvc
                .perform(get(USERS_URL + "/" + utc.INVALID_USER_ID))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(utc.PV_USER_ID_ERR_MSG)));
    }

    @DisplayName("when there's no user with id 'userId' " +
            "returns status code 404 & error dto")
    @Test
    void whenNotExistedUserId() throws Exception {
        mockMvc
                .perform(get(USERS_URL + "/" + utc.NOT_EXISTED_USER_ID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(
                        "Users with id '" + utc.NOT_EXISTED_USER_ID + "' not found.")
                ));
    }

    @DisplayName("returns corresponding user dto")
    @Test
    void shouldReturnUser() throws Exception {
        when(userService.findUserById(any())).thenReturn(
                Optional.of(utc.VALID_USER_ENTITY)
        );
        when(userConverter.mapToDto(any())).thenReturn(
                utc.VALID_USER_DTO
        );

        mockMvc
                .perform(get(USERS_URL+ "/" + utc.VALID_USER_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",  is(utc.VALID_USER_DTO.id().intValue())))
                .andExpect(jsonPath("$.firstName", is(utc.VALID_USER_DTO.firstName())))
                .andExpect(jsonPath("$.lastName", is(utc.VALID_USER_DTO.lastName())))
                .andExpect(jsonPath("$.phoneNumber", is(utc.VALID_USER_DTO.phoneNumber())))
                .andExpect(jsonPath("$.email", is(utc.VALID_USER_DTO.email())))
                .andExpect(jsonPath("$.role", is(utc.VALID_USER_DTO.role())))
                .andExpect(jsonPath("$.registrationDate",
                        is(utc.VALID_USER_DTO.registrationDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                )

                .andExpect(jsonPath("$", aMapWithSize(utc.USERS_FIELD_AMOUNT)));
    }
}
