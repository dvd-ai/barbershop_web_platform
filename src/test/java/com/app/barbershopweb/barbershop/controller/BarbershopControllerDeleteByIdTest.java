package com.app.barbershopweb.barbershop.controller;

import com.app.barbershopweb.barbershop.BarbershopController;
import com.app.barbershopweb.barbershop.BarbershopConverter;
import com.app.barbershopweb.barbershop.BarbershopService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BarbershopController.class)
@DisplayName("Testing DELETE: /barbershops/{barbershopId}")
class BarbershopControllerDeleteByIdTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BarbershopService barbershopService;

    @MockBean
    BarbershopConverter barbershopConverter;

    private long barbershopId;


    @DisplayName("When request param input 'barbershopId' isn't valid" +
            " returns status code 400 (BAD_REQUEST) & error dto")
    @Test
    void whenBarbershopIdNotValid() throws Exception {
        barbershopId = -100L;

        mockMvc
                .perform(delete("/barbershops/" + barbershopId))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is("'barbershopId' must be greater than or equal to 1")));
    }

    @DisplayName("returns empty body, status code 200, " +
            "when: barbershop with existing / not existing id was deleted")
    @Test
    void shouldDeleteBarbershopById() throws Exception {
        barbershopId = 1L;

        mockMvc
                .perform(delete("/barbershops/" + barbershopId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }
}
