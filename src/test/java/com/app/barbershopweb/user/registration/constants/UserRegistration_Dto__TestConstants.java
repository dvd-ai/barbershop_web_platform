package com.app.barbershopweb.user.registration.constants;

import com.app.barbershopweb.user.registration.UserRegistrationDto;

import java.time.LocalDateTime;

import static com.app.barbershopweb.user.credentials.UserCredentials_Entities__TestConstants.USER_CREDENTIALS_VALID_ENTITY;

public final class UserRegistration_Dto__TestConstants {

    public static final UserRegistrationDto USER_REGISTRATION_VALID_DTO = new UserRegistrationDto(
        "firstname", "lastname",
            "+39030423", "1@gmail.com",
            USER_CREDENTIALS_VALID_ENTITY.getUsername(),
            USER_CREDENTIALS_VALID_ENTITY.getPassword(),
            LocalDateTime.of(2022, 11, 10, 13, 0)
    );

    public static final UserRegistrationDto USER_REGISTRATION_INVALID_DTO = new UserRegistrationDto(
            "", "",
            "", "",
            "",
            "",
            null
    );

}
