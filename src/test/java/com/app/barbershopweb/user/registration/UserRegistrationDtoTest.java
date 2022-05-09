package com.app.barbershopweb.user.registration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_URL;
import static com.app.barbershopweb.user.registration.constants.UserRegistrationErrorMessage_Dto__TestConstants.*;
import static com.app.barbershopweb.user.registration.constants.UserRegistration_Dto__TestConstants.USER_REGISTRATION_INVALID_DTO;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserRegistrationController.class)
@MockBean(AuthenticationProvider.class)
class UserRegistrationDtoTest {

    @MockBean
    UserRegistrationService userRegistrationService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @WithMockUser
    @Test
    void register_InvalidDto() throws Exception {
        String json = objectMapper.writeValueAsString(
                USER_REGISTRATION_INVALID_DTO
        );

        mockMvc
                .perform(post(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(7)))
                .andExpect(jsonPath("$.errors", hasItem(USER_REGISTRATION_INVALID_DTO_FIRSTNAME)))
                .andExpect(jsonPath("$.errors", hasItem(USER_REGISTRATION_INVALID_DTO_LASTNAME)))
                .andExpect(jsonPath("$.errors", hasItem(USER_REGISTRATION_INVALID_DTO_PHONE_NUMBER)))
                .andExpect(jsonPath("$.errors", hasItem(USER_REGISTRATION_INVALID_DTO_EMAIL)))
                .andExpect(jsonPath("$.errors", hasItem(USER_REGISTRATION_INVALID_DTO_USERNAME)))
                .andExpect(jsonPath("$.errors", hasItem(USER_REGISTRATION_INVALID_DTO_PASSWORD)))
                .andExpect(jsonPath("$.errors", hasItem(USER_REGISTRATION_INVALID_DTO_REGISTRATION_DATE)))
        ;

        //should fill constants with output... from failed test
    }
}
