package com.app.barbershopweb.user.crud.constants;

import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_VALID_USER_ID;

public final class UserErrorMessage__TestConstants {

    public static final String USER_ERR_INVALID_DTO_ID = "'usersDto.id' must be greater than or equal to 1";
    public static final String USER_ERR_INVALID_DTO_PHONE_NUMBER = "'usersDto.phoneNumber' must not be blank";
    public static final String USER_ERR_INVALID_DTO_LAST_NAME = "'usersDto.lastName' must not be blank";

    public static final String USER_ERR_INVALID_PATH_VAR_USER_ID = "'userId' must be greater than or equal to 1";

    public static final String USER_ERR_FILE_UPLOAD_USER_ID = "Profile avatar upload: User with id "
            + USERS_VALID_USER_ID + " wasn't found";

    public static final String USER_ERR_FILE_DOWNLOAD_USER_ID = "Profile avatar download: User with id "
            + USERS_VALID_USER_ID + " wasn't found";

}
