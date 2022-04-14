package com.app.barbershopweb.order.crud.controller;

import com.app.barbershopweb.order.crud.OrderController;
import com.app.barbershopweb.order.crud.OrderConverter;
import com.app.barbershopweb.order.crud.OrderService;
import com.app.barbershopweb.order.testutils.OrderController__TestUtils;
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

import java.util.Collections;

import static com.app.barbershopweb.order.crud.constants.OrderList__TestConstants.ORDER_VALID_DTO_LIST;
import static com.app.barbershopweb.order.crud.constants.OrderList__TestConstants.ORDER_VALID_ENTITY_LIST;
import static com.app.barbershopweb.order.crud.constants.OrderMetadata__TestConstants.ORDERS_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        OrderController__TestUtils.checkOrderDtoJson(response.getContentAsString(), ORDER_VALID_DTO_LIST, false);
    }

}
