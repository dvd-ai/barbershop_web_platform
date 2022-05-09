package com.app.barbershopweb.user.crud.controller;

import com.app.barbershopweb.user.crud.UserController;
import com.app.barbershopweb.user.crud.UserConverter;
import com.app.barbershopweb.user.crud.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.web.servlet.MockMvc;

import static com.app.barbershopweb.user.crud.constants.UserErrorMessage__TestConstants.USER_ERR_INVALID_PATH_VAR_USER_ID;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@DisplayName("Testing DELETE: " + USERS_URL + "{userId}")
@MockBean(AuthenticationProvider.class)
class UserControllerDeleteByIdTest {

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
                .perform(delete(USERS_URL + "/" + USERS_INVALID_USER_ID))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(USER_ERR_INVALID_PATH_VAR_USER_ID)));
    }

    @DisplayName("returns empty body, status code 200, " +
            "when: user with existing / not existing id was deleted")
    @Test
    void shouldDeleteUserById() throws Exception {
        mockMvc
                .perform(delete(USERS_URL + "/" + USERS_VALID_USER_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }

}
