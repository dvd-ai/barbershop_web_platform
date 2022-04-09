package com.app.barbershopweb.order.reservation.controller;

import com.app.barbershopweb.exception.DbUniqueConstraintsViolationException;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.order.crud.OrderConverter;
import com.app.barbershopweb.order.reservation.OrderReservationController;
import com.app.barbershopweb.order.reservation.OrderReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.app.barbershopweb.order.reservation.constants.OrderReservation_Metadata__TestConstants.ORDER_RESERVATION_URL;
import static com.app.barbershopweb.order.reservation.constants.dto.OrderReservation_Dto__TestConstants.ORDER_RESERVATION_VALID_DTO;
import static com.app.barbershopweb.order.reservation.constants.error.OrderReservation_ErrorMessage_Fk__TestConstants.ORDER_RESERVATION_ERR_FK_CUSTOMER_ID;
import static com.app.barbershopweb.order.reservation.constants.error.OrderReservation_ErrorMessage_Fk__TestConstants.ORDER_RESERVATION_ERR_FK_ORDER_ID_LIST;
import static com.app.barbershopweb.order.reservation.constants.error.OrderReservation_ErrorMessage_Uk__TestConstants.ORDER_RESERVATION_ERR_UK_CUSTOMER_ID__ORDER_DATE;
import static com.app.barbershopweb.order.reservation.constants.list.dto.OrderReservation_List_OrderDto__TestConstants.ORDER_RESERVATION_CLOSED_ORDER_DTO_LIST;
import static com.app.barbershopweb.order.reservation.constants.list.entity.OrderReservation_List_OrderEntity__TestConstants.ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderReservationController.class)
@DisplayName("TESTING: " + ORDER_RESERVATION_URL + " (OrderReservationController.reserveOrders)")
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

        mockMvc.perform(put(ORDER_RESERVATION_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST.size())))
                .andExpect(jsonPath("$[0].orderId", is(ORDER_RESERVATION_CLOSED_ORDER_DTO_LIST.get(0).orderId().intValue())))
                .andExpect(jsonPath("$[0].barbershopId", is(ORDER_RESERVATION_CLOSED_ORDER_DTO_LIST.get(0).barbershopId().intValue())))
                .andExpect(jsonPath("$[0].barberId", is(ORDER_RESERVATION_CLOSED_ORDER_DTO_LIST.get(0).barberId().intValue())))
                .andExpect(jsonPath("$[0].customerId", is(ORDER_RESERVATION_CLOSED_ORDER_DTO_LIST.get(0).customerId().intValue())))
                .andExpect(jsonPath("$[0].orderDate", is(ORDER_RESERVATION_CLOSED_ORDER_DTO_LIST.get(0).orderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[0].active", is(ORDER_RESERVATION_CLOSED_ORDER_DTO_LIST.get(0).active())))

                .andExpect(jsonPath("$[1].orderId", is(ORDER_RESERVATION_CLOSED_ORDER_DTO_LIST.get(1).orderId().intValue())))
                .andExpect(jsonPath("$[1].barbershopId", is(ORDER_RESERVATION_CLOSED_ORDER_DTO_LIST.get(1).barbershopId().intValue())))
                .andExpect(jsonPath("$[1].barberId", is(ORDER_RESERVATION_CLOSED_ORDER_DTO_LIST.get(1).barberId().intValue())))
                .andExpect(jsonPath("$[1].customerId", is(ORDER_RESERVATION_CLOSED_ORDER_DTO_LIST.get(1).customerId().intValue())))
                .andExpect(jsonPath("$[1].orderDate", is(ORDER_RESERVATION_CLOSED_ORDER_DTO_LIST.get(1).orderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[1].active", is(ORDER_RESERVATION_CLOSED_ORDER_DTO_LIST.get(1).active())))

                .andExpect(jsonPath("$[2].orderId", is(ORDER_RESERVATION_CLOSED_ORDER_DTO_LIST.get(2).orderId().intValue())))
                .andExpect(jsonPath("$[2].barbershopId", is(ORDER_RESERVATION_CLOSED_ORDER_DTO_LIST.get(2).barbershopId().intValue())))
                .andExpect(jsonPath("$[2].barberId", is(ORDER_RESERVATION_CLOSED_ORDER_DTO_LIST.get(2).barberId().intValue())))
                .andExpect(jsonPath("$[2].customerId", is(ORDER_RESERVATION_CLOSED_ORDER_DTO_LIST.get(2).customerId().intValue())))
                .andExpect(jsonPath("$[2].orderDate", is(ORDER_RESERVATION_CLOSED_ORDER_DTO_LIST.get(2).orderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[2].active", is(ORDER_RESERVATION_CLOSED_ORDER_DTO_LIST.get(2).active())))
        ;

    }


}
