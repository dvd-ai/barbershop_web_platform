package com.app.barbershopweb.order.crud.dto;

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

import static com.app.barbershopweb.order.crud.constants.OrderDto__TestConstants.ORDER_INVALID_DTO;
import static com.app.barbershopweb.order.crud.constants.OrderMetadata__TestConstants.ORDERS_URL;
import static com.app.barbershopweb.order.crud.constants.error.OrderErrorMessage_Dto__TestConstants.*;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderDtoTest {


    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    @MockBean
    OrderConverter orderConverter;

    @Autowired
    ObjectMapper objectMapper;


    String json;

    @DisplayName(
            "Testing POST: " + ORDERS_URL +
                    "when order dto isn't valid " +
                    "returns status code 400 & error dto")
    @Test
    void addOrder__whenOrderDtoNotValid() throws Exception {
        json = objectMapper.writeValueAsString(
                ORDER_INVALID_DTO
        );

        mockMvc
                .perform(post(ORDERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(4)))
                .andExpect(jsonPath("$.errors", hasItem(ORDER_ERR_INVALID_DTO_BARBERSHOP_ID)))
                .andExpect(jsonPath("$.errors", hasItem(ORDER_ERR_INVALID_DTO_ORDER_ID)))
                .andExpect(jsonPath("$.errors", hasItem(ORDER_ERR_INVALID_DTO_CUSTOMER_ID)))
                .andExpect(jsonPath("$.errors", hasItem(ORDER_ERR_INVALID_DTO_BARBER_ID)));
    }

    @DisplayName(
            "Testing PUT: " + ORDERS_URL +
                    " when order dto isn't valid " +
                    "returns status code 400 & error dto")
    @Test
    void whenOrderDtoNotValid() throws Exception {
        String json = objectMapper.writeValueAsString(
                ORDER_INVALID_DTO
        );

        mockMvc
                .perform(put(ORDERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(4)))
                .andExpect(jsonPath("$.errors", hasItem(ORDER_ERR_INVALID_DTO_BARBERSHOP_ID)))
                .andExpect(jsonPath("$.errors", hasItem(ORDER_ERR_INVALID_DTO_ORDER_ID)))
                .andExpect(jsonPath("$.errors", hasItem(ORDER_ERR_INVALID_DTO_CUSTOMER_ID)))
                .andExpect(jsonPath("$.errors", hasItem(ORDER_ERR_INVALID_DTO_BARBER_ID)));
    }


}