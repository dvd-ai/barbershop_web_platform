package com.app.barbershopweb.barbershop.controller;

import com.app.barbershopweb.barbershop.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BarbershopController.class)
@DisplayName("Testing GET: /barbershops/{barbershopId}")
class BarbershopControllerGetBarbershopByIdTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private BarbershopService barbershopService;

    @MockBean
    private BarbershopConverter barbershopConverter;

    private long barbershopId;

    @DisplayName("When request param input 'barbershopId' isn't valid" +
            " returns status code 400 (BAD_REQUEST) & error dto")
    @Test
    void whenBarbershopIdNotValid() throws Exception {
        barbershopId = -100L;

        mockMvc
                .perform(get("/barbershops/" + barbershopId))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is("'barbershopId' must be greater than or equal to 1")));
    }

    @DisplayName("when there's no barbershop with id 'barbershopId' " +
            "returns status code 404 & error dto")
    @Test
    void whenNotExistedBarbershopId() throws Exception {
        barbershopId = 100_000L;

        mockMvc
                .perform(get("/barbershops/" + barbershopId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is("Barbershop with id " + barbershopId+ " not found.")));
    }

    @DisplayName("returns corresponding barbershop dto")
    @Test
    void shouldReturnBarbershop() throws Exception {
        barbershopId = 1L;

        when(barbershopService.findBarbershopById(any())).thenReturn(
                Optional.of(
                        new Barbershop(1L, "1a",
                                "Barbershop1", "+38091",
                                "1@gmail.com"
                        )
                )
        );
        when(barbershopConverter.mapToDto(any())).thenReturn(
                new BarbershopDto(
                        1L, "A1",
                        "Barbershop1", "+38091",
                        "1@gmail.com"
                )
        );

        mockMvc
                .perform(get("/barbershops/" + barbershopId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",  is(1)))
                .andExpect(jsonPath("$.address", is("A1")))
                .andExpect(jsonPath("$.name", is("Barbershop1")))
                .andExpect(jsonPath("$.phoneNumber", is("+38091")))
                .andExpect(jsonPath("$.email", is("1@gmail.com")));

    }
}
