package com.app.barbershopweb.user.credentials;

import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_VALID_USER_ID;

public final class UserCredentials_Entities__TestConstants {

    public final static UserCredentials USER_CREDENTIALS_VALID_ENTITY = new UserCredentials(
            USERS_VALID_USER_ID, "username", "password", true
    );
}
