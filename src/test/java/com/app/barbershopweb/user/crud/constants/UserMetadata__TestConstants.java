package com.app.barbershopweb.user.crud.constants;

import java.time.LocalDateTime;

public final class UserMetadata__TestConstants {
    public static final int USERS_FIELD_AMOUNT = 7;
    public static final String USERS_URL = "/users";

    public static final long USERS_INVALID_USER_ID = -100L;
    public static final long USERS_VALID_USER_ID = 1L;
    public static final long USERS_NOT_EXISTED_USER_ID = 100_000L;
    public static final String USERS_ROLE = "admin";
    public static final LocalDateTime USERS_REGISTRATION_DATE =
            LocalDateTime.of(
                    2022, 12,
                    15, 14,
                    30
            );

}
