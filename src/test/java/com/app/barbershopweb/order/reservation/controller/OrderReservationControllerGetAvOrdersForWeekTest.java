package com.app.barbershopweb.order.reservation.controller;

import com.app.barbershopweb.order.crud.OrderConverter;
import com.app.barbershopweb.order.crud.OrderTestConstants;
import com.app.barbershopweb.order.reservation.OrderReservationController;
import com.app.barbershopweb.order.reservation.OrderReservationService;
import com.app.barbershopweb.order.reservation.OrderReservationTestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static com.app.barbershopweb.order.reservation.OrderReservationTestConstants.ORDER_RESERV_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderReservationController.class)
@DisplayName("TESTING: " + ORDER_RESERV_URL + " (getBarbershopActiveUnreservedOrdersForWeek method)")
class OrderReservationControllerGetAvOrdersForWeekTest {

    @MockBean
    OrderReservationService orderReservationService;

    @MockBean
    OrderConverter orderConverter;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    OrderReservationTestConstants ortc = new OrderReservationTestConstants();
    OrderTestConstants otc = new OrderTestConstants();

    String json;

    @Test
    @DisplayName(
            """
            When there's no unreserved orders for week returns empty list, 
            status code 200"
            """
    )
    void shouldReturnEmptyUnreservedOrdersList() throws Exception {
        json = objectMapper.writeValueAsString(
                ortc.SUOR_DTO_NO_FILTERS
        );

        when(orderReservationService.getAvailableOrders(any(), any()))
                .thenReturn(Collections.emptyList());

        when(orderConverter.orderEntityListToDtoList(any())).
                thenReturn(Collections.emptyList());

        mockMvc.perform(post(ORDER_RESERV_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    @DisplayName(
            """
            When there are unreserved orders for week returns the orders, 
            status code 200"
            """
    )
    void shouldReturnUnreservedOrdersList() throws Exception {
        json = objectMapper.writeValueAsString(
                ortc.SUOR_DTO_NO_FILTERS
        );

        when(orderReservationService.getAvailableOrders(any(), any()))
                .thenReturn(ortc.UNRESERVED_ORDER_ENTITY_LIST);
        when(orderConverter.orderEntityListToDtoList(any()))
                .thenReturn(ortc.UNRESERVED_ORDER_DTO_LIST);

        mockMvc.perform(post(ORDER_RESERV_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].orderId", is(ortc.UNRESERVED_ORDER_DTO_LIST.get(0).orderId().intValue())))
                .andExpect(jsonPath("$[0].barbershopId", is(ortc.UNRESERVED_ORDER_DTO_LIST.get(0).barbershopId().intValue())))
                .andExpect(jsonPath("$[0].barberId", is(ortc.UNRESERVED_ORDER_DTO_LIST.get(0).barberId().intValue())))
                .andExpect(jsonPath("$[0].customerId", is(ortc.UNRESERVED_ORDER_DTO_LIST.get(0).customerId())))
                .andExpect(jsonPath("$[0].orderDate", is(ortc.UNRESERVED_ORDER_DTO_LIST.get(0).orderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[0].active", is(ortc.UNRESERVED_ORDER_DTO_LIST.get(0).active())))

                .andExpect(jsonPath("$[1].orderId", is(ortc.UNRESERVED_ORDER_DTO_LIST.get(1).orderId().intValue())))
                .andExpect(jsonPath("$[1].barbershopId", is(ortc.UNRESERVED_ORDER_DTO_LIST.get(1).barbershopId().intValue())))
                .andExpect(jsonPath("$[1].barberId", is(ortc.UNRESERVED_ORDER_DTO_LIST.get(1).barberId().intValue())))
                .andExpect(jsonPath("$[1].customerId", is(ortc.UNRESERVED_ORDER_DTO_LIST.get(1).customerId())))
                .andExpect(jsonPath("$[1].orderDate", is(ortc.UNRESERVED_ORDER_DTO_LIST.get(1).orderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[1].active", is(ortc.UNRESERVED_ORDER_DTO_LIST.get(1).active())))

                .andExpect(jsonPath("$[2].orderId", is(ortc.UNRESERVED_ORDER_DTO_LIST.get(2).orderId().intValue())))
                .andExpect(jsonPath("$[2].barbershopId", is(ortc.UNRESERVED_ORDER_DTO_LIST.get(2).barbershopId().intValue())))
                .andExpect(jsonPath("$[2].barberId", is(ortc.UNRESERVED_ORDER_DTO_LIST.get(2).barberId().intValue())))
                .andExpect(jsonPath("$[2].customerId", is(ortc.UNRESERVED_ORDER_DTO_LIST.get(2).customerId())))
                .andExpect(jsonPath("$[2].orderDate", is(ortc.UNRESERVED_ORDER_DTO_LIST.get(2).orderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[2].active", is(ortc.UNRESERVED_ORDER_DTO_LIST.get(2).active())))

                .andExpect(jsonPath("$[3].orderId", is(ortc.UNRESERVED_ORDER_DTO_LIST.get(3).orderId().intValue())))
                .andExpect(jsonPath("$[3].barbershopId", is(ortc.UNRESERVED_ORDER_DTO_LIST.get(3).barbershopId().intValue())))
                .andExpect(jsonPath("$[3].barberId", is(ortc.UNRESERVED_ORDER_DTO_LIST.get(3).barberId().intValue())))
                .andExpect(jsonPath("$[3].customerId", is(ortc.UNRESERVED_ORDER_DTO_LIST.get(3).customerId())))
                .andExpect(jsonPath("$[3].orderDate", is(ortc.UNRESERVED_ORDER_DTO_LIST.get(3).orderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[3].active", is(ortc.UNRESERVED_ORDER_DTO_LIST.get(3).active())))

                .andExpect(jsonPath("$", hasSize(ortc.UNRESERVED_ORDER_DTO_LIST.size())))
                .andExpect(jsonPath("$[0]", aMapWithSize(otc.ORDER_FIELD_AMOUNT)))
                .andExpect(jsonPath("$[1]", aMapWithSize(otc.ORDER_FIELD_AMOUNT)))
                .andExpect(jsonPath("$[2]", aMapWithSize(otc.ORDER_FIELD_AMOUNT)))
                .andExpect(jsonPath("$[3]", aMapWithSize(otc.ORDER_FIELD_AMOUNT)))
        ;

    }

    @Test
    @DisplayName(
            """
            When invalid dto, returns 
            status code 400 and error dto"
            """
    )
    void whenInvalidDto() throws Exception {
        json = objectMapper.writeValueAsString(
                ortc.INVALID_SUOR_DTO_NO_FILTERS
        );

        mockMvc
                .perform(post(ORDER_RESERV_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(ortc.DTO_CV_BARBERSHOP_ID_ERR_MSG)))
                .andExpect(jsonPath("$.errors", hasItem(ortc.DTO_CV_ORDER_FILTERS_ERR_MSG)))
                .andExpect(jsonPath("$.errors", hasItem(ortc.DTO_CV_START_WEEK_DATE_ERR_MSG)))
                ;
    }


}
