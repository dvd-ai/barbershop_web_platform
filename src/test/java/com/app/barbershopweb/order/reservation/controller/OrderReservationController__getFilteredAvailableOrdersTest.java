package com.app.barbershopweb.order.reservation.controller;

import com.app.barbershopweb.order.crud.OrderConverter;
import com.app.barbershopweb.order.reservation.OrderReservationController;
import com.app.barbershopweb.order.reservation.OrderReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static com.app.barbershopweb.order.crud.constants.OrderMetadata__TestConstants.ORDER_FIELD_AMOUNT;
import static com.app.barbershopweb.order.reservation.constants.OrderReservation_Metadata__TestConstants.ORDER_RESERVATION_FILTER_URL;
import static com.app.barbershopweb.order.reservation.constants.OrderReservation_Metadata__TestConstants.ORDER_RESERVATION_URL;
import static com.app.barbershopweb.order.reservation.constants.dto.OrderReservation_GetOpenOrders_Filtered_Dto__TestConstants.GET_OPEN_FILTERED_ORDERS_NO_FILTERS__REQUEST_DTO;
import static com.app.barbershopweb.order.reservation.constants.dto.OrderReservation_GetOpenOrders_Filtered_Dto__TestConstants.GET_OPEN_FILTERED_ORDERS__REQUEST_DTO;
import static com.app.barbershopweb.order.reservation.constants.list.dto.OrderReservation_List_OrderDto__TestConstants.ORDER_RESERVATION_OPEN_FILTERED_ORDER_DTO_LIST;
import static com.app.barbershopweb.order.reservation.constants.list.dto.OrderReservation_List_OrderDto__TestConstants.ORDER_RESERVATION_OPEN_ORDER_DTO_LIST;
import static com.app.barbershopweb.order.reservation.constants.list.entity.OrderReservation_List_OrderEntity__TestConstants.ORDER_RESERVATION_OPEN_FILTERED_ORDER_ENTITY_LIST;
import static com.app.barbershopweb.order.reservation.constants.list.entity.OrderReservation_List_OrderEntity__TestConstants.ORDER_RESERVATION_OPEN_ORDER_ENTITY_LIST;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderReservationController.class)
@DisplayName("TESTING: " + ORDER_RESERVATION_URL + " (OrderReservationController.getFilteredAvailableOrders)")
@ExtendWith(MockitoExtension.class)
class OrderReservationController__getFilteredAvailableOrdersTest {

    @MockBean
    OrderReservationService orderReservationService;

    @MockBean
    OrderConverter orderConverter;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    String json;

    @Test
    @DisplayName(
            """
                    When there's no filtered unreserved orders for week returns empty list, 
                    status code 200"
                    """
    )
    void shouldReturnEmptyUnreservedOrdersList() throws Exception {
        json = objectMapper.writeValueAsString(
                GET_OPEN_FILTERED_ORDERS__REQUEST_DTO
        );

        when(orderReservationService.getFilteredAvailableOrders(
                GET_OPEN_FILTERED_ORDERS__REQUEST_DTO.barbershopId(),
                GET_OPEN_FILTERED_ORDERS__REQUEST_DTO.startWeekDate(),
                GET_OPEN_FILTERED_ORDERS__REQUEST_DTO.orderFilters()
        ))
                .thenReturn(Collections.emptyList());

        when(orderConverter.orderEntityListToDtoList(Collections.emptyList())).
                thenReturn(Collections.emptyList());

        mockMvc.perform(post(ORDER_RESERVATION_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    @DisplayName(
            """
                    returns unfiltered unreserved orders if the filters weren't specified, 
                    status code 200"
                    """
    )
    void shouldReturnUnfilteredUnreservedOrdersList() throws Exception {
        json = objectMapper.writeValueAsString(
                GET_OPEN_FILTERED_ORDERS_NO_FILTERS__REQUEST_DTO
        );

        when(orderReservationService.getFilteredAvailableOrders(
                        GET_OPEN_FILTERED_ORDERS_NO_FILTERS__REQUEST_DTO.barbershopId(),
                        GET_OPEN_FILTERED_ORDERS_NO_FILTERS__REQUEST_DTO.startWeekDate(),
                        GET_OPEN_FILTERED_ORDERS_NO_FILTERS__REQUEST_DTO.orderFilters()
                )
        )
                .thenReturn(ORDER_RESERVATION_OPEN_ORDER_ENTITY_LIST);
        when(orderConverter.
                orderEntityListToDtoList(
                        ORDER_RESERVATION_OPEN_ORDER_ENTITY_LIST

                )
        )
                .thenReturn(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST);

        mockMvc.perform(post(ORDER_RESERVATION_FILTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].orderId", is(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(0).orderId().intValue())))
                .andExpect(jsonPath("$[0].barbershopId", is(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(0).barbershopId().intValue())))
                .andExpect(jsonPath("$[0].barberId", is(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(0).barberId().intValue())))
                .andExpect(jsonPath("$[0].customerId", is(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(0).customerId())))
                .andExpect(jsonPath("$[0].orderDate", is(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(0).orderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[0].active", is(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(0).active())))

                .andExpect(jsonPath("$[1].orderId", is(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(1).orderId().intValue())))
                .andExpect(jsonPath("$[1].barbershopId", is(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(1).barbershopId().intValue())))
                .andExpect(jsonPath("$[1].barberId", is(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(1).barberId().intValue())))
                .andExpect(jsonPath("$[1].customerId", is(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(1).customerId())))
                .andExpect(jsonPath("$[1].orderDate", is(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(1).orderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[1].active", is(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(1).active())))

                .andExpect(jsonPath("$[2].orderId", is(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(2).orderId().intValue())))
                .andExpect(jsonPath("$[2].barbershopId", is(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(2).barbershopId().intValue())))
                .andExpect(jsonPath("$[2].barberId", is(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(2).barberId().intValue())))
                .andExpect(jsonPath("$[2].customerId", is(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(2).customerId())))
                .andExpect(jsonPath("$[2].orderDate", is(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(2).orderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[2].active", is(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(2).active())))

                .andExpect(jsonPath("$[3].orderId", is(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(3).orderId().intValue())))
                .andExpect(jsonPath("$[3].barbershopId", is(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(3).barbershopId().intValue())))
                .andExpect(jsonPath("$[3].barberId", is(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(3).barberId().intValue())))
                .andExpect(jsonPath("$[3].customerId", is(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(3).customerId())))
                .andExpect(jsonPath("$[3].orderDate", is(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(3).orderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[3].active", is(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(3).active())))

                .andExpect(jsonPath("$", hasSize(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.size())))
                .andExpect(jsonPath("$[0]", aMapWithSize(ORDER_FIELD_AMOUNT)))
                .andExpect(jsonPath("$[1]", aMapWithSize(ORDER_FIELD_AMOUNT)))
                .andExpect(jsonPath("$[2]", aMapWithSize(ORDER_FIELD_AMOUNT)))
                .andExpect(jsonPath("$[3]", aMapWithSize(ORDER_FIELD_AMOUNT)));

    }

    @Test
    @DisplayName(
            """
                    returns filtered (by barberId) unreserved orders , 
                    status code 200"
                    """
    )
    void shouldReturnFilteredUnreservedOrdersList() throws Exception {
        json = objectMapper.writeValueAsString(
                GET_OPEN_FILTERED_ORDERS__REQUEST_DTO
        );

        when(orderReservationService.
                getFilteredAvailableOrders(
                        GET_OPEN_FILTERED_ORDERS__REQUEST_DTO.barbershopId(),
                        GET_OPEN_FILTERED_ORDERS__REQUEST_DTO.startWeekDate(),
                        GET_OPEN_FILTERED_ORDERS__REQUEST_DTO.orderFilters()
                )
        )
                .thenReturn(ORDER_RESERVATION_OPEN_FILTERED_ORDER_ENTITY_LIST);
        when(orderConverter
                .orderEntityListToDtoList(
                        ORDER_RESERVATION_OPEN_FILTERED_ORDER_ENTITY_LIST
                )
        )
                .thenReturn(ORDER_RESERVATION_OPEN_FILTERED_ORDER_DTO_LIST);

        mockMvc.perform(post(ORDER_RESERVATION_FILTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].orderId", is(ORDER_RESERVATION_OPEN_FILTERED_ORDER_DTO_LIST.get(0).orderId().intValue())))
                .andExpect(jsonPath("$[0].barbershopId", is(ORDER_RESERVATION_OPEN_FILTERED_ORDER_DTO_LIST.get(0).barbershopId().intValue())))
                .andExpect(jsonPath("$[0].barberId", is(ORDER_RESERVATION_OPEN_FILTERED_ORDER_DTO_LIST.get(0).barberId().intValue())))
                .andExpect(jsonPath("$[0].customerId", is(ORDER_RESERVATION_OPEN_FILTERED_ORDER_DTO_LIST.get(0).customerId())))
                .andExpect(jsonPath("$[0].orderDate", is(ORDER_RESERVATION_OPEN_FILTERED_ORDER_DTO_LIST.get(0).orderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[0].active", is(ORDER_RESERVATION_OPEN_FILTERED_ORDER_DTO_LIST.get(0).active())))

                .andExpect(jsonPath("$[1].orderId", is(ORDER_RESERVATION_OPEN_FILTERED_ORDER_DTO_LIST.get(1).orderId().intValue())))
                .andExpect(jsonPath("$[1].barbershopId", is(ORDER_RESERVATION_OPEN_FILTERED_ORDER_DTO_LIST.get(1).barbershopId().intValue())))
                .andExpect(jsonPath("$[1].barberId", is(ORDER_RESERVATION_OPEN_FILTERED_ORDER_DTO_LIST.get(1).barberId().intValue())))
                .andExpect(jsonPath("$[1].customerId", is(ORDER_RESERVATION_OPEN_FILTERED_ORDER_DTO_LIST.get(1).customerId())))
                .andExpect(jsonPath("$[1].orderDate", is(ORDER_RESERVATION_OPEN_FILTERED_ORDER_DTO_LIST.get(1).orderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[1].active", is(ORDER_RESERVATION_OPEN_FILTERED_ORDER_DTO_LIST.get(1).active())))

                .andExpect(jsonPath("$[2].orderId", is(ORDER_RESERVATION_OPEN_FILTERED_ORDER_DTO_LIST.get(2).orderId().intValue())))
                .andExpect(jsonPath("$[2].barbershopId", is(ORDER_RESERVATION_OPEN_FILTERED_ORDER_DTO_LIST.get(2).barbershopId().intValue())))
                .andExpect(jsonPath("$[2].barberId", is(ORDER_RESERVATION_OPEN_FILTERED_ORDER_DTO_LIST.get(2).barberId().intValue())))
                .andExpect(jsonPath("$[2].customerId", is(ORDER_RESERVATION_OPEN_FILTERED_ORDER_DTO_LIST.get(2).customerId())))
                .andExpect(jsonPath("$[2].orderDate", is(ORDER_RESERVATION_OPEN_FILTERED_ORDER_DTO_LIST.get(2).orderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[2].active", is(ORDER_RESERVATION_OPEN_FILTERED_ORDER_DTO_LIST.get(2).active())))

                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0]", aMapWithSize(ORDER_FIELD_AMOUNT)))
                .andExpect(jsonPath("$[1]", aMapWithSize(ORDER_FIELD_AMOUNT)))
                .andExpect(jsonPath("$[2]", aMapWithSize(ORDER_FIELD_AMOUNT)))

        ;

    }


}
