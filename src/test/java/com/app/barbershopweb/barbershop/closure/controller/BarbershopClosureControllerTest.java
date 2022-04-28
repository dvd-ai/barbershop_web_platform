package com.app.barbershopweb.barbershop.closure.controller;

import com.app.barbershopweb.barbershop.closure.BarbershopClosureController;
import com.app.barbershopweb.barbershop.closure.BarbershopClosureService;
import com.app.barbershopweb.exception.MailException;
import com.app.barbershopweb.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.app.barbershopweb.barbershop.closure.constants.BarbershopClosure_ErrorMessage__TestConstants.BARBERSHOP_CLOSURE_ERR_BARBERSHOP_NOT_FOUND;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopMetadata__TestConstants.BARBERSHOPS_URL;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopMetadata__TestConstants.BARBERSHOP_VALID_BARBERSHOP_ID;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BarbershopClosureController.class)
class BarbershopClosureControllerTest {

    @MockBean
    BarbershopClosureService barbershopClosureService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void deleteBarbershop_barbershopNotExist() throws Exception {
        doThrow(
                new NotFoundException(
                        List.of(BARBERSHOP_CLOSURE_ERR_BARBERSHOP_NOT_FOUND)
                )
        ).when(barbershopClosureService).outOfBusiness(BARBERSHOP_VALID_BARBERSHOP_ID);

        mockMvc.perform(delete(BARBERSHOPS_URL + "/" + BARBERSHOP_VALID_BARBERSHOP_ID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(
                        BARBERSHOP_CLOSURE_ERR_BARBERSHOP_NOT_FOUND)
                ));

    }

    @Test
    void deleteBarbershop_MailException() throws Exception {
        doThrow(
                new MailException(
                        List.of("")
                )
        ).when(barbershopClosureService).outOfBusiness(BARBERSHOP_VALID_BARBERSHOP_ID);

        mockMvc.perform(delete(BARBERSHOPS_URL + "/" + BARBERSHOP_VALID_BARBERSHOP_ID))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
        ;
    }

    @Test
    @DisplayName("deletes barbershop successfully")
    void deleteBarbershop() throws Exception {
        mockMvc.perform(delete(BARBERSHOPS_URL + "/" + BARBERSHOP_VALID_BARBERSHOP_ID))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }


}