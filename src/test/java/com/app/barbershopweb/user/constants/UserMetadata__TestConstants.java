package com.app.barbershopweb.user.constants;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public final class UserMetadata__TestConstants {
    public static final int USERS_FIELD_AMOUNT = 7;
    public static final String USERS_URL = "/users";

    public static final long USERS_INVALID_USER_ID = -100L;
    public static final long USERS_VALID_USER_ID = 1L;
    public static final long USERS_NOT_EXISTED_USER_ID = 100_000L;
    public static final String USERS_ROLE = "admin";
    public static final String USERS_S3_KEY_PREFIX = "profile_avatar_";
    public static final String USERS_S3_KEY = USERS_S3_KEY_PREFIX + USERS_VALID_USER_ID;

    public static final LocalDateTime USERS_REGISTRATION_DATE =
            LocalDateTime.of(
                    2022, 12,
                    15, 14,
                    30
            );

    public static final MultipartFile USERS_AVATAR_IMAGE_MOCK = new MockMultipartFile("avatar.png",
            "avatar.png",
            "image/png",
            "image file content".getBytes());

}
