package com.app.barbershopweb.barbershop.crud.controller;

import com.app.barbershopweb.barbershop.crud.BarbershopController;
import com.app.barbershopweb.barbershop.crud.BarbershopConverter;
import com.app.barbershopweb.barbershop.crud.BarbershopService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.web.servlet.MockMvc;

import static com.app.barbershopweb.barbershop.crud.constants.BarbershopDto__TestConstants.BARBERSHOP_VALID_DTO;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopEntity__TestConstants.BARBERSHOP_VALID_ENTITY;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopMetadata__TestConstants.BARBERSHOPS_URL;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopMetadata__TestConstants.BARBERSHOP_VALID_BARBERSHOP_ID;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BarbershopController.class)
@DisplayName("Testing POST: " + BARBERSHOPS_URL)
@ExtendWith(MockitoExtension.class)
@MockBean(AuthenticationProvider.class)
class BarbershopControllerAddBarbershopTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BarbershopService barbershopService;

    @MockBean
    BarbershopConverter barbershopConverter;

    @Autowired
    ObjectMapper objectMapper;


    @DisplayName("after saving barbershop entity returns its id and status code 201")
    @Test
    void shouldAddBarbershop() throws Exception {
        String json = objectMapper.writeValueAsString(
                BARBERSHOP_VALID_DTO
        );

        when(barbershopConverter.mapToEntity(BARBERSHOP_VALID_DTO)).thenReturn(
                BARBERSHOP_VALID_ENTITY
        );
        when(barbershopService.addBarbershop(BARBERSHOP_VALID_ENTITY)).thenReturn(
                BARBERSHOP_VALID_BARBERSHOP_ID
        );

        mockMvc
                .perform(post(BARBERSHOPS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(String.valueOf(BARBERSHOP_VALID_BARBERSHOP_ID)));
    }
}
