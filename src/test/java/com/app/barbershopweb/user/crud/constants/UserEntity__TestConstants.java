package com.app.barbershopweb.user.crud.constants;

import com.app.barbershopweb.user.crud.User;

import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.*;

public final class UserEntity__TestConstants {

    public static final User USER_VALID_ENTITY = new User(
            USERS_VALID_USER_ID, "firstname1",
            "lastname1", "+38091",
            "1@gmail.com", USERS_ROLE, USERS_REGISTRATION_DATE
    );

    public static final User USER_ENTITY_NOT_EXISTED_ID = new User(
            USERS_NOT_EXISTING_USER_ID, USER_VALID_ENTITY.getFirstName(),
            USER_VALID_ENTITY.getLastName(), USER_VALID_ENTITY.getPhoneNumber(),
            USER_VALID_ENTITY.getEmail(), USER_VALID_ENTITY.getRole(), USERS_REGISTRATION_DATE
    );

    public static final User USER_VALID_UPDATED_USER_ENTITY = new User(
            USER_VALID_ENTITY.getId(), "Antonio",
            "Petru44i", USER_VALID_ENTITY.getPhoneNumber(),
            USER_VALID_ENTITY.getEmail(), USERS_ROLE, USERS_REGISTRATION_DATE
    );

}
