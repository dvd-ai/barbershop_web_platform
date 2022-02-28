package com.app.barbershopweb.barbershop.controller;

import com.app.barbershopweb.barbershop.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.app.barbershopweb.barbershop.BarbershopTestConstants.BARBERSHOPS_URL;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BarbershopController.class)
@DisplayName("Testing GET: " + BARBERSHOPS_URL + "/" + "{barbershopId}")
class BarbershopControllerGetBarbershopByIdTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BarbershopService barbershopService;

    @MockBean
    BarbershopConverter barbershopConverter;

    BarbershopTestConstants btc = new BarbershopTestConstants();

    @DisplayName("When path variable input 'barbershopId' isn't valid" +
            " returns status code 400 (BAD_REQUEST) & error dto")
    @Test
    void whenBarbershopIdNotValid() throws Exception {
        mockMvc
                .perform(get(BARBERSHOPS_URL + "/" + btc.INVALID_BARBERSHOP_ID))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(btc.PV_BARBERSHOP_ID_ERR_MSG)));
    }

    @DisplayName("when there's no barbershop with id 'barbershopId' " +
            "returns status code 404 & error dto")
    @Test
    void whenNotExistedBarbershopId() throws Exception {
        mockMvc
                .perform(get(BARBERSHOPS_URL + "/" + btc.NOT_EXISTED_BARBERSHOP_ID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(
                        "Barbershop with id " + btc.NOT_EXISTED_BARBERSHOP_ID + " not found.")
                ));
    }

    @DisplayName("returns corresponding barbershop dto")
    @Test
    void shouldReturnBarbershop() throws Exception {
        when(barbershopService.findBarbershopById(any())).thenReturn(
                Optional.of(btc.VALID_BARBERSHOP_ENTITY)
        );
        when(barbershopConverter.mapToDto(any())).thenReturn(
                btc.VALID_BARBERSHOP_DTO
        );

        mockMvc
                .perform(get(BARBERSHOPS_URL+ "/" + btc.VALID_BARBERSHOP_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",  is(btc.VALID_BARBERSHOP_DTO.id().intValue())))
                .andExpect(jsonPath("$.address", is(btc.VALID_BARBERSHOP_DTO.address())))
                .andExpect(jsonPath("$.name", is(btc.VALID_BARBERSHOP_DTO.name())))
                .andExpect(jsonPath("$.phoneNumber", is(btc.VALID_BARBERSHOP_DTO.phoneNumber())))
                .andExpect(jsonPath("$.email", is(btc.VALID_BARBERSHOP_DTO.email())))
                .andExpect(jsonPath("$.workTimeFrom", is(
                        btc.VALID_BARBERSHOP_DTO.workTimeFrom().format(DateTimeFormatter.ISO_LOCAL_TIME)
                        )
                ))
                .andExpect(jsonPath("$.workTimeTo", is(
                        btc.VALID_BARBERSHOP_DTO.workTimeTo().format(DateTimeFormatter.ISO_LOCAL_TIME)
                        )
                ))
                .andExpect(jsonPath("$", aMapWithSize(btc.BARBERSHOP_FIELD_AMOUNT)));
    }
}
