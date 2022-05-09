package com.app.barbershopweb.order.reservation.controller;

import com.app.barbershopweb.exception.DbUniqueConstraintsViolationException;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.order.crud.OrderConverter;
import com.app.barbershopweb.order.reservation.OrderReservationController;
import com.app.barbershopweb.order.reservation.OrderReservationService;
import com.app.barbershopweb.order.testutils.OrderController__TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.app.barbershopweb.order.reservation.constants.OrderReservation_Metadata__TestConstants.ORDER_RESERVATION_URL;
import static com.app.barbershopweb.order.reservation.constants.dto.OrderReservation_Dto__TestConstants.ORDER_RESERVATION_VALID_DTO;
import static com.app.barbershopweb.order.reservation.constants.error.OrderReservation_ErrorMessage_Fk__TestConstants.ORDER_RESERVATION_ERR_FK_CUSTOMER_ID;
import static com.app.barbershopweb.order.reservation.constants.error.OrderReservation_ErrorMessage_Fk__TestConstants.ORDER_RESERVATION_ERR_FK_ORDER_ID_LIST;
import static com.app.barbershopweb.order.reservation.constants.error.OrderReservation_ErrorMessage_Uk__TestConstants.ORDER_RESERVATION_ERR_UK_CUSTOMER_ID__ORDER_DATE;
import static com.app.barbershopweb.order.reservation.constants.list.dto.OrderReservation_List_OrderDto__TestConstants.ORDER_RESERVATION_CLOSED_ORDER_DTO_LIST;
import static com.app.barbershopweb.order.reservation.constants.list.entity.OrderReservation_List_OrderEntity__TestConstants.ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderReservationController.class)
@DisplayName("TESTING: " + ORDER_RESERVATION_URL + " (OrderReservationController.reserveOrders)")
@MockBean(AuthenticationProvider.class)
class OrderReservationController__reserveOrders {

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
                    When fk violation (customerId) in the dto, returns 
                    status code 404 and error dto
                    """
    )
    void whenCustomerDoesntExist() throws Exception {
        json = objectMapper.writeValueAsString(
                ORDER_RESERVATION_VALID_DTO
        );

        when(orderReservationService.
                reserveOrders(
                        ORDER_RESERVATION_VALID_DTO.orderIds(),
                        ORDER_RESERVATION_VALID_DTO.customerId()
                )
        )
                .thenThrow(new NotFoundException(
                                List.of(ORDER_RESERVATION_ERR_FK_CUSTOMER_ID)
                        )
                );

        mockMvc
                .perform(put(ORDER_RESERVATION_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(ORDER_RESERVATION_ERR_FK_CUSTOMER_ID)))
        ;
    }

    @Test
    @DisplayName(
            """
                    When fk violation (orderIds) in the dto, returns 
                    status code 404 and error dto"
                    """
    )
    void whenOrdersDontExist() throws Exception {
        json = objectMapper.writeValueAsString(
                ORDER_RESERVATION_VALID_DTO
        );

        when(orderReservationService.
                reserveOrders(
                        ORDER_RESERVATION_VALID_DTO.orderIds(),
                        ORDER_RESERVATION_VALID_DTO.customerId()
                )
        )
                .thenThrow(new NotFoundException(
                                ORDER_RESERVATION_ERR_FK_ORDER_ID_LIST
                        )
                );

        mockMvc
                .perform(put(ORDER_RESERVATION_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(jsonPath("$.errors", hasSize(ORDER_RESERVATION_VALID_DTO.orderIds().size())))
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors", is(ORDER_RESERVATION_ERR_FK_ORDER_ID_LIST)))
        ;

    }

    @Test
    @DisplayName(
            """
                    When uk violation (customerId, orderDate) in the dto, returns 
                    status code 400 and error dto"
                    """
    )
    void whenUkViolation() throws Exception {
        json = objectMapper.writeValueAsString(
                ORDER_RESERVATION_VALID_DTO
        );

        when(orderReservationService.
                reserveOrders(
                        ORDER_RESERVATION_VALID_DTO.orderIds(),
                        ORDER_RESERVATION_VALID_DTO.customerId()
                )
        )
                .thenThrow(
                        new DbUniqueConstraintsViolationException(
                                List.of(
                                        ORDER_RESERVATION_ERR_UK_CUSTOMER_ID__ORDER_DATE
                                )
                        )
                );


        mockMvc
                .perform(put(ORDER_RESERVATION_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(ORDER_RESERVATION_ERR_UK_CUSTOMER_ID__ORDER_DATE)))
        ;
    }

    @Test
    @DisplayName(
            """
                    returns reserved orders with customerId, returns 
                    status code 200
                    """
    )
    void reserveSuccessfully() throws Exception {
        json = objectMapper.writeValueAsString(
                ORDER_RESERVATION_VALID_DTO
        );

        when(orderReservationService.
                reserveOrders(
                        ORDER_RESERVATION_VALID_DTO.orderIds(),
                        ORDER_RESERVATION_VALID_DTO.customerId()
                )
        ).thenReturn(
                ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST
        );
        when(orderConverter.orderEntityListToDtoList(ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST))
                .thenReturn(
                        ORDER_RESERVATION_CLOSED_ORDER_DTO_LIST
                );

        MockHttpServletResponse response = mockMvc.perform(put(ORDER_RESERVATION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        OrderController__TestUtils.checkOrderEntityJson(
                response.getContentAsString(),
                ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST,
                false
        );
    }
}
