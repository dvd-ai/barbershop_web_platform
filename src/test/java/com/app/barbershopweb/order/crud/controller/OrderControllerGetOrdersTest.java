package com.app.barbershopweb.order.crud.controller;

import com.app.barbershopweb.order.crud.OrderController;
import com.app.barbershopweb.order.crud.OrderConverter;
import com.app.barbershopweb.order.crud.OrderService;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.app.barbershopweb.order.crud.constants.OrderList__TestConstants.ORDER_VALID_DTO_LIST;
import static com.app.barbershopweb.order.crud.constants.OrderList__TestConstants.ORDER_VALID_ENTITY_LIST;
import static com.app.barbershopweb.order.crud.constants.OrderMetadata__TestConstants.ORDERS_URL;
import static com.app.barbershopweb.order.crud.constants.OrderMetadata__TestConstants.ORDER_FIELD_AMOUNT;
import static com.app.barbershopweb.order.reservation.constants.list.dto.OrderReservation_List_OrderDto__TestConstants.ORDER_RESERVATION_OPEN_ORDER_DTO_LIST;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

        MockHttpServletResponse response = mockMvc.
                perform(get(ORDERS_URL)).
                andDo(print()).andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        DocumentContext context = JsonPath.parse(response.getContentAsString());
        List<Object> object = context.read("$");

        assertEquals(ORDER_VALID_DTO_LIST.size(), object.size());

        for (var i = 0; i < ORDER_VALID_DTO_LIST.size(); i++) {
            Map<String, Object> dto = context.read("$[" + i + "]");
            assertEquals(ORDER_FIELD_AMOUNT, dto.size());
            assertEquals(ORDER_VALID_DTO_LIST.get(i).orderId().intValue(), (Integer) context.read("$[" + i + "].orderId"));
            assertEquals(ORDER_VALID_DTO_LIST.get(i).barbershopId().intValue(), (Integer) context.read("$[" + i + "].barbershopId"));
            assertEquals(ORDER_VALID_DTO_LIST.get(i).barberId().intValue(), (Integer) context.read("$[" + i + "].barberId"));
            assertEquals(ORDER_VALID_DTO_LIST.get(i).customerId().intValue(), (Integer) context.read("$[" + i + "].customerId"));
            assertEquals(ORDER_VALID_DTO_LIST.get(i).orderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), context.read("$[" + i + "].orderDate"));
            assertEquals(ORDER_VALID_DTO_LIST.get(i).active(), context.read("$[" + i + "].active"));
        }
    }
}
