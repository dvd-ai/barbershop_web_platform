package com.app.barbershopweb.user.avatar.constants;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_VALID_USER_ID;

public final class UserAvatar__TestConstants {

    public static final String USER_AVATAR_S3_KEY_PREFIX = "profile_avatar_";
    public static final String USER_AVATAR_S3_KEY = USER_AVATAR_S3_KEY_PREFIX + USERS_VALID_USER_ID;

    public static final MultipartFile USERS_AVATAR_IMAGE_MOCK = new MockMultipartFile(
            "avatar.png",
            "avatar.png",
            "image/png",
            "image file content".getBytes()
    );

}
