package com.app.barbershopweb.order.reservation.controller;

import com.app.barbershopweb.exception.DbUniqueConstraintsViolationException;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.order.crud.OrderConverter;
import com.app.barbershopweb.order.reservation.OrderReservationController;
import com.app.barbershopweb.order.reservation.OrderReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static com.app.barbershopweb.order.crud.constants.OrderMetadata__TestConstants.ORDER_FIELD_AMOUNT;
import static com.app.barbershopweb.order.reservation.constants.OrderReservation_Metadata__TestConstants.ORDER_RESERVATION_URL;
import static com.app.barbershopweb.order.reservation.constants.dto.OrderReservation_Dto__TestConstants.ORDER_RESERVATION_VALID_DTO;
import static com.app.barbershopweb.order.reservation.constants.error.OrderReservation_ErrorMessage_Fk__TestConstants.ORDER_RESERVATION_ERR_FK_CUSTOMER_ID;
import static com.app.barbershopweb.order.reservation.constants.error.OrderReservation_ErrorMessage_Fk__TestConstants.ORDER_RESERVATION_ERR_FK_ORDER_ID_LIST;
import static com.app.barbershopweb.order.reservation.constants.error.OrderReservation_ErrorMessage_Uk__TestConstants.ORDER_RESERVATION_ERR_UK_CUSTOMER_ID__ORDER_DATE;
import static com.app.barbershopweb.order.reservation.constants.list.dto.OrderReservation_List_OrderDto__TestConstants.ORDER_RESERVATION_CLOSED_ORDER_DTO_LIST;
import static com.app.barbershopweb.order.reservation.constants.list.dto.OrderReservation_List_OrderDto__TestConstants.ORDER_RESERVATION_OPEN_FILTERED_ORDER_DTO_LIST;
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

        DocumentContext context = JsonPath.parse(response.getContentAsString());
        List<Object> object = context.read("$");

        assertEquals(ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST.size(), object.size());

        for (var i = 0; i < ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST.size(); i++) {
            Map<String, Object> dto = context.read("$[" + i + "]");
            assertEquals(ORDER_FIELD_AMOUNT, dto.size());
            assertEquals(ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST.get(i).getOrderId().intValue(), (Integer) context.read("$["+ i + "].orderId"));
            assertEquals(ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST.get(i).getBarbershopId().intValue(), (Integer) context.read("$["+ i + "].barbershopId"));
            assertEquals(ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST.get(i).getBarberId().intValue(), (Integer) context.read("$["+ i + "].barberId"));
            assertEquals(ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST.get(i).getCustomerId().intValue(), (Integer) context.read("$["+ i + "].customerId"));
            assertEquals(ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST.get(i).getOrderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), context.read("$["+ i + "].orderDate"));
            assertEquals(ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST.get(i).getActive(), context.read("$[" + i + "].active"));
        }
    }
}
