package com.app.barbershopweb.barbershop.controller;

import com.app.barbershopweb.barbershop.BarbershopController;
import com.app.barbershopweb.barbershop.BarbershopConverter;
import com.app.barbershopweb.barbershop.BarbershopService;
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

import static com.app.barbershopweb.barbershop.constants.BarbershopList__TestConstants.BARBERSHOP_VALID_DTO_LIST;
import static com.app.barbershopweb.barbershop.constants.BarbershopList__TestConstants.BARBERSHOP_VALID_ENTITY_LIST;
import static com.app.barbershopweb.barbershop.constants.BarbershopMetadata__TestConstants.BARBERSHOPS_URL;
import static com.app.barbershopweb.barbershop.constants.BarbershopMetadata__TestConstants.BARBERSHOP_FIELD_AMOUNT;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BarbershopController.class)
@DisplayName("Testing GET: " + BARBERSHOPS_URL)
@ExtendWith(MockitoExtension.class)
class BarbershopControllerGetBarbershopsTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BarbershopService barbershopService;

    @MockBean
    BarbershopConverter converter;

    @DisplayName("gives empty Barbershop List when they're no added barbershops yet")
    @Test
    void shouldReturnEmptyBarbershopList() throws Exception {

        when(barbershopService.getBarbershops()).thenReturn(Collections.emptyList());
        when(converter.barbershopEntityListToDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

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
                BARBERSHOP_VALID_ENTITY_LIST
        );
        when(converter.barbershopEntityListToDtoList(BARBERSHOP_VALID_ENTITY_LIST)).thenReturn(
                BARBERSHOP_VALID_DTO_LIST
        );

        mockMvc.
                perform(get(BARBERSHOPS_URL)).
                andDo(print()).
                andExpect(status().isOk()).

                andExpect(jsonPath("$[0].id", is(BARBERSHOP_VALID_DTO_LIST.get(0).id().intValue()))).
                andExpect(jsonPath("$[0].address", is(BARBERSHOP_VALID_DTO_LIST.get(0).address()))).
                andExpect(jsonPath("$[0].name", is(BARBERSHOP_VALID_DTO_LIST.get(0).name()))).
                andExpect(jsonPath("$[0].phoneNumber", is(BARBERSHOP_VALID_DTO_LIST.get(0).phoneNumber()))).
                andExpect(jsonPath("$[0].email", is(BARBERSHOP_VALID_DTO_LIST.get(0).email()))).
                andExpect(jsonPath("$[0].workTimeFrom", is(BARBERSHOP_VALID_DTO_LIST.get(0).workTimeFrom().format(DateTimeFormatter.ISO_LOCAL_TIME)))).
                andExpect(jsonPath("$[0].workTimeTo", is(BARBERSHOP_VALID_DTO_LIST.get(0).workTimeTo().format(DateTimeFormatter.ISO_LOCAL_TIME)))).

                andExpect(jsonPath("$[1].id", is(BARBERSHOP_VALID_DTO_LIST.get(1).id().intValue()))).
                andExpect(jsonPath("$[1].address", is(BARBERSHOP_VALID_DTO_LIST.get(1).address()))).
                andExpect(jsonPath("$[1].name", is(BARBERSHOP_VALID_DTO_LIST.get(1).name()))).
                andExpect(jsonPath("$[1].phoneNumber", is(BARBERSHOP_VALID_DTO_LIST.get(1).phoneNumber()))).
                andExpect(jsonPath("$[1].email", is(BARBERSHOP_VALID_DTO_LIST.get(1).email()))).
                andExpect(jsonPath("$[1].workTimeFrom", is(BARBERSHOP_VALID_DTO_LIST.get(1).workTimeFrom().format(DateTimeFormatter.ISO_LOCAL_TIME)))).
                andExpect(jsonPath("$[1].workTimeTo", is(BARBERSHOP_VALID_DTO_LIST.get(1).workTimeTo().format(DateTimeFormatter.ISO_LOCAL_TIME)))).

                andExpect(jsonPath("$[2].id", is(BARBERSHOP_VALID_DTO_LIST.get(2).id().intValue()))).
                andExpect(jsonPath("$[2].address", is(BARBERSHOP_VALID_DTO_LIST.get(2).address()))).
                andExpect(jsonPath("$[2].name", is(BARBERSHOP_VALID_DTO_LIST.get(2).name()))).
                andExpect(jsonPath("$[2].phoneNumber", is(BARBERSHOP_VALID_DTO_LIST.get(2).phoneNumber()))).
                andExpect(jsonPath("$[2].email", is(BARBERSHOP_VALID_DTO_LIST.get(2).email()))).
                andExpect(jsonPath("$[2].workTimeFrom", is(BARBERSHOP_VALID_DTO_LIST.get(2).workTimeFrom().format(DateTimeFormatter.ISO_LOCAL_TIME)))).
                andExpect(jsonPath("$[2].workTimeTo", is(BARBERSHOP_VALID_DTO_LIST.get(2).workTimeTo().format(DateTimeFormatter.ISO_LOCAL_TIME)))).


                andExpect(jsonPath("$", hasSize(3))).
                andExpect(jsonPath("$[0]", aMapWithSize(BARBERSHOP_FIELD_AMOUNT))).
                andExpect(jsonPath("$[1]", aMapWithSize(BARBERSHOP_FIELD_AMOUNT))).
                andExpect(jsonPath("$[2]", aMapWithSize(BARBERSHOP_FIELD_AMOUNT)));

    }
}