package com.app.barbershopweb.order.reservation.dto;

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

import static com.app.barbershopweb.order.reservation.constants.OrderReservation_Metadata__TestConstants.ORDER_RESERVATION_FILTER_URL;
import static com.app.barbershopweb.order.reservation.constants.OrderReservation_Metadata__TestConstants.ORDER_RESERVATION_URL;
import static com.app.barbershopweb.order.reservation.constants.dto.OrderReservation_GetOpenOrders_Filtered_Dto__TestConstants.GET_OPEN_FILTERED_ORDERS__REQUEST_INVALID_DTO;
import static com.app.barbershopweb.order.reservation.constants.error.OrderReservation_ErrorMessage_Dto__TestConstants.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderReservationController.class)
class GetOpenFilteredOrders__RequestDtoTest {

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
            "TESTING: " + ORDER_RESERVATION_URL + " (OrderReservationController.getFilteredAvailableOrders) " +
                    """
                            When invalid dto, returns 
                            status code 400 and error dto"
                            """
    )
    void getFilteredAvailableOrders__whenInvalidDto() throws Exception {
        json = objectMapper.writeValueAsString(
                GET_OPEN_FILTERED_ORDERS__REQUEST_INVALID_DTO
        );

        mockMvc
                .perform(post(ORDER_RESERVATION_FILTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(GET_OPEN_FILTERED_ORDERS_ERR_INVALID_DTO_BARBERSHOP_ID)))
                .andExpect(jsonPath("$.errors", hasItem(GET_OPEN_FILTERED_ORDERS_ERR_INVALID_DTO_START_WEEK_DATE)))
                .andExpect(jsonPath("$.errors", hasItem(GET_OPEN_FILTERED_ORDERS_ERR_INVALID_DTO_ORDER_FILTERS)))
        ;
    }

}