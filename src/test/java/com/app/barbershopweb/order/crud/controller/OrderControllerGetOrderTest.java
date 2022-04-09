package com.app.barbershopweb.order.crud.controller;

import com.app.barbershopweb.order.crud.OrderController;
import com.app.barbershopweb.order.crud.OrderConverter;
import com.app.barbershopweb.order.crud.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.app.barbershopweb.order.crud.constants.OrderDto__TestConstants.ORDER_VALID_DTO;
import static com.app.barbershopweb.order.crud.constants.OrderEntity__TestConstants.ORDER_VALID_ENTITY;
import static com.app.barbershopweb.order.crud.constants.OrderMetadata__TestConstants.*;
import static com.app.barbershopweb.order.crud.constants.error.OrderErrorMessage_PathVar__TestConstants.ORDER_ERR_INVALID_PATH_VAR_ORDER_ID;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(OrderController.class)
@DisplayName("Testing GET: " + ORDERS_URL + "/" + "{orderId}")
@ExtendWith(MockitoExtension.class)
class OrderControllerGetOrderTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    @MockBean
    OrderConverter orderConverter;


    @DisplayName("When path variable input 'orderId' isn't valid" +
            " returns status code 400 (BAD_REQUEST) & error dto")
    @Test
    void whenOrderIdNotValid() throws Exception {
        mockMvc
                .perform(get(ORDERS_URL + "/" + ORDER_INVALID_ORDER_ID))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(ORDER_ERR_INVALID_PATH_VAR_ORDER_ID)));
    }

    @DisplayName("when there's no order with id 'orderId' " +
            "returns status code 404 & error dto")
    @Test
    void whenNotExistedOrderId() throws Exception {
        mockMvc
                .perform(get(ORDERS_URL + "/" + ORDER_NOT_EXISTING_ORDER_ID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(
                        "Order with orderId '" + ORDER_NOT_EXISTING_ORDER_ID + "' not found.")
                ));
    }

    @DisplayName("returns corresponding order dto")
    @Test
    void shouldReturnOrder() throws Exception {
        when(orderService.findOrder(ORDER_VALID_ORDER_ID)).thenReturn(
                Optional.of(ORDER_VALID_ENTITY)
        );
        when(orderConverter.mapToDto(ORDER_VALID_ENTITY)).thenReturn(
                ORDER_VALID_DTO
        );

        mockMvc
                .perform(get(ORDERS_URL + "/" + ORDER_VALID_ORDER_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.barbershopId", is(ORDER_VALID_DTO.barbershopId().intValue())))
                .andExpect(jsonPath("$.customerId", is(ORDER_VALID_DTO.customerId().intValue())))
                .andExpect(jsonPath("$.barberId", is(ORDER_VALID_DTO.barberId().intValue())))
                .andExpect(jsonPath("$.orderId", is(ORDER_VALID_DTO.orderId().intValue())))
                .andExpect(jsonPath("$.active", is(ORDER_VALID_DTO.active())))
                .andExpect(jsonPath("$.orderDate", is(ORDER_VALID_DTO.orderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))

                .andExpect(jsonPath("$", aMapWithSize(ORDER_FIELD_AMOUNT)));
    }
}
