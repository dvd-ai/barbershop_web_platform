package com.app.barbershopweb.user.registration;

import com.app.barbershopweb.exception.DbUniqueConstraintsViolationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;

import java.util.List;

import static com.app.barbershopweb.user.credentials.UserCredentials_Entities__TestConstants.USER_CREDENTIALS_VALID_ENTITY;
import static com.app.barbershopweb.user.credentials.error.UserCredentialsErrorMessage_Fk__TestConstants.USER_CREDENTIALS_ERR_FK_USER_ID;
import static com.app.barbershopweb.user.credentials.error.UserCredentialsErrorMessage_Uk__TestConstants.USER_CREDENTIALS_ERR_UK_USERNAME;
import static com.app.barbershopweb.user.credentials.error.UserCredentialsErrorMessage_Uk__TestConstants.USER_CREDENTIALS_ERR_UK_USER_ID;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_URL;
import static com.app.barbershopweb.user.registration.constants.UserRegistration_Dto__TestConstants.USER_REGISTRATION_VALID_DTO;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserRegistrationController.class)
class UserRegistrationControllerTest {

    @MockBean
    UserRegistrationService userRegistrationService;

    @MockBean
    DataSource dataSource;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void register() throws Exception {
        String json = objectMapper.writeValueAsString(
                USER_REGISTRATION_VALID_DTO
        );

        when(userRegistrationService.register(USER_REGISTRATION_VALID_DTO))
                .thenReturn(USER_CREDENTIALS_VALID_ENTITY.getUserId());

        mockMvc
                .perform(post(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isCreated())
                .andExpect(content().string(String.valueOf(USER_CREDENTIALS_VALID_ENTITY.getUserId())));
    }

    @Test
    void register_ukUserId() throws Exception {
        String json = objectMapper.writeValueAsString(
                USER_REGISTRATION_VALID_DTO
        );

        when(userRegistrationService.register(USER_REGISTRATION_VALID_DTO))
                .thenThrow(new DbUniqueConstraintsViolationException(List.of(USER_CREDENTIALS_ERR_UK_USER_ID)));

        mockMvc
                .perform(post(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(USER_CREDENTIALS_ERR_UK_USER_ID)))
        ;
    }

    @Test
    void register_fkUserId() throws Exception {
        String json = objectMapper.writeValueAsString(
                USER_REGISTRATION_VALID_DTO
        );

        when(userRegistrationService.register(USER_REGISTRATION_VALID_DTO))
                .thenThrow(new DbUniqueConstraintsViolationException(List.of(USER_CREDENTIALS_ERR_FK_USER_ID)));

        mockMvc
                .perform(post(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(USER_CREDENTIALS_ERR_FK_USER_ID)))
        ;
    }

    @Test
    void register_ukUsername() throws Exception {
        String json = objectMapper.writeValueAsString(
                USER_REGISTRATION_VALID_DTO
        );

        when(userRegistrationService.register(USER_REGISTRATION_VALID_DTO))
                .thenThrow(new DbUniqueConstraintsViolationException(List.of(USER_CREDENTIALS_ERR_UK_USERNAME)));

        mockMvc
                .perform(post(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(USER_CREDENTIALS_ERR_UK_USERNAME)))
        ;
    }
}
