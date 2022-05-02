package com.app.barbershopweb.user.credentials.error;

import static com.app.barbershopweb.user.credentials.UserCredentials_Entities__TestConstants.USER_CREDENTIALS_VALID_ENTITY;

public class UserCredentialsErrorMessage_Uk__TestConstants {

    public static final String USER_CREDENTIALS_ERR_UK_USERNAME =
            "uk violation: user credentials with username " + USER_CREDENTIALS_VALID_ENTITY.getUsername()
                    + " already exist";

    public static final String USER_CREDENTIALS_ERR_UK_USER_ID =
            "uk violation: user credentials with userId " + USER_CREDENTIALS_VALID_ENTITY.getUserId()
                    + " already exist";
}
