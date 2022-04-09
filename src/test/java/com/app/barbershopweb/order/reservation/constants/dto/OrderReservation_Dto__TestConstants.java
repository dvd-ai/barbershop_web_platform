package com.app.barbershopweb.order.reservation.constants.dto;

import com.app.barbershopweb.order.reservation.dto.OrderReservationDto;

import java.util.List;

public final class OrderReservation_Dto__TestConstants {
    public static final OrderReservationDto ORDER_RESERVATION_INVALID_DTO = new OrderReservationDto(
            List.of(), 0L
    );

    public static final OrderReservationDto ORDER_RESERVATION_VALID_DTO = new OrderReservationDto(
            List.of(1L, 2L, 3L), 1L
    );
}
