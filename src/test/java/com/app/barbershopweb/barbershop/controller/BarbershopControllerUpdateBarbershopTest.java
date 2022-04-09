package com.app.barbershopweb.barbershop.controller;

import com.app.barbershopweb.barbershop.BarbershopController;
import com.app.barbershopweb.barbershop.BarbershopConverter;
import com.app.barbershopweb.barbershop.BarbershopService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.app.barbershopweb.barbershop.constants.BarbershopDto__TestConstants.BARBERSHOP_NOT_EXISTED_ID_DTO;
import static com.app.barbershopweb.barbershop.constants.BarbershopDto__TestConstants.BARBERSHOP_VALID_DTO;
import static com.app.barbershopweb.barbershop.constants.BarbershopEntity__TestConstants.BARBERSHOP_NOT_EXISTED_ID_ENTITY;
import static com.app.barbershopweb.barbershop.constants.BarbershopEntity__TestConstants.BARBERSHOP_VALID_ENTITY;
import static com.app.barbershopweb.barbershop.constants.BarbershopMetadata__TestConstants.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BarbershopController.class)
@DisplayName("Testing PUT: " + BARBERSHOPS_URL)
@ExtendWith(MockitoExtension.class)
class BarbershopControllerUpdateBarbershopTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BarbershopConverter barbershopConverter;

    @MockBean
    BarbershopService barbershopService;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @DisplayName("when entity with 'id' in barbershopDto doesn't exist " +
            "returns status code 404 and error dto")
    void whenNotFoundBarbershopId() throws Exception {
        String json = objectMapper.writeValueAsString(
                BARBERSHOP_NOT_EXISTED_ID_DTO
        );

        when(barbershopConverter.mapToEntity(BARBERSHOP_NOT_EXISTED_ID_DTO)).thenReturn(
                BARBERSHOP_NOT_EXISTED_ID_ENTITY
        );
        when(barbershopService.updateBarbershop(BARBERSHOP_NOT_EXISTED_ID_ENTITY)).thenReturn(Optional.empty());

        mockMvc
                .perform(put(BARBERSHOPS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(
                        "Barbershop with id '" + BARBERSHOP_NOT_EXISTED_BARBERSHOP_ID + "' not found."
                )))
                .andExpect(jsonPath("$", aMapWithSize(1)));
    }

    @Test
    @DisplayName("should return updated barbershop (dto)")
    void shouldReturnUpdatedBarbershop() throws Exception {
        String json = objectMapper.writeValueAsString(
                BARBERSHOP_VALID_DTO
        );

        when(barbershopConverter.mapToEntity(BARBERSHOP_VALID_DTO)).thenReturn(
                BARBERSHOP_VALID_ENTITY
        );
        when(barbershopService.updateBarbershop(BARBERSHOP_VALID_ENTITY)).thenReturn(
                Optional.of(BARBERSHOP_VALID_ENTITY)
        );
        when(barbershopConverter.mapToDto(BARBERSHOP_VALID_ENTITY)).thenReturn(
                BARBERSHOP_VALID_DTO
        );

        mockMvc
                .perform(put(BARBERSHOPS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(BARBERSHOP_VALID_DTO.id().intValue())))
                .andExpect(jsonPath("$.address", is(BARBERSHOP_VALID_DTO.address())))
                .andExpect(jsonPath("$.name", is(BARBERSHOP_VALID_DTO.name())))
                .andExpect(jsonPath("$.phoneNumber", is(BARBERSHOP_VALID_DTO.phoneNumber())))
                .andExpect(jsonPath("$.email", is(BARBERSHOP_VALID_DTO.email())))
                .andExpect(jsonPath("$.workTimeFrom", is(
                                BARBERSHOP_VALID_DTO.workTimeFrom().format(DateTimeFormatter.ISO_LOCAL_TIME)
                        ))
                )
                .andExpect(jsonPath("$.workTimeTo", is(
                                BARBERSHOP_VALID_DTO.workTimeTo().format(DateTimeFormatter.ISO_LOCAL_TIME)
                        ))
                )
                .andExpect(jsonPath("$", aMapWithSize(BARBERSHOP_FIELD_AMOUNT)));

    }
}
