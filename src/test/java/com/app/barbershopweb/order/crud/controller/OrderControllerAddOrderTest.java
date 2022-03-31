package com.app.barbershopweb.order.crud.controller;

import com.app.barbershopweb.exception.DbUniqueConstraintsViolationException;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.order.crud.OrderController;
import com.app.barbershopweb.order.crud.OrderConverter;
import com.app.barbershopweb.order.crud.OrderService;
import com.app.barbershopweb.order.crud.OrderTestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.app.barbershopweb.order.crud.OrderTestConstants.ORDERS_URL;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@DisplayName("Testing POST: " + ORDERS_URL)
class OrderControllerAddOrderTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    @MockBean
    OrderConverter orderConverter;

    @Autowired
    ObjectMapper objectMapper;

    private final OrderTestConstants otc = new OrderTestConstants();

    @DisplayName("when order dto isn't valid " +
            "returns status code 400 & error dto")
    @Test
    void whenOrderDtoNotValid() throws Exception {
        String json = objectMapper.writeValueAsString(
                otc.INVALID_ORDER_DTO
        );

        mockMvc
                .perform(post(ORDERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(4)))
                .andExpect(jsonPath("$.errors", hasItem(otc.DTO_CV_BARBERSHOP_ID_ERR_MSG)))
                .andExpect(jsonPath("$.errors", hasItem(otc.DTO_CV_ORDER_ID_ERR_MSG)))
                .andExpect(jsonPath("$.errors", hasItem(otc.DTO_CV_CUSTOMER_ID_ERR_MSG)))
                .andExpect(jsonPath("$.errors", hasItem(otc.DTO_CV_BARBER_ID_ERR_MSG)));
    }

    @DisplayName("when order dto violates db fk constraints " +
            "returns status code 404 & error dto")
    @Test
    void whenOrderDtoViolatesDbFkConstraints() throws Exception {
        String json = objectMapper.writeValueAsString(
                otc.VALID_ORDER_DTO
        );

        when(orderConverter.mapToEntity(any())).thenReturn(
                otc.VALID_ORDER_ENTITY);

        when(orderService.addOrder(any())).thenThrow(
                new NotFoundException(
                        List.of(
                                otc.FK_CV_BARBER_ID_ERR_MSG,
                                otc.FK_CV_CUSTOMER_ID_ERR_MSG
                        )
                )
        );

        mockMvc
                .perform(post(ORDERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors", hasItem(otc.FK_CV_BARBER_ID_ERR_MSG)))
                .andExpect(jsonPath("$.errors", hasItem(otc.FK_CV_CUSTOMER_ID_ERR_MSG)));
    }

    @DisplayName("when order dto violates db uk constraints " +
            "returns status code 400 & error dto")
    @Test
    void whenOrderDtoViolatesDbUkConstraints() throws Exception {
        String json = objectMapper.writeValueAsString(
                otc.VALID_ORDER_DTO
        );

        when(orderConverter.mapToEntity(any())).thenReturn(
                otc.VALID_ORDER_ENTITY);

        when(orderService.addOrder(any())).thenThrow(
                new DbUniqueConstraintsViolationException(
                        List.of(
                                otc.UK_CV_BARBER_ID_ORDER_DATE_ERR_MSG,
                                otc.UK_CV_CUSTOMER_ID_ORDER_DATE_ERR_MSG
                        )
                )
        );

        mockMvc
                .perform(post(ORDERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors", hasItem(otc.UK_CV_BARBER_ID_ORDER_DATE_ERR_MSG)))
                .andExpect(jsonPath("$.errors", hasItem(otc.UK_CV_CUSTOMER_ID_ORDER_DATE_ERR_MSG)));
    }

    @DisplayName("when order dto violates business data format " +
            "returns status code 400 & error dto")
    @Test
    void whenOrderDtoViolatesBusinessDataFormat() throws Exception {
        String json = objectMapper.writeValueAsString(
                otc.VALID_ORDER_DTO
        );

        when(orderConverter.mapToEntity(any())).thenReturn(
                otc.VALID_ORDER_ENTITY);

        when(orderService.addOrder(any())).thenThrow(
                new DbUniqueConstraintsViolationException(
                        List.of(
                                otc.BDF_CV_TIME_FORMAT_ERR_MSG,
                                otc.BDF_CV_BARBERSHOP_HOURS_ERR_MSG,
                                otc.BDF_CV_CUSTOMER_ID_BARBER_ID_EQ_ERR_MSG
                        )
                )
        );

        mockMvc
                .perform(post(ORDERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors", hasItem(otc.BDF_CV_TIME_FORMAT_ERR_MSG)))
                .andExpect(jsonPath("$.errors", hasItem(otc.BDF_CV_BARBERSHOP_HOURS_ERR_MSG)))
                .andExpect(jsonPath("$.errors", hasItem(otc.BDF_CV_CUSTOMER_ID_BARBER_ID_EQ_ERR_MSG)));
    }



    @DisplayName("after saving order entity returns its id and status code 201")
    @Test
    void shouldAddOrder() throws Exception {
        String json = objectMapper.writeValueAsString(
                otc.VALID_ORDER_DTO
        );

        when(orderConverter.mapToEntity(any())).thenReturn(
                otc.VALID_ORDER_ENTITY
        );
        when(orderService.addOrder(any())).thenReturn(
                otc.VALID_ORDER_ID
        );

        mockMvc
                .perform(post(ORDERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(String.valueOf(otc.VALID_ORDER_ID)));
    }
}
