package com.app.barbershopweb.user.crud.constants;

import com.app.barbershopweb.user.crud.UsersDto;

import static com.app.barbershopweb.user.crud.constants.UserEntity__TestConstants.USER_VALID_ENTITY;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.*;

public final class UserDto__TestConstants {

    public static final UsersDto USERS_VALID_USER_DTO = new UsersDto(
            USERS_VALID_USER_ID, USER_VALID_ENTITY.getFirstName(),
            USER_VALID_ENTITY.getLastName(), USER_VALID_ENTITY.getPhoneNumber(),
            USER_VALID_ENTITY.getEmail(), USER_VALID_ENTITY.getRole(), USERS_REGISTRATION_DATE
    );

    public static final UsersDto USERS_INVALID_USER_DTO = new UsersDto(
            USERS_INVALID_USER_ID, USERS_VALID_USER_DTO.firstName(),
            "", "",
            USERS_VALID_USER_DTO.email(), USERS_VALID_USER_DTO.role(), USERS_REGISTRATION_DATE
    );

    public static final UsersDto USERS_USER_DTO_NOT_EXISTED_ID = new UsersDto(
            USERS_NOT_EXISTING_USER_ID, USERS_VALID_USER_DTO.firstName(),
            USERS_VALID_USER_DTO.lastName(), USERS_VALID_USER_DTO.phoneNumber(),
            USERS_VALID_USER_DTO.email(), USERS_VALID_USER_DTO.role(), USERS_REGISTRATION_DATE
    );

    public static final UsersDto USERS_VALID_UPDATED_USER_DTO = new UsersDto(
            USERS_VALID_USER_DTO.id(), "Antonio",
            "Petru44i", USERS_VALID_USER_DTO.phoneNumber(),
            USERS_VALID_USER_DTO.email(), USERS_ROLE, USERS_REGISTRATION_DATE
    );
}
