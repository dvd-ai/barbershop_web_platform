package com.app.barbershopweb.barbershop.controller;

import com.app.barbershopweb.barbershop.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BarbershopController.class)
@DisplayName("Testing PUT: /barbershops")
class BarbershopControllerUpdateBarbershopTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BarbershopConverter barbershopConverter;

    @MockBean
    BarbershopService barbershopService;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("when barbershop dto isn't valid " +
            "returns status code 400")
    @Test
    void whenBarbershopDtoNotValid() throws Exception {
        String json = objectMapper.writeValueAsString(
                new Barbershop(
                        1L, "",
                        "", "+38091",
                        "1@gmail.com"
                )
        );

        mockMvc
                .perform(put("/barbershops")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("when entity with 'id' in barbershopDto doesn't exist " +
            "returns status code 404 and error dto")
    void whenNotFoundBarbershopId() throws Exception {
        Barbershop barbershop = new Barbershop(
                100_000L, "a1",
                "name1", "+38091",
                "1@gmail.com"
        );

        String json = objectMapper.writeValueAsString(
                barbershop
        );

        when(barbershopConverter.mapToEntity(any())).thenReturn(barbershop);
        when(barbershopService.updateBarbershop(any())).thenReturn(Optional.empty());

        mockMvc
                .perform(put("/barbershops")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is("Barbershop with id '" + barbershop.getId() + "' not found.")));
    }

    @Test
    @DisplayName("should return updated barbershop (dto)")
    void shouldReturnUpdatedBarbershop() throws Exception {
        Barbershop barbershop = new Barbershop(
                1L, "a1",
                "name1", "+38091",
                "1@gmail.com"
        );

        String json = objectMapper.writeValueAsString(
                barbershop
        );

        when(barbershopConverter.mapToEntity(any())).thenReturn(barbershop);
        when(barbershopService.updateBarbershop(any())).thenReturn(Optional.of(barbershop));
        when(barbershopConverter.mapToDto(any())).thenReturn(
                new BarbershopDto(
                        1L, "a1",
                        "name1", "+38091",
                        "1@gmail.com"
                )
        );

        mockMvc
                .perform(put("/barbershops")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",  is(1)))
                .andExpect(jsonPath("$.address", is("a1")))
                .andExpect(jsonPath("$.name", is("name1")))
                .andExpect(jsonPath("$.phoneNumber", is("+38091")))
                .andExpect(jsonPath("$.email", is("1@gmail.com")));

    }
}
