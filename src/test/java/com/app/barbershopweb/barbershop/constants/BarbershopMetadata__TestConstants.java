package com.app.barbershopweb.barbershop.constants;

import java.time.LocalTime;

public class BarbershopMetadata__TestConstants {

    public static final int BARBERSHOP_FIELD_AMOUNT = 7;
    public static final String BARBERSHOPS_URL = "/barbershops";

    public static final long BARBERSHOP_INVALID_BARBERSHOP_ID = -100L;
    public static final long BARBERSHOP_VALID_BARBERSHOP_ID = 1L;
    public static final long BARBERSHOP_NOT_EXISTED_BARBERSHOP_ID = 100_000L;

    public static final LocalTime BARBERSHOP_TIME_FROM = LocalTime.of(8, 0, 0);
    public static final LocalTime BARBERSHOP_TIME_TO = LocalTime.of(20, 0, 0);
}
