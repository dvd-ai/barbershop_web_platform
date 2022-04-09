package com.app.barbershopweb.order.reservation.constants;

import java.time.LocalDateTime;

public final class OrderReservation_Metadata__TestConstants {
    public static final String ORDER_RESERVATION_URL = "/orders/reservations";
    public static final String ORDER_RESERVATION_FILTER_URL = ORDER_RESERVATION_URL + "/filtered";

    public static final long GET_OPEN_ORDERS_BARBER_FILTER = 3L;
    public static final LocalDateTime GET_OPEN_ORDERS_START_WEEK_DATE = LocalDateTime.of(2022, 3,
            28, 12, 30
    );
}
