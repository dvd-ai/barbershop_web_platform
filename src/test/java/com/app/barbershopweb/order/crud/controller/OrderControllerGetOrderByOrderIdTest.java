package com.app.barbershopweb.order.crud.controller;

import com.app.barbershopweb.order.crud.OrderController;
import com.app.barbershopweb.order.crud.OrderConverter;
import com.app.barbershopweb.order.crud.OrderService;
import com.app.barbershopweb.order.crud.OrderTestConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.app.barbershopweb.order.crud.OrderTestConstants.ORDERS_URL;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(OrderController.class)
@DisplayName("Testing GET: " + ORDERS_URL + "/" + "{orderId}")
class OrderControllerGetOrderByOrderIdTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    @MockBean
    OrderConverter orderConverter;

    OrderTestConstants otc = new OrderTestConstants();

    @DisplayName("When path variable input 'orderId' isn't valid" +
            " returns status code 400 (BAD_REQUEST) & error dto")
    @Test
    void whenOrderIdNotValid() throws Exception {
        mockMvc
                .perform(get(ORDERS_URL + "/" + otc.INVALID_ORDER_ID))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(otc.PV_ORDER_ID_ERR_MSG)));
    }

    @DisplayName("when there's no order with id 'orderId' " +
            "returns status code 404 & error dto")
    @Test
    void whenNotExistedOrderId() throws Exception {
        mockMvc
                .perform(get(ORDERS_URL + "/" + otc.NOT_EXISTING_ORDER_ID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(
                        "Order with orderId '" + otc.NOT_EXISTING_ORDER_ID + "' not found.")
                ));
    }

    @DisplayName("returns corresponding order dto")
    @Test
    void shouldReturnOrder() throws Exception {
        when(orderService.findOrderByOrderId(any())).thenReturn(
                Optional.of(otc.VALID_ORDER_ENTITY)
        );
        when(orderConverter.mapToDto(any())).thenReturn(
                otc.VALID_ORDER_DTO
        );

        mockMvc
                .perform(get(ORDERS_URL+ "/" + otc.VALID_ORDER_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.barbershopId",  is(otc.VALID_ORDER_DTO.barbershopId().intValue())))
                .andExpect(jsonPath("$.customerId", is(otc.VALID_ORDER_DTO.customerId().intValue())))
                .andExpect(jsonPath("$.barberId", is(otc.VALID_ORDER_DTO.barberId().intValue())))
                .andExpect(jsonPath("$.orderId", is(otc.VALID_ORDER_DTO.orderId().intValue())))
                .andExpect(jsonPath("$.active", is(otc.VALID_ORDER_DTO.active())))
                .andExpect(jsonPath("$.orderDate", is(otc.VALID_ORDER_DTO.orderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))

                .andExpect(jsonPath("$", aMapWithSize(otc.ORDER_FIELD_AMOUNT)));
    }
}
