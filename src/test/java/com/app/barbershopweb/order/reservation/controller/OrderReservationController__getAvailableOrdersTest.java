package com.app.barbershopweb.order.reservation.controller;

import com.app.barbershopweb.order.crud.OrderConverter;
import com.app.barbershopweb.order.reservation.OrderReservationController;
import com.app.barbershopweb.order.reservation.OrderReservationService;
import com.app.barbershopweb.order.testutils.OrderController__TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static com.app.barbershopweb.order.reservation.constants.OrderReservation_Metadata__TestConstants.ORDER_RESERVATION_URL;
import static com.app.barbershopweb.order.reservation.constants.dto.OrderReservation_GetOpenOrders_Dto__TestConstants.GET_OPEN_ORDERS__REQUEST_VALID_DTO;
import static com.app.barbershopweb.order.reservation.constants.list.dto.OrderReservation_List_OrderDto__TestConstants.ORDER_RESERVATION_OPEN_ORDER_DTO_LIST;
import static com.app.barbershopweb.order.reservation.constants.list.entity.OrderReservation_List_OrderEntity__TestConstants.ORDER_RESERVATION_OPEN_ORDER_ENTITY_LIST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderReservationController.class)
@DisplayName("TESTING: " + ORDER_RESERVATION_URL + " (OrderReservationController.getAvailableOrders)")
@ExtendWith(MockitoExtension.class)
@MockBean(AuthenticationProvider.class)
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
    @WithMockUser
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
    @WithMockUser
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

        OrderController__TestUtils.checkOrderDtoJson(response.getContentAsString(), ORDER_RESERVATION_OPEN_ORDER_DTO_LIST, true);
    }
}
