package com.app.barbershopweb.barbershop.controller;

import com.app.barbershopweb.barbershop.Barbershop;
import com.app.barbershopweb.barbershop.BarbershopController;
import com.app.barbershopweb.barbershop.BarbershopConverter;
import com.app.barbershopweb.barbershop.BarbershopService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BarbershopController.class)
@DisplayName("Testing POST: /barbershops")
class BarbershopControllerAddBarbershopTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BarbershopService barbershopService;

    @MockBean
    BarbershopConverter barbershopConverter;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("when barbershop dto isn't valid " +
            "returns status code 400")
    @Test
    void whenBarbershopDtoNotValid() throws Exception {
        String json = objectMapper.writeValueAsString(
                new Barbershop(
                1L, "a1",
                "", "+38091",
                "1@gmail.com"
                )
        );

        mockMvc
                .perform(post("/barbershops")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("after saving barbershop entity returns its id and status code 201")
    @Test
    void shouldAddBarbershop() throws Exception {
        Barbershop barbershop = new Barbershop(
                1L, "a1",
                "name1", "+38091",
                "1@gmail.com"
        );

        String json = objectMapper.writeValueAsString(
                barbershop
        );

        when(barbershopConverter.mapToEntity(any())).thenReturn(barbershop);
        when(barbershopService.addBarbershop(any())).thenReturn(1L);

        mockMvc
                .perform(post("/barbershops")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));
    }
}
