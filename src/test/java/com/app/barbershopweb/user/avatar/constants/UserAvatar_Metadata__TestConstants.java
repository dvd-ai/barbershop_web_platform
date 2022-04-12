package com.app.barbershopweb.user.avatar.constants;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_URL;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_VALID_USER_ID;

public final class UserAvatar_Metadata__TestConstants {

    public static final String USER_AVATARS_URL = USERS_URL + "/avatars";
    public static final String USER_AVATAR_S3_KEY_PREFIX = "profile_avatar_";
    public static final String USER_AVATAR_S3_KEY = USER_AVATAR_S3_KEY_PREFIX + USERS_VALID_USER_ID;

    public static final MockMultipartFile USERS_AVATAR_IMAGE_MOCK =
            new MockMultipartFile(
            "file",
            "avatar.png",
            "image/png",
            "image file content".getBytes()
    );

    public static final MockMultipartFile USERS_AVATAR_TEXT_FILE_MOCK
            = new MockMultipartFile(
            "file",
            "hello.txt",
            MediaType.TEXT_PLAIN_VALUE,
            "Hello, World!".getBytes()
    );

}
