package com.app.barbershopweb.order.reservation.constants.dto;

import com.app.barbershopweb.order.reservation.dto.GetOpenOrders__RequestDto;

import static com.app.barbershopweb.order.reservation.constants.OrderReservation_Metadata__TestConstants.GET_OPEN_ORDERS_START_WEEK_DATE;

public final class OrderReservation_GetOpenOrders_Dto__TestConstants {

    public static final GetOpenOrders__RequestDto GET_OPEN_ORDERS__REQUEST_VALID_DTO = new GetOpenOrders__RequestDto(
            1L, GET_OPEN_ORDERS_START_WEEK_DATE
    );

    public static final GetOpenOrders__RequestDto GET_OPEN_ORDERS__REQUEST_INVALID_DTO = new GetOpenOrders__RequestDto(
            0L, null
    );
}
