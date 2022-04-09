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

import static com.app.barbershopweb.barbershop.constants.BarbershopErrorMessage__TestConstants.BARBERSHOP_ERR_INVALID_PATH_VAR_BARBERSHOP_ID;
import static com.app.barbershopweb.barbershop.constants.BarbershopMetadata__TestConstants.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BarbershopController.class)
@DisplayName("Testing DELETE: " + BARBERSHOPS_URL + "{barbershopId}")
class BarbershopControllerDeleteByIdTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BarbershopService barbershopService;

    @MockBean
    BarbershopConverter barbershopConverter;


    @DisplayName("When path variable input 'barbershopId' isn't valid" +
            " returns status code 400 (BAD_REQUEST) & error dto")
    @Test
    void whenBarbershopIdNotValid() throws Exception {


        mockMvc
                .perform(delete(BARBERSHOPS_URL + "/" + BARBERSHOP_INVALID_BARBERSHOP_ID))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(BARBERSHOP_ERR_INVALID_PATH_VAR_BARBERSHOP_ID)));
    }

    @DisplayName("returns empty body, status code 200, " +
            "when: barbershop with existing / not existing id was deleted")
    @Test
    void shouldDeleteBarbershopById() throws Exception {
        mockMvc
                .perform(delete(BARBERSHOPS_URL + "/" + BARBERSHOP_VALID_BARBERSHOP_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }
}
