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

import static com.app.barbershopweb.order.crud.constants.OrderMetadata__TestConstants.*;
import static com.app.barbershopweb.order.crud.constants.error.OrderErrorMessage_PathVar__TestConstants.ORDER_ERR_INVALID_PATH_VAR_ORDER_ID;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(OrderController.class)
@DisplayName("Testing DELETE: " + ORDERS_URL + "{orderId}")
@ExtendWith(MockitoExtension.class)
class OrderControllerDeleteTest {

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
                .perform(delete(ORDERS_URL + "/" + ORDER_INVALID_ORDER_ID))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(ORDER_ERR_INVALID_PATH_VAR_ORDER_ID)));
    }

    @DisplayName("returns empty body, status code 200, " +
            "when: order with existing / not existing id was deleted")
    @Test
    void shouldDeleteOrderById() throws Exception {


        mockMvc
                .perform(delete(ORDERS_URL + "/" + ORDER_VALID_ORDER_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

        verify(orderService, times(1))
                .deleteOrder(ORDER_VALID_ORDER_ID);
    }

}
