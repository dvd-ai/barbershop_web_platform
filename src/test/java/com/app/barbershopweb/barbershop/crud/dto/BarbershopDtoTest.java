package com.app.barbershopweb.barbershop.crud.dto;

import com.app.barbershopweb.barbershop.crud.BarbershopController;
import com.app.barbershopweb.barbershop.crud.BarbershopConverter;
import com.app.barbershopweb.barbershop.crud.BarbershopService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.app.barbershopweb.barbershop.crud.constants.BarbershopDto__TestConstants.BARBERSHOP_INVALID_DTO;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopErrorMessage__TestConstants.*;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopMetadata__TestConstants.BARBERSHOPS_URL;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BarbershopController.class)
@MockBean(AuthenticationProvider.class)
class BarbershopDtoTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    BarbershopService barbershopService;

    @MockBean
    BarbershopConverter barbershopConverter;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName(
            "Testing POST: " + BARBERSHOPS_URL +
                    " when barbershop dto isn't valid " +
                    "returns status code 400 & error dto")
    @Test
    @WithMockUser(roles = "ADMIN")
    void addBarbershop__whenDtoNotValid() throws Exception {
        String json = objectMapper.writeValueAsString(
                BARBERSHOP_INVALID_DTO
        );

        mockMvc
                .perform(post(BARBERSHOPS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors", hasItem(BARBERSHOP_ERR_INVALID_DTO_ID)))
                .andExpect(jsonPath("$.errors", hasItem(BARBERSHOP_ERR_INVALID_DTO_PHONE_NUMBER)))
                .andExpect(jsonPath("$.errors", hasItem(BARBERSHOP_ERR_INVALID_DTO_NAME)));
    }

    @DisplayName(
            "Testing PUT: " + BARBERSHOPS_URL +
                    "when barbershop dto isn't valid " +
                    "returns status code 400 & returns error dto")
    @Test
    @WithMockUser(roles = "ADMIN")
    void whenBarbershopDtoNotValid() throws Exception {


        String json = objectMapper.writeValueAsString(
                BARBERSHOP_INVALID_DTO
        );

        mockMvc
                .perform(put(BARBERSHOPS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }


}