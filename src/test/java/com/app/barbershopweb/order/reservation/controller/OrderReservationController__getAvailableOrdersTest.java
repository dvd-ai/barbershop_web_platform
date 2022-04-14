package com.app.barbershopweb.order.reservation.controller;

import com.app.barbershopweb.order.crud.OrderConverter;
import com.app.barbershopweb.order.reservation.OrderReservationController;
import com.app.barbershopweb.order.reservation.OrderReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.app.barbershopweb.order.crud.constants.OrderMetadata__TestConstants.ORDER_FIELD_AMOUNT;
import static com.app.barbershopweb.order.reservation.constants.OrderReservation_Metadata__TestConstants.ORDER_RESERVATION_URL;
import static com.app.barbershopweb.order.reservation.constants.dto.OrderReservation_GetOpenOrders_Dto__TestConstants.GET_OPEN_ORDERS__REQUEST_VALID_DTO;
import static com.app.barbershopweb.order.reservation.constants.list.dto.OrderReservation_List_OrderDto__TestConstants.ORDER_RESERVATION_OPEN_ORDER_DTO_LIST;
import static com.app.barbershopweb.order.reservation.constants.list.entity.OrderReservation_List_OrderEntity__TestConstants.ORDER_RESERVATION_OPEN_ORDER_ENTITY_LIST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderReservationController.class)
@DisplayName("TESTING: " + ORDER_RESERVATION_URL + " (OrderReservationController.getAvailableOrders)")
@ExtendWith(MockitoExtension.class)
class OrderReservationController__getAvailableOrdersTest {

    @MockBean
    OrderReservationService orderReservationService;

    @MockBean
    OrderConverter orderConverter;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    
    @Test
    @DisplayName(
            """
                    When there's no unreserved orders for week returns empty list, 
                    status code 200"
                    """
    )
    void shouldReturnEmptyUnreservedOrdersList() throws Exception {
        String json = objectMapper.writeValueAsString(
                GET_OPEN_ORDERS__REQUEST_VALID_DTO
        );

        when(orderReservationService
                .getAvailableOrders(
                        GET_OPEN_ORDERS__REQUEST_VALID_DTO.barbershopId(),
                        GET_OPEN_ORDERS__REQUEST_VALID_DTO.startWeekDate()
                )
        )
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
                    When there are unreserved orders for week returns the orders, 
                    status code 200"
                    """
    )
    void shouldReturnUnreservedOrdersList() throws Exception {
        String json = objectMapper.writeValueAsString(
                GET_OPEN_ORDERS__REQUEST_VALID_DTO
        );

        when(orderReservationService.
                getAvailableOrders(
                        GET_OPEN_ORDERS__REQUEST_VALID_DTO.barbershopId(),
                        GET_OPEN_ORDERS__REQUEST_VALID_DTO.startWeekDate()
                )
        )
                .thenReturn(ORDER_RESERVATION_OPEN_ORDER_ENTITY_LIST);
        when(orderConverter.
                orderEntityListToDtoList(
                        ORDER_RESERVATION_OPEN_ORDER_ENTITY_LIST
                )
        )
                .thenReturn(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST);

        MockHttpServletResponse response = mockMvc.perform(post(ORDER_RESERVATION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andReturn().getResponse();
        
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        
        DocumentContext context = JsonPath.parse(response.getContentAsString());
        List<Object> object = context.read("$");
        
        assertEquals(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.size(), object.size());

        for (var i = 0; i < ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.size(); i++) {
            Map<String, Object> dto = context.read("$[" + i + "]");
            assertEquals(ORDER_FIELD_AMOUNT, dto.size());
            assertEquals(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(i).orderId().intValue(), (Integer) context.read("$["+ i + "].orderId"));
            assertEquals(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(i).barbershopId().intValue(), (Integer) context.read("$["+ i + "].barbershopId"));
            assertEquals(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(i).barberId().intValue(), (Integer) context.read("$["+ i + "].barberId"));
            assertEquals(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(i).customerId(), context.read("$["+ i + "].customerId"));
            assertEquals(ORDER_RESERVATION_OPEN_ORDER_DTO_LIST.get(i).orderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), context.read("$["+ i + "].orderDate"));
        }
    }


}
