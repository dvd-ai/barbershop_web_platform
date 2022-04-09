package com.app.barbershopweb.user.constants;

import com.app.barbershopweb.user.Users;

import static com.app.barbershopweb.user.constants.UserMetadata__TestConstants.*;

public final class UserEntity__TestConstants {

    public static final Users USERS_VALID_ENTITY = new Users(
            USERS_VALID_USER_ID, "firstname1",
            "lastname1", "+38091",
            "1@gmail.com", USERS_ROLE, USERS_REGISTRATION_DATE
    );

    public static final Users USERS_ENTITY_NOT_EXISTED_ID = new Users(
            USERS_NOT_EXISTED_USER_ID, USERS_VALID_ENTITY.getFirstName(),
            USERS_VALID_ENTITY.getLastName(), USERS_VALID_ENTITY.getPhoneNumber(),
            USERS_VALID_ENTITY.getEmail(), USERS_VALID_ENTITY.getRole(), USERS_REGISTRATION_DATE
    );

    public static final Users USERS_VALID_UPDATED_USER_ENTITY = new Users(
            USERS_VALID_ENTITY.getId(), "Antonio",
            "Petru44i", USERS_VALID_ENTITY.getPhoneNumber(),
            USERS_VALID_ENTITY.getEmail(), USERS_ROLE, USERS_REGISTRATION_DATE
    );

}
