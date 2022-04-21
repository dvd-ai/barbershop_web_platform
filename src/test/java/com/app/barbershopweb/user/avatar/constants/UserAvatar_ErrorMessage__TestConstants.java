package com.app.barbershopweb.user.avatar.constants;

import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_VALID_USER_ID;

public final class UserAvatar_ErrorMessage__TestConstants {

    public final static String USER_AVATAR_ERR_FILE_SIZE = "'image' File size should be at most 1MB.";
    public final static String USER_AVATAR_ERR_INVALID_FILE = "'image' Only JPG and PNG images are allowed.";
    public final static String USER_AVATAR_ERR_EMPTY_FILE = "'image' It must not be an empty image.";

    public final static String USER_AVATAR_ERR_NO_AVATAR_FOUND = "No profile avatar for user with id " + USERS_VALID_USER_ID;
}
