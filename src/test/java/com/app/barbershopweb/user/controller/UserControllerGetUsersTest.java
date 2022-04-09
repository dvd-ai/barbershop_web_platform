package com.app.barbershopweb.user.controller;

import com.app.barbershopweb.user.UserController;
import com.app.barbershopweb.user.UserConverter;
import com.app.barbershopweb.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static com.app.barbershopweb.user.constants.UserList__TestConstants.USERS_USER_VALID_DTO_LIST;
import static com.app.barbershopweb.user.constants.UserList__TestConstants.USERS_USER_VALID_ENTITY_LIST;
import static com.app.barbershopweb.user.constants.UserMetadata__TestConstants.USERS_FIELD_AMOUNT;
import static com.app.barbershopweb.user.constants.UserMetadata__TestConstants.USERS_URL;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@DisplayName("Testing GET: " + USERS_URL)
@ExtendWith(MockitoExtension.class)
class UserControllerGetUsersTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    UserConverter converter;


    @DisplayName("gives empty User List when they're no added users yet")
    @Test
    void shouldReturnEmptyUserList() throws Exception {

        when(userService.getUsers()).thenReturn(Collections.emptyList());
        when(converter.userEntityListToDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

        mockMvc.
                perform(get(USERS_URL)).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(content().string("[]"));
    }

    @DisplayName("gives all users at once")
    @Test
    void shouldReturnAllUsers() throws Exception {
        when(userService.getUsers()).thenReturn(
                USERS_USER_VALID_ENTITY_LIST
        );
        when(converter.userEntityListToDtoList(USERS_USER_VALID_ENTITY_LIST)).thenReturn(
                USERS_USER_VALID_DTO_LIST
        );

        mockMvc.
                perform(get(USERS_URL)).
                andDo(print()).
                andExpect(status().isOk()).

                andExpect(jsonPath("$[0].id", is(USERS_USER_VALID_DTO_LIST.get(0).id().intValue()))).
                andExpect(jsonPath("$[0].firstName", is(USERS_USER_VALID_DTO_LIST.get(0).firstName()))).
                andExpect(jsonPath("$[0].lastName", is(USERS_USER_VALID_DTO_LIST.get(0).lastName()))).
                andExpect(jsonPath("$[0].phoneNumber", is(USERS_USER_VALID_DTO_LIST.get(0).phoneNumber()))).
                andExpect(jsonPath("$[0].email", is(USERS_USER_VALID_DTO_LIST.get(0).email()))).
                andExpect(jsonPath("$[0].role", is(USERS_USER_VALID_DTO_LIST.get(0).role()))).
                andExpect(jsonPath("$[0].registrationDate", is(USERS_USER_VALID_DTO_LIST.get(0).registrationDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))).

                andExpect(jsonPath("$[1].id", is(USERS_USER_VALID_DTO_LIST.get(1).id().intValue()))).
                andExpect(jsonPath("$[1].firstName", is(USERS_USER_VALID_DTO_LIST.get(1).firstName()))).
                andExpect(jsonPath("$[1].lastName", is(USERS_USER_VALID_DTO_LIST.get(1).lastName()))).
                andExpect(jsonPath("$[1].phoneNumber", is(USERS_USER_VALID_DTO_LIST.get(1).phoneNumber()))).
                andExpect(jsonPath("$[1].email", is(USERS_USER_VALID_DTO_LIST.get(1).email()))).
                andExpect(jsonPath("$[1].role", is(USERS_USER_VALID_DTO_LIST.get(1).role()))).
                andExpect(jsonPath("$[1].registrationDate", is(USERS_USER_VALID_DTO_LIST.get(1).registrationDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))).

                andExpect(jsonPath("$[2].id", is(USERS_USER_VALID_DTO_LIST.get(2).id().intValue()))).
                andExpect(jsonPath("$[2].firstName", is(USERS_USER_VALID_DTO_LIST.get(2).firstName()))).
                andExpect(jsonPath("$[2].lastName", is(USERS_USER_VALID_DTO_LIST.get(2).lastName()))).
                andExpect(jsonPath("$[2].phoneNumber", is(USERS_USER_VALID_DTO_LIST.get(2).phoneNumber()))).
                andExpect(jsonPath("$[2].email", is(USERS_USER_VALID_DTO_LIST.get(2).email()))).
                andExpect(jsonPath("$[2].role", is(USERS_USER_VALID_DTO_LIST.get(2).role()))).
                andExpect(jsonPath("$[2].registrationDate", is(USERS_USER_VALID_DTO_LIST.get(2).registrationDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))).


                andExpect(jsonPath("$", hasSize(3))).
                andExpect(jsonPath("$[0]", aMapWithSize(USERS_FIELD_AMOUNT))).
                andExpect(jsonPath("$[1]", aMapWithSize(USERS_FIELD_AMOUNT))).
                andExpect(jsonPath("$[2]", aMapWithSize(USERS_FIELD_AMOUNT)));

    }
}
