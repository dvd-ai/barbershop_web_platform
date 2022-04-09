package com.app.barbershopweb.order.crud.constants;

import java.time.LocalDateTime;

import static com.app.barbershopweb.barbershop.constants.BarbershopEntity__TestConstants.BARBERSHOP_VALID_ENTITY;

public final class OrderMetadata__TestConstants {

    public static final int ORDER_FIELD_AMOUNT = 6;
    public static final String ORDERS_URL = "/orders";

    public static final long ORDER_INVALID_ORDER_ID = -100L;
    public static final long ORDER_VALID_ORDER_ID = 1L;
    public static final long ORDER_NOT_EXISTING_ORDER_ID = 100_000L;
    public static final long ORDER_VALID_CUSTOMER_ID = 1L;//
    public static final long ORDER_VALID_BARBER_ID = 2L;//
    public static final LocalDateTime ORDER_VALID_ORDER_DATE = LocalDateTime.of(
            2022, 3, 30,
            BARBERSHOP_VALID_ENTITY.getWorkTimeFrom()
                    .getHour(),
            0
    );
    //hours shouldn't match to barbershop order hours,
    // minutes shouldn't be like '00';
    public static final LocalDateTime ORDER_INVALID_ORDER_DATE =
            ORDER_VALID_ORDER_DATE
                    .minusHours(5L)
                    .minusMinutes(30L);

    public static final boolean ORDER_ACTIVE = true;
}
