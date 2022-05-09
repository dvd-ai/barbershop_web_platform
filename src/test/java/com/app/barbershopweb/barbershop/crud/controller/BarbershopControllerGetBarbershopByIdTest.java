package com.app.barbershopweb.barbershop.crud.controller;

import com.app.barbershopweb.barbershop.crud.BarbershopController;
import com.app.barbershopweb.barbershop.crud.BarbershopConverter;
import com.app.barbershopweb.barbershop.crud.BarbershopService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.app.barbershopweb.barbershop.crud.constants.BarbershopDto__TestConstants.BARBERSHOP_VALID_DTO;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopEntity__TestConstants.BARBERSHOP_VALID_ENTITY;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopErrorMessage__TestConstants.BARBERSHOP_ERR_INVALID_PATH_VAR_BARBERSHOP_ID;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopMetadata__TestConstants.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BarbershopController.class)
@DisplayName("Testing GET: " + BARBERSHOPS_URL + "/" + "{barbershopId}")
@ExtendWith(MockitoExtension.class)
@MockBean(AuthenticationProvider.class)
class BarbershopControllerGetBarbershopByIdTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BarbershopService barbershopService;

    @MockBean
    BarbershopConverter barbershopConverter;


    @DisplayName("When path variable input 'barbershopId' isn't valid" +
            " returns status code 400 (BAD_REQUEST) & error dto")
    @Test
    @WithMockUser
    void whenBarbershopIdNotValid() throws Exception {
        mockMvc
                .perform(get(BARBERSHOPS_URL + "/" + BARBERSHOP_INVALID_BARBERSHOP_ID))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(BARBERSHOP_ERR_INVALID_PATH_VAR_BARBERSHOP_ID)));
    }

    @DisplayName("when there's no barbershop with id 'barbershopId' " +
            "returns status code 404 & error dto")
    @Test
    @WithMockUser
    void whenNotExistedBarbershopId() throws Exception {
        mockMvc
                .perform(get(BARBERSHOPS_URL + "/" + BARBERSHOP_NOT_EXISTED_BARBERSHOP_ID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(
                        "Barbershop with id '" + BARBERSHOP_NOT_EXISTED_BARBERSHOP_ID + "' not found.")
                ));
    }

    @DisplayName("returns corresponding barbershop dto")
    @Test
    @WithMockUser
    void shouldReturnBarbershop() throws Exception {
        when(barbershopService.findBarbershopById(BARBERSHOP_VALID_BARBERSHOP_ID)).thenReturn(
                Optional.of(BARBERSHOP_VALID_ENTITY)
        );
        when(barbershopConverter.mapToDto(BARBERSHOP_VALID_ENTITY)).thenReturn(
                BARBERSHOP_VALID_DTO
        );

        mockMvc
                .perform(get(BARBERSHOPS_URL + "/" + BARBERSHOP_VALID_BARBERSHOP_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(BARBERSHOP_VALID_DTO.id().intValue())))
                .andExpect(jsonPath("$.address", is(BARBERSHOP_VALID_DTO.address())))
                .andExpect(jsonPath("$.name", is(BARBERSHOP_VALID_DTO.name())))
                .andExpect(jsonPath("$.phoneNumber", is(BARBERSHOP_VALID_DTO.phoneNumber())))
                .andExpect(jsonPath("$.email", is(BARBERSHOP_VALID_DTO.email())))
                .andExpect(jsonPath("$.workTimeFrom", is(
                                BARBERSHOP_VALID_DTO.workTimeFrom().format(DateTimeFormatter.ISO_LOCAL_TIME)
                        )
                ))
                .andExpect(jsonPath("$.workTimeTo", is(
                                BARBERSHOP_VALID_DTO.workTimeTo().format(DateTimeFormatter.ISO_LOCAL_TIME)
                        )
                ))
                .andExpect(jsonPath("$.isActive", is(BARBERSHOP_VALID_DTO.isActive())))
                .andExpect(jsonPath("$", aMapWithSize(BARBERSHOP_FIELD_AMOUNT)));
    }
}
