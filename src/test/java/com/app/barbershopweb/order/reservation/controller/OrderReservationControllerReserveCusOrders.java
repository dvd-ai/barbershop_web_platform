package com.app.barbershopweb.order.reservation.controller;

import com.app.barbershopweb.exception.DbUniqueConstraintsViolationException;
import com.app.barbershopweb.exception.NotFoundException;
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
import java.util.List;

import static com.app.barbershopweb.order.reservation.OrderReservationTestConstants.ORDER_RESERV_URL;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderReservationController.class)
@DisplayName("TESTING: " + ORDER_RESERV_URL + " (reserveCustomerOrders method)")
class OrderReservationControllerReserveCusOrders {

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
            When invalid dto, returns 
            status code 400 and error dto"
            """
    )
    void whenInvalidDto() throws Exception {
        json = objectMapper.writeValueAsString(
                ortc.INVALID_ORDER_RESERV_DTO
        );

        mockMvc
                .perform(put(ORDER_RESERV_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(ortc.DTO_CV_CUSTOMER_ID_ERR_MSG)))
                .andExpect(jsonPath("$.errors", hasItem(ortc.DTO_CV_ORDER_ID_LIST_ERR_MSG)))
        ;
    }

    @Test
    @DisplayName(
            """
            When fk violation (customerId) in the dto, returns 
            status code 404 and error dto
            """
    )
    void whenCustomerDoesntExist() throws Exception {
        json = objectMapper.writeValueAsString(
                ortc.VALID_ORDER_RESERV_DTO
        );

        when(orderReservationService.reserveCustomerOrders(any(), any()))
                .thenThrow(new NotFoundException(
                        List.of(ortc.FK_CV_CUSTOMER_ID_ERR_MSG)
                )
        );

        mockMvc
                .perform(put(ORDER_RESERV_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(ortc.FK_CV_CUSTOMER_ID_ERR_MSG)))
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
                ortc.VALID_ORDER_RESERV_DTO
        );

        when(orderReservationService.reserveCustomerOrders(any(), any()))
                .thenThrow(new NotFoundException(
                                ortc.FK_CV_ORDER_ID_LIST_ERR_MSG
                        )
                );

        mockMvc
                .perform(put(ORDER_RESERV_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(jsonPath("$.errors", hasSize(ortc.VALID_ORDER_RESERV_DTO.orderIds().size())))
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors", is(ortc.FK_CV_ORDER_ID_LIST_ERR_MSG)))
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
                ortc.VALID_ORDER_RESERV_DTO
        );

        when(orderReservationService.reserveCustomerOrders(any(), any()))
                .thenThrow(
                        new DbUniqueConstraintsViolationException(
                                List.of(
                                        ortc.UK_CV_CUSTOMER_ID_ORDER_DATE_ERR_MSG
                                )
                        )
                );


        mockMvc
                .perform(put(ORDER_RESERV_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(ortc.UK_CV_CUSTOMER_ID_ORDER_DATE_ERR_MSG)))
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
                ortc.VALID_ORDER_RESERV_DTO
        );

        when(orderReservationService.reserveCustomerOrders(any(), any())).thenReturn(
                ortc.RESERVED_ORDER_ENTITY_LIST
        );
        when(orderConverter.orderEntityListToDtoList(any())).thenReturn(
                ortc.RESERVED_ORDER_DTO_LIST
        );

        mockMvc.perform(put(ORDER_RESERV_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(ortc.RESERVED_ORDER_ENTITY_LIST.size())))
                .andExpect(jsonPath("$[0].orderId", is(ortc.RESERVED_ORDER_DTO_LIST.get(0).orderId().intValue())))
                .andExpect(jsonPath("$[0].barbershopId", is(ortc.RESERVED_ORDER_DTO_LIST.get(0).barbershopId().intValue())))
                .andExpect(jsonPath("$[0].barberId", is(ortc.RESERVED_ORDER_DTO_LIST.get(0).barberId().intValue())))
                .andExpect(jsonPath("$[0].customerId", is(ortc.RESERVED_ORDER_DTO_LIST.get(0).customerId().intValue())))
                .andExpect(jsonPath("$[0].orderDate", is(ortc.RESERVED_ORDER_DTO_LIST.get(0).orderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[0].active", is(ortc.RESERVED_ORDER_DTO_LIST.get(0).active())))

                .andExpect(jsonPath("$[1].orderId", is(ortc.RESERVED_ORDER_DTO_LIST.get(1).orderId().intValue())))
                .andExpect(jsonPath("$[1].barbershopId", is(ortc.RESERVED_ORDER_DTO_LIST.get(1).barbershopId().intValue())))
                .andExpect(jsonPath("$[1].barberId", is(ortc.RESERVED_ORDER_DTO_LIST.get(1).barberId().intValue())))
                .andExpect(jsonPath("$[1].customerId", is(ortc.RESERVED_ORDER_DTO_LIST.get(1).customerId().intValue())))
                .andExpect(jsonPath("$[1].orderDate", is(ortc.RESERVED_ORDER_DTO_LIST.get(1).orderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[1].active", is(ortc.RESERVED_ORDER_DTO_LIST.get(1).active())))

                .andExpect(jsonPath("$[2].orderId", is(ortc.RESERVED_ORDER_DTO_LIST.get(2).orderId().intValue())))
                .andExpect(jsonPath("$[2].barbershopId", is(ortc.RESERVED_ORDER_DTO_LIST.get(2).barbershopId().intValue())))
                .andExpect(jsonPath("$[2].barberId", is(ortc.RESERVED_ORDER_DTO_LIST.get(2).barberId().intValue())))
                .andExpect(jsonPath("$[2].customerId", is(ortc.RESERVED_ORDER_DTO_LIST.get(2).customerId().intValue())))
                .andExpect(jsonPath("$[2].orderDate", is(ortc.RESERVED_ORDER_DTO_LIST.get(2).orderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[2].active", is(ortc.RESERVED_ORDER_DTO_LIST.get(2).active())))
                ;

    }



}
