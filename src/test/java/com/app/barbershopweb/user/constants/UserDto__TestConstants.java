package com.app.barbershopweb.user.constants;

import com.app.barbershopweb.user.UsersDto;

import static com.app.barbershopweb.user.constants.UserEntity__TestConstants.USERS_VALID_ENTITY;
import static com.app.barbershopweb.user.constants.UserMetadata__TestConstants.*;

public final class UserDto__TestConstants {

    public static final UsersDto USERS_VALID_USER_DTO = new UsersDto(
            USERS_VALID_USER_ID, USERS_VALID_ENTITY.getFirstName(),
            USERS_VALID_ENTITY.getLastName(), USERS_VALID_ENTITY.getPhoneNumber(),
            USERS_VALID_ENTITY.getEmail(), USERS_VALID_ENTITY.getRole(), USERS_REGISTRATION_DATE
    );

    public static final UsersDto USERS_INVALID_USER_DTO = new UsersDto(
            USERS_INVALID_USER_ID, USERS_VALID_USER_DTO.firstName(),
            "", "",
            USERS_VALID_USER_DTO.email(), USERS_VALID_USER_DTO.role(), USERS_REGISTRATION_DATE
    );

    public static final UsersDto USERS_USER_DTO_NOT_EXISTED_ID = new UsersDto(
            USERS_NOT_EXISTED_USER_ID, USERS_VALID_USER_DTO.firstName(),
            USERS_VALID_USER_DTO.lastName(), USERS_VALID_USER_DTO.phoneNumber(),
            USERS_VALID_USER_DTO.email(), USERS_VALID_USER_DTO.role(), USERS_REGISTRATION_DATE
    );

    public static final UsersDto USERS_VALID_UPDATED_USER_DTO = new UsersDto(
            USERS_VALID_USER_DTO.id(), "Antonio",
            "Petru44i", USERS_VALID_USER_DTO.phoneNumber(),
            USERS_VALID_USER_DTO.email(), USERS_ROLE, USERS_REGISTRATION_DATE
    );
}
