package com.app.barbershopweb.barbershop.controller;

import com.app.barbershopweb.barbershop.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BarbershopController.class)
@DisplayName("Testing GET: /barbershops")
class BarbershopControllerGetBarbershopsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BarbershopService barbershopService;

    @MockBean
    private BarbershopConverter converter;

    @DisplayName("gives empty Barbershop List when they're no added barbershops yet")
    @Test
    void shouldReturnEmptyBarbershopList() throws Exception {

        when(barbershopService.getBarbershops()).thenReturn(Collections.emptyList());
        when(converter.barbershopEntityListToDtoList(any())).thenReturn(Collections.emptyList());

        mockMvc.
                perform(get("/barbershops")).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(content().string("[]"));

        verify(barbershopService).getBarbershops();
        verify(converter).barbershopEntityListToDtoList(any());
    }

    @DisplayName("gives all barbershops at once")
    @Test
    void shouldReturnAllBarbershops() throws Exception {
        List<Barbershop> barbershops = List.of(
                new Barbershop( 1L, "A1", "Barbershop1", "+38091", "1@gmail.com"),
                new Barbershop(2L, "A2", "Barbershop2", "+38092", "2@gmail.com"),
                new Barbershop( 3L, "A3", "Barbershop3", "+38093", "3@gmail.com")
        );

        List<BarbershopDto> barbershopDtos = List.of(
                new BarbershopDto(1L, "A1", "Barbershop1", "+38091", "1@gmail.com"),
                new BarbershopDto(2L, "A2", "Barbershop2", "+38092", "2@gmail.com"),
                new BarbershopDto(3L, "A3", "Barbershop3", "+38093", "3@gmail.com")
        );


        when(barbershopService.getBarbershops()).thenReturn(barbershops);
        when(converter.barbershopEntityListToDtoList(barbershops)).thenReturn(barbershopDtos);

        mockMvc.
                perform(get("/barbershops")).
                andDo(print()).
                andExpect(status().isOk()).

                andExpect(jsonPath("$[0].id", is(1))).
                andExpect(jsonPath("$[0].address", is("A1"))).
                andExpect(jsonPath("$[0].name", is("Barbershop1"))).
                andExpect(jsonPath("$[0].phoneNumber", is("+38091"))).
                andExpect(jsonPath("$[0].email", is("1@gmail.com"))).

                andExpect(jsonPath("$[1].id", is(2))).
                andExpect(jsonPath("$[1].address", is("A2"))).
                andExpect(jsonPath("$[1].name", is("Barbershop2"))).
                andExpect(jsonPath("$[1].phoneNumber", is("+38092"))).
                andExpect(jsonPath("$[1].email", is("2@gmail.com"))).

                andExpect(jsonPath("$[2].id", is(3))).
                andExpect(jsonPath("$[2].address", is("A3"))).
                andExpect(jsonPath("$[2].name", is("Barbershop3"))).
                andExpect(jsonPath("$[2].phoneNumber", is("+38093"))).
                andExpect(jsonPath("$[2].email", is("3@gmail.com")));
    }
}