package com.app.barbershopweb.barbershop.closure.controller;

import com.app.barbershopweb.barbershop.closure.BarbershopClosureController;
import com.app.barbershopweb.barbershop.closure.BarbershopClosureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.web.servlet.MockMvc;

import static com.app.barbershopweb.barbershop.crud.constants.BarbershopErrorMessage__TestConstants.BARBERSHOP_ERR_INVALID_PATH_VAR_BARBERSHOP_ID;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopMetadata__TestConstants.BARBERSHOPS_URL;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopMetadata__TestConstants.BARBERSHOP_INVALID_BARBERSHOP_ID;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BarbershopClosureController.class)
@MockBean(AuthenticationProvider.class)
class BarbershopValidatorTest {

    @MockBean
    BarbershopClosureService barbershopClosureService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void deleteBarbershop_invalidId() throws Exception {
        mockMvc.perform(delete(BARBERSHOPS_URL + "/" + BARBERSHOP_INVALID_BARBERSHOP_ID))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(
                        BARBERSHOP_ERR_INVALID_PATH_VAR_BARBERSHOP_ID)
                ));
    }
}
