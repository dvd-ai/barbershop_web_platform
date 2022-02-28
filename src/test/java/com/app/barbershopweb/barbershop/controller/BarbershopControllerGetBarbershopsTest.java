package com.app.barbershopweb.barbershop.controller;

import com.app.barbershopweb.barbershop.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static com.app.barbershopweb.barbershop.BarbershopTestConstants.BARBERSHOPS_URL;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BarbershopController.class)
@DisplayName("Testing GET: " + BARBERSHOPS_URL)
class BarbershopControllerGetBarbershopsTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BarbershopService barbershopService;

    @MockBean
    BarbershopConverter converter;

    private final BarbershopTestConstants btc = new BarbershopTestConstants();

    @DisplayName("gives empty Barbershop List when they're no added barbershops yet")
    @Test
    void shouldReturnEmptyBarbershopList() throws Exception {

        when(barbershopService.getBarbershops()).thenReturn(Collections.emptyList());
        when(converter.barbershopEntityListToDtoList(any())).thenReturn(Collections.emptyList());

        mockMvc.
                perform(get(BARBERSHOPS_URL)).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(content().string("[]"));
    }

    @DisplayName("gives all barbershops at once")
    @Test
    void shouldReturnAllBarbershops() throws Exception {
        when(barbershopService.getBarbershops()).thenReturn(
                btc.VALID_BARBERSHOP_ENTITY_LIST
        );
        when(converter.barbershopEntityListToDtoList(any())).thenReturn(
                btc.VALID_BARBERSHOP_DTO_LIST
        );

        mockMvc.
                perform(get(BARBERSHOPS_URL)).
                andDo(print()).
                andExpect(status().isOk()).

                andExpect(jsonPath("$[0].id", is(btc.VALID_BARBERSHOP_DTO_LIST.get(0).id().intValue()))).
                andExpect(jsonPath("$[0].address", is(btc.VALID_BARBERSHOP_DTO_LIST.get(0).address()))).
                andExpect(jsonPath("$[0].name", is(btc.VALID_BARBERSHOP_DTO_LIST.get(0).name()))).
                andExpect(jsonPath("$[0].phoneNumber", is(btc.VALID_BARBERSHOP_DTO_LIST.get(0).phoneNumber()))).
                andExpect(jsonPath("$[0].email", is(btc.VALID_BARBERSHOP_DTO_LIST.get(0).email()))).
                andExpect(jsonPath("$[0].workTimeFrom", is(btc.VALID_BARBERSHOP_DTO_LIST.get(0).workTimeFrom().format(DateTimeFormatter.ISO_LOCAL_TIME)))).
                andExpect(jsonPath("$[0].workTimeTo", is(btc.VALID_BARBERSHOP_DTO_LIST.get(0).workTimeTo().format(DateTimeFormatter.ISO_LOCAL_TIME)))).

                andExpect(jsonPath("$[1].id", is(btc.VALID_BARBERSHOP_DTO_LIST.get(1).id().intValue()))).
                andExpect(jsonPath("$[1].address", is(btc.VALID_BARBERSHOP_DTO_LIST.get(1).address()))).
                andExpect(jsonPath("$[1].name", is(btc.VALID_BARBERSHOP_DTO_LIST.get(1).name()))).
                andExpect(jsonPath("$[1].phoneNumber", is(btc.VALID_BARBERSHOP_DTO_LIST.get(1).phoneNumber()))).
                andExpect(jsonPath("$[1].email", is(btc.VALID_BARBERSHOP_DTO_LIST.get(1).email()))).
                andExpect(jsonPath("$[1].workTimeFrom", is(btc.VALID_BARBERSHOP_DTO_LIST.get(1).workTimeFrom().format(DateTimeFormatter.ISO_LOCAL_TIME)))).
                andExpect(jsonPath("$[1].workTimeTo", is(btc.VALID_BARBERSHOP_DTO_LIST.get(1).workTimeTo().format(DateTimeFormatter.ISO_LOCAL_TIME)))).

                andExpect(jsonPath("$[2].id", is(btc.VALID_BARBERSHOP_DTO_LIST.get(2).id().intValue()))).
                andExpect(jsonPath("$[2].address", is(btc.VALID_BARBERSHOP_DTO_LIST.get(2).address()))).
                andExpect(jsonPath("$[2].name", is(btc.VALID_BARBERSHOP_DTO_LIST.get(2).name()))).
                andExpect(jsonPath("$[2].phoneNumber", is(btc.VALID_BARBERSHOP_DTO_LIST.get(2).phoneNumber()))).
                andExpect(jsonPath("$[2].email", is(btc.VALID_BARBERSHOP_DTO_LIST.get(2).email()))).
                andExpect(jsonPath("$[2].workTimeFrom", is(btc.VALID_BARBERSHOP_DTO_LIST.get(2).workTimeFrom().format(DateTimeFormatter.ISO_LOCAL_TIME)))).
                andExpect(jsonPath("$[2].workTimeTo", is(btc.VALID_BARBERSHOP_DTO_LIST.get(2).workTimeTo().format(DateTimeFormatter.ISO_LOCAL_TIME)))).


                andExpect(jsonPath("$", hasSize(3))).
                andExpect(jsonPath("$[0]", aMapWithSize(btc.BARBERSHOP_FIELD_AMOUNT))).
                andExpect(jsonPath("$[1]", aMapWithSize(btc.BARBERSHOP_FIELD_AMOUNT))).
                andExpect(jsonPath("$[2]", aMapWithSize(btc.BARBERSHOP_FIELD_AMOUNT)));

    }
}