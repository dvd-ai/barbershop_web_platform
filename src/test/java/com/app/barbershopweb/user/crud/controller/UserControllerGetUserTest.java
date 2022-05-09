package com.app.barbershopweb.user.crud.controller;

import com.app.barbershopweb.user.crud.UserController;
import com.app.barbershopweb.user.crud.UserConverter;
import com.app.barbershopweb.user.crud.UserService;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.app.barbershopweb.user.crud.constants.UserList__TestConstants.USERS_USER_VALID_DTO_LIST;
import static com.app.barbershopweb.user.crud.constants.UserList__TestConstants.USER_USER_VALID_ENTITY_LIST;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_FIELD_AMOUNT;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@DisplayName("Testing GET: " + USERS_URL)
@ExtendWith(MockitoExtension.class)
@MockBean(AuthenticationProvider.class)
class UserControllerGetUserTest {

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
                USER_USER_VALID_ENTITY_LIST
        );
        when(converter.userEntityListToDtoList(USER_USER_VALID_ENTITY_LIST)).thenReturn(
                USERS_USER_VALID_DTO_LIST
        );

        MockHttpServletResponse response = mockMvc.
                perform(get(USERS_URL)).
                andDo(print()).andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        checkUsersDtoJson(response.getContentAsString());
    }

    void checkUsersDtoJson(String json) {
        DocumentContext context = JsonPath.parse(json);
        List<Object> object = context.read("$");

        assertEquals(USERS_USER_VALID_DTO_LIST.size(), object.size());

        for (var i = 0; i < USERS_USER_VALID_DTO_LIST.size(); i++) {
            Map<String, Object> dto = context.read("$[" + i + "]");
            assertEquals(USERS_FIELD_AMOUNT, dto.size());
            assertEquals(USERS_USER_VALID_DTO_LIST.get(i).id().intValue(), (Integer) context.read("$[" + i + "].id"));
            assertEquals(USERS_USER_VALID_DTO_LIST.get(i).firstName(), context.read("$[" + i + "].firstName"));
            assertEquals(USERS_USER_VALID_DTO_LIST.get(i).lastName(), context.read("$[" + i + "].lastName"));
            assertEquals(USERS_USER_VALID_DTO_LIST.get(i).phoneNumber(), context.read("$[" + i + "].phoneNumber"));
            assertEquals(USERS_USER_VALID_DTO_LIST.get(i).email(), context.read("$[" + i + "].email"));
            assertEquals(USERS_USER_VALID_DTO_LIST.get(i).role(), context.read("$[" + i + "].role"));
            assertEquals(USERS_USER_VALID_DTO_LIST.get(i).registrationDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), context.read("$[" + i + "].registrationDate"));
        }
    }
}
