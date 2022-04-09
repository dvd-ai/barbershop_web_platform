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
import java.util.Collections;

import static com.app.barbershopweb.order.crud.constants.OrderList__TestConstants.ORDER_VALID_DTO_LIST;
import static com.app.barbershopweb.order.crud.constants.OrderList__TestConstants.ORDER_VALID_ENTITY_LIST;
import static com.app.barbershopweb.order.crud.constants.OrderMetadata__TestConstants.ORDERS_URL;
import static com.app.barbershopweb.order.crud.constants.OrderMetadata__TestConstants.ORDER_FIELD_AMOUNT;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@DisplayName("Testing GET: " + ORDERS_URL)
@ExtendWith(MockitoExtension.class)
class OrderControllerGetOrdersTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    @MockBean
    OrderConverter converter;


    @DisplayName("gives empty Order List when they're no added orders yet")
    @Test
    void shouldReturnEmptyOrderList() throws Exception {

        when(orderService.getOrders()).thenReturn(Collections.emptyList());
        when(converter.orderEntityListToDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

        mockMvc.
                perform(get(ORDERS_URL)).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(content().string("[]"));
    }

    @DisplayName("gives all orders at once")
    @Test
    void shouldReturnAllOrders() throws Exception {
        when(orderService.getOrders()).thenReturn(
                ORDER_VALID_ENTITY_LIST
        );
        when(converter.orderEntityListToDtoList(ORDER_VALID_ENTITY_LIST)).thenReturn(
                ORDER_VALID_DTO_LIST
        );

        mockMvc.
                perform(get(ORDERS_URL)).
                andDo(print()).
                andExpect(status().isOk()).

                andExpect(jsonPath("$[0].orderId", is(ORDER_VALID_DTO_LIST.get(0).orderId().intValue()))).
                andExpect(jsonPath("$[0].barbershopId", is(ORDER_VALID_DTO_LIST.get(0).barbershopId().intValue()))).
                andExpect(jsonPath("$[0].barberId", is(ORDER_VALID_DTO_LIST.get(0).barberId().intValue()))).
                andExpect(jsonPath("$[0].customerId", is(ORDER_VALID_DTO_LIST.get(0).customerId().intValue()))).
                andExpect(jsonPath("$[0].orderDate", is(ORDER_VALID_DTO_LIST.get(0).orderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))).
                andExpect(jsonPath("$[0].active", is(ORDER_VALID_DTO_LIST.get(0).active()))).

                andExpect(jsonPath("$[1].orderId", is(ORDER_VALID_DTO_LIST.get(1).orderId().intValue()))).
                andExpect(jsonPath("$[1].barbershopId", is(ORDER_VALID_DTO_LIST.get(1).barbershopId().intValue()))).
                andExpect(jsonPath("$[1].barberId", is(ORDER_VALID_DTO_LIST.get(1).barberId().intValue()))).
                andExpect(jsonPath("$[1].customerId", is(ORDER_VALID_DTO_LIST.get(1).customerId().intValue()))).
                andExpect(jsonPath("$[1].orderDate", is(ORDER_VALID_DTO_LIST.get(1).orderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))).
                andExpect(jsonPath("$[1].active", is(ORDER_VALID_DTO_LIST.get(1).active()))).

                andExpect(jsonPath("$[2].orderId", is(ORDER_VALID_DTO_LIST.get(2).orderId().intValue()))).
                andExpect(jsonPath("$[2].barbershopId", is(ORDER_VALID_DTO_LIST.get(2).barbershopId().intValue()))).
                andExpect(jsonPath("$[2].barberId", is(ORDER_VALID_DTO_LIST.get(2).barberId().intValue()))).
                andExpect(jsonPath("$[2].customerId", is(ORDER_VALID_DTO_LIST.get(2).customerId().intValue()))).
                andExpect(jsonPath("$[2].orderDate", is(ORDER_VALID_DTO_LIST.get(2).orderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))).
                andExpect(jsonPath("$[2].active", is(ORDER_VALID_DTO_LIST.get(2).active()))).

                andExpect(jsonPath("$", hasSize(ORDER_VALID_DTO_LIST.size()))).
                andExpect(jsonPath("$[0]", aMapWithSize(ORDER_FIELD_AMOUNT))).
                andExpect(jsonPath("$[1]", aMapWithSize(ORDER_FIELD_AMOUNT))).
                andExpect(jsonPath("$[2]", aMapWithSize(ORDER_FIELD_AMOUNT)));

    }
}
