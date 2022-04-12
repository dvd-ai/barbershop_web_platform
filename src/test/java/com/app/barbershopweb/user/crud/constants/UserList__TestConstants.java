package com.app.barbershopweb.user.crud.constants;

import com.app.barbershopweb.user.crud.Users;
import com.app.barbershopweb.user.crud.UsersDto;

import java.util.List;

import static com.app.barbershopweb.user.crud.constants.UserEntity__TestConstants.USERS_VALID_ENTITY;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.*;

public class UserList__TestConstants {

    public static final List<Users> USERS_USER_VALID_ENTITY_LIST = List.of(
            new Users(
                    USERS_VALID_USER_ID, USERS_VALID_ENTITY.getFirstName(),
                    USERS_VALID_ENTITY.getLastName(), USERS_VALID_ENTITY.getPhoneNumber(),
                    USERS_VALID_ENTITY.getEmail(), USERS_VALID_ENTITY.getRole(), USERS_REGISTRATION_DATE
            ),
            new Users(
                    USERS_VALID_USER_ID + 1, USERS_VALID_ENTITY.getFirstName(),
                    USERS_VALID_ENTITY.getLastName(), USERS_VALID_ENTITY.getPhoneNumber(),
                    USERS_VALID_ENTITY.getEmail(), USERS_VALID_ENTITY.getRole(), USERS_REGISTRATION_DATE
            ),
            new Users(
                    USERS_VALID_USER_ID + 2, USERS_VALID_ENTITY.getFirstName(),
                    USERS_VALID_ENTITY.getLastName(), USERS_VALID_ENTITY.getPhoneNumber(),
                    USERS_VALID_ENTITY.getEmail(), USERS_VALID_ENTITY.getRole(), USERS_REGISTRATION_DATE
            )
    );

    public static final List<UsersDto> USERS_USER_VALID_DTO_LIST = List.of(
            new UsersDto(
                    USERS_VALID_USER_ID, USERS_USER_VALID_ENTITY_LIST.get(0).getFirstName(),
                    USERS_USER_VALID_ENTITY_LIST.get(0).getLastName(), USERS_USER_VALID_ENTITY_LIST.get(0).getPhoneNumber(),
                    USERS_USER_VALID_ENTITY_LIST.get(0).getEmail(), USERS_ROLE, USERS_REGISTRATION_DATE
            ),
            new UsersDto(
                    USERS_VALID_USER_ID + 1, USERS_USER_VALID_ENTITY_LIST.get(1).getFirstName(),
                    USERS_USER_VALID_ENTITY_LIST.get(1).getLastName(), USERS_USER_VALID_ENTITY_LIST.get(1).getPhoneNumber(),
                    USERS_USER_VALID_ENTITY_LIST.get(1).getEmail(), USERS_ROLE, USERS_REGISTRATION_DATE
            ),
            new UsersDto(
                    USERS_VALID_USER_ID + 2, USERS_USER_VALID_ENTITY_LIST.get(2).getFirstName(),
                    USERS_USER_VALID_ENTITY_LIST.get(2).getLastName(), USERS_USER_VALID_ENTITY_LIST.get(2).getPhoneNumber(),
                    USERS_USER_VALID_ENTITY_LIST.get(2).getEmail(), USERS_ROLE, USERS_REGISTRATION_DATE
            )
    );
}
