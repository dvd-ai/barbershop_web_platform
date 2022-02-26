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


import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.app.barbershopweb.barbershop.controller.BarbershopTestConstants.BARBERSHOPS_URL;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BarbershopController.class)
@DisplayName("Testing PUT: " + BARBERSHOPS_URL)
class BarbershopControllerUpdateBarbershopTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BarbershopConverter barbershopConverter;

    @MockBean
    BarbershopService barbershopService;

    @Autowired
    ObjectMapper objectMapper;

    BarbershopTestConstants btc = new BarbershopTestConstants();


    @DisplayName("when barbershop dto isn't valid " +
            "returns status code 400")
    @Test
    void whenBarbershopDtoNotValid() throws Exception {


        String json = objectMapper.writeValueAsString(
                btc.INVALID_BARBERSHOP_DTO
        );

        mockMvc
                .perform(put(BARBERSHOPS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("when entity with 'id' in barbershopDto doesn't exist " +
            "returns status code 404 and error dto")
    void whenNotFoundBarbershopId() throws Exception {
        String json = objectMapper.writeValueAsString(
                btc.BARBERSHOP_DTO_NOT_EXISTED_ID
        );

        when(barbershopConverter.mapToEntity(any())).thenReturn(
                btc.BARBERSHOP_ENTITY_NOT_EXISTED_ID
        );
        when(barbershopService.updateBarbershop(any())).thenReturn(Optional.empty());

        mockMvc
                .perform(put(BARBERSHOPS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(
                        "Barbershop with id '" + btc.NOT_EXISTED_BARBERSHOP_ID + "' not found."
                )))
                .andExpect(jsonPath("$", aMapWithSize(1)));
    }

    @Test
    @DisplayName("should return updated barbershop (dto)")
    void shouldReturnUpdatedBarbershop() throws Exception {
        String json = objectMapper.writeValueAsString(
                btc.VALID_BARBERSHOP_DTO
        );

        when(barbershopConverter.mapToEntity(any())).thenReturn(
                btc.VALID_BARBERSHOP_ENTITY
        );
        when(barbershopService.updateBarbershop(any())).thenReturn(
                Optional.of(btc.VALID_BARBERSHOP_ENTITY)
        );
        when(barbershopConverter.mapToDto(any())).thenReturn(
                btc.VALID_BARBERSHOP_DTO
        );

        mockMvc
                .perform(put(BARBERSHOPS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",  is(btc.VALID_BARBERSHOP_DTO.id().intValue())))
                .andExpect(jsonPath("$.address", is(btc.VALID_BARBERSHOP_DTO.address())))
                .andExpect(jsonPath("$.name", is(btc.VALID_BARBERSHOP_DTO.name())))
                .andExpect(jsonPath("$.phoneNumber", is(btc.VALID_BARBERSHOP_DTO.phoneNumber())))
                .andExpect(jsonPath("$.email", is(btc.VALID_BARBERSHOP_DTO.email())))
                .andExpect(jsonPath("$.workTimeFrom", is(
                        btc.VALID_BARBERSHOP_DTO.workTimeFrom().format(DateTimeFormatter.ISO_LOCAL_TIME)
                        ))
                )
                .andExpect(jsonPath("$.workTimeTo", is(
                        btc.VALID_BARBERSHOP_DTO.workTimeTo().format(DateTimeFormatter.ISO_LOCAL_TIME)
                        ))
                )
                .andExpect(jsonPath("$", aMapWithSize(btc.BARBERSHOP_FIELD_AMOUNT)));

    }
}
