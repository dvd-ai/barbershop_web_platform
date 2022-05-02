package com.app.barbershopweb.user.credentials.error;

import static com.app.barbershopweb.user.credentials.UserCredentials_Entities__TestConstants.USER_CREDENTIALS_VALID_ENTITY;

public final class UserCredentialsErrorMessage_Fk__TestConstants {
    public static final String USER_CREDENTIALS_ERR_FK_USER_ID =
            "fk violation: user with id " +
                    USER_CREDENTIALS_VALID_ENTITY.getUserId() + " not present";
}
