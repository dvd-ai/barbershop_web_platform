package com.app.barbershopweb.user.crud.constants;

import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_NOT_EXISTING_USER_ID;

public final class UserErrorMessage__TestConstants {

    public static final String USER_ERR_INVALID_DTO_ID = "'usersDto.id' must be greater than or equal to 1";
    public static final String USER_ERR_INVALID_DTO_PHONE_NUMBER = "'usersDto.phoneNumber' must not be blank";
    public static final String USER_ERR_INVALID_DTO_LAST_NAME = "'usersDto.lastName' must not be blank";

    public static final String USER_ERR_INVALID_PATH_VAR_USER_ID = "'userId' must be greater than or equal to 1";
    public static final String USER_ERR_NOT_EXISTING_USER_ID = "User with id " + USERS_NOT_EXISTING_USER_ID + " not found";
}
