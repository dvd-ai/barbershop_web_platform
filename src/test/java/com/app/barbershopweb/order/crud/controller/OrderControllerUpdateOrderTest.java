package com.app.barbershopweb.order.crud.controller;

import com.app.barbershopweb.exception.DbUniqueConstraintsViolationException;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.order.crud.OrderController;
import com.app.barbershopweb.order.crud.OrderConverter;
import com.app.barbershopweb.order.crud.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.app.barbershopweb.order.crud.constants.OrderDto__TestConstants.ORDER_VALID_UPDATED_DTO;
import static com.app.barbershopweb.order.crud.constants.OrderEntity__TestConstants.ORDER_VALID_UPDATED_ENTITY;
import static com.app.barbershopweb.order.crud.constants.OrderMetadata__TestConstants.ORDERS_URL;
import static com.app.barbershopweb.order.crud.constants.OrderMetadata__TestConstants.ORDER_FIELD_AMOUNT;
import static com.app.barbershopweb.order.crud.constants.error.OrderErrorMessage_Business_TestConstants.*;
import static com.app.barbershopweb.order.crud.constants.error.OrderErrorMessage_Fk__TestConstants.ORDER_ERR_FK_BARBER_ID;
import static com.app.barbershopweb.order.crud.constants.error.OrderErrorMessage_Fk__TestConstants.ORDER_ERR_FK_CUSTOMER_ID;
import static com.app.barbershopweb.order.crud.constants.error.OrderErrorMessage_Uk__TestConstants.ORDER_ERR_UK_BARBER_ID__ORDER_DATE;
import static com.app.barbershopweb.order.crud.constants.error.OrderErrorMessage_Uk__TestConstants.ORDER_ERR_UK_CUSTOMER_ID__ORDER_DATE;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(OrderController.class)
@DisplayName("Testing PUT: " + ORDERS_URL)
class OrderControllerUpdateOrderTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    @MockBean
    OrderConverter orderConverter;

    @Autowired
    ObjectMapper objectMapper;


    @DisplayName("when order dto violates db fk constraints " +
            "returns status code 404 & error dto")
    @Test
    void whenOrderDtoViolatesDbFkConstraints() throws Exception {
        String json = objectMapper.writeValueAsString(
                ORDER_VALID_UPDATED_DTO
        );

        when(orderConverter.mapToEntity(ORDER_VALID_UPDATED_DTO))
                .thenReturn(
                        ORDER_VALID_UPDATED_ENTITY
                );

        when(orderService.updateOrder(ORDER_VALID_UPDATED_ENTITY)).thenThrow(
                new NotFoundException(
                        List.of(
                                ORDER_ERR_FK_BARBER_ID,
                                ORDER_ERR_FK_CUSTOMER_ID
                        )
                )
        );

        mockMvc
                .perform(put(ORDERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors", hasItem(ORDER_ERR_FK_BARBER_ID)))
                .andExpect(jsonPath("$.errors", hasItem(ORDER_ERR_FK_CUSTOMER_ID)));
    }

    @DisplayName("when order dto violates db uk constraints " +
            "returns status code 400 & error dto")
    @Test
    void whenOrderDtoViolatesDbUkConstraints() throws Exception {
        String json = objectMapper.writeValueAsString(
                ORDER_VALID_UPDATED_DTO
        );

        when(orderConverter.mapToEntity(ORDER_VALID_UPDATED_DTO)).thenReturn(
                ORDER_VALID_UPDATED_ENTITY);

        when(orderService.updateOrder(ORDER_VALID_UPDATED_ENTITY)).thenThrow(
                new DbUniqueConstraintsViolationException(
                        List.of(
                                ORDER_ERR_UK_BARBER_ID__ORDER_DATE,
                                ORDER_ERR_UK_CUSTOMER_ID__ORDER_DATE
                        )
                )
        );

        mockMvc
                .perform(put(ORDERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors", hasItem(ORDER_ERR_UK_BARBER_ID__ORDER_DATE)))
                .andExpect(jsonPath("$.errors", hasItem(ORDER_ERR_UK_CUSTOMER_ID__ORDER_DATE)));
    }

    @DisplayName("when order dto violates business data format " +
            "returns status code 400 & error dto")
    @Test
    void whenOrderDtoViolatesBusinessDataFormat() throws Exception {
        String json = objectMapper.writeValueAsString(
                ORDER_VALID_UPDATED_DTO
        );

        when(orderConverter.mapToEntity(ORDER_VALID_UPDATED_DTO)).thenReturn(
                ORDER_VALID_UPDATED_ENTITY);

        when(orderService.updateOrder(ORDER_VALID_UPDATED_ENTITY)).thenThrow(
                new DbUniqueConstraintsViolationException(
                        List.of(
                                ORDER_ERR_BUSINESS_TIME_FORMAT,
                                ORDER_ERR_BUSINESS_BARBERSHOP_HOURS,
                                ORDER_ERR_BUSINESS_CUSTOMER_ID_BARBER_ID
                        )
                )
        );

        mockMvc
                .perform(put(ORDERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors", hasItem(ORDER_ERR_BUSINESS_TIME_FORMAT)))
                .andExpect(jsonPath("$.errors", hasItem(ORDER_ERR_BUSINESS_BARBERSHOP_HOURS)))
                .andExpect(jsonPath("$.errors", hasItem(ORDER_ERR_BUSINESS_CUSTOMER_ID_BARBER_ID)));
    }


    @DisplayName("after saving order entity returns its id and status code 201")
    @Test
    void shouldAddOrder() throws Exception {
        String json = objectMapper.writeValueAsString(
                ORDER_VALID_UPDATED_DTO
        );

        when(orderConverter.mapToEntity(ORDER_VALID_UPDATED_DTO)).thenReturn(
                ORDER_VALID_UPDATED_ENTITY
        );
        when(orderService.updateOrder(ORDER_VALID_UPDATED_ENTITY)).thenReturn(
                Optional.of(ORDER_VALID_UPDATED_ENTITY)
        );
        when(orderConverter.mapToDto(ORDER_VALID_UPDATED_ENTITY)).thenReturn(
                ORDER_VALID_UPDATED_DTO
        );

        mockMvc
                .perform(put(ORDERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId", is(ORDER_VALID_UPDATED_DTO.orderId().intValue())))
                .andExpect(jsonPath("$.barberId", is(ORDER_VALID_UPDATED_DTO.barberId().intValue())))
                .andExpect(jsonPath("$.customerId", is(ORDER_VALID_UPDATED_DTO.customerId().intValue())))
                .andExpect(jsonPath("$.barbershopId", is(ORDER_VALID_UPDATED_DTO.barbershopId().intValue())))
                .andExpect(jsonPath("$.active", is(ORDER_VALID_UPDATED_DTO.active())))
                .andExpect(jsonPath("$.orderDate", is(ORDER_VALID_UPDATED_DTO.orderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))

                .andExpect(jsonPath("$", aMapWithSize(ORDER_FIELD_AMOUNT)));
    }
}
