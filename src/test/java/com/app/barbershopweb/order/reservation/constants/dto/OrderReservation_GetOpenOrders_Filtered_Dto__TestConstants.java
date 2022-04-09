package com.app.barbershopweb.order.reservation.constants.dto;

import com.app.barbershopweb.order.reservation.dto.GetOpenFilteredOrders__RequestDto;
import com.app.barbershopweb.order.reservation.entity.OrderFilters;

import java.util.List;

import static com.app.barbershopweb.order.reservation.constants.OrderReservation_Metadata__TestConstants.GET_OPEN_ORDERS_BARBER_FILTER;
import static com.app.barbershopweb.order.reservation.constants.OrderReservation_Metadata__TestConstants.GET_OPEN_ORDERS_START_WEEK_DATE;

public final class OrderReservation_GetOpenOrders_Filtered_Dto__TestConstants {

    public static final GetOpenFilteredOrders__RequestDto GET_OPEN_FILTERED_ORDERS_NO_FILTERS__REQUEST_DTO =
            new GetOpenFilteredOrders__RequestDto(
                    1L, GET_OPEN_ORDERS_START_WEEK_DATE,
                    new OrderFilters(List.of())
            );

    public static final GetOpenFilteredOrders__RequestDto GET_OPEN_FILTERED_ORDERS__REQUEST_DTO =
            new GetOpenFilteredOrders__RequestDto(
                    1L, GET_OPEN_ORDERS_START_WEEK_DATE,
                    new OrderFilters(
                            List.of(GET_OPEN_ORDERS_BARBER_FILTER)
                    )
            );


    public static final GetOpenFilteredOrders__RequestDto GET_OPEN_FILTERED_ORDERS__REQUEST_INVALID_DTO =
            new GetOpenFilteredOrders__RequestDto(
                    0L, null, null
            );

}
