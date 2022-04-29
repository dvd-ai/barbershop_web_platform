package com.app.barbershopweb.user.crud.constants;

import com.app.barbershopweb.user.crud.User;
import com.app.barbershopweb.user.crud.UsersDto;

import java.util.List;

import static com.app.barbershopweb.user.crud.constants.UserEntity__TestConstants.USER_VALID_ENTITY;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.*;

public class UserList__TestConstants {

    public static final List<User> USER_USER_VALID_ENTITY_LIST = List.of(
            new User(
                    USERS_VALID_USER_ID, USER_VALID_ENTITY.getFirstName(),
                    USER_VALID_ENTITY.getLastName(), USER_VALID_ENTITY.getPhoneNumber(),
                    USER_VALID_ENTITY.getEmail(), USER_VALID_ENTITY.getRole(), USERS_REGISTRATION_DATE
            ),
            new User(
                    USERS_VALID_USER_ID + 1, USER_VALID_ENTITY.getFirstName(),
                    USER_VALID_ENTITY.getLastName(), USER_VALID_ENTITY.getPhoneNumber(),
                    USER_VALID_ENTITY.getEmail(), USER_VALID_ENTITY.getRole(), USERS_REGISTRATION_DATE
            ),
            new User(
                    USERS_VALID_USER_ID + 2, USER_VALID_ENTITY.getFirstName(),
                    USER_VALID_ENTITY.getLastName(), USER_VALID_ENTITY.getPhoneNumber(),
                    USER_VALID_ENTITY.getEmail(), USER_VALID_ENTITY.getRole(), USERS_REGISTRATION_DATE
            )
    );

    public static final List<UsersDto> USERS_USER_VALID_DTO_LIST = List.of(
            new UsersDto(
                    USERS_VALID_USER_ID, USER_USER_VALID_ENTITY_LIST.get(0).getFirstName(),
                    USER_USER_VALID_ENTITY_LIST.get(0).getLastName(), USER_USER_VALID_ENTITY_LIST.get(0).getPhoneNumber(),
                    USER_USER_VALID_ENTITY_LIST.get(0).getEmail(), USERS_ROLE, USERS_REGISTRATION_DATE
            ),
            new UsersDto(
                    USERS_VALID_USER_ID + 1, USER_USER_VALID_ENTITY_LIST.get(1).getFirstName(),
                    USER_USER_VALID_ENTITY_LIST.get(1).getLastName(), USER_USER_VALID_ENTITY_LIST.get(1).getPhoneNumber(),
                    USER_USER_VALID_ENTITY_LIST.get(1).getEmail(), USERS_ROLE, USERS_REGISTRATION_DATE
            ),
            new UsersDto(
                    USERS_VALID_USER_ID + 2, USER_USER_VALID_ENTITY_LIST.get(2).getFirstName(),
                    USER_USER_VALID_ENTITY_LIST.get(2).getLastName(), USER_USER_VALID_ENTITY_LIST.get(2).getPhoneNumber(),
                    USER_USER_VALID_ENTITY_LIST.get(2).getEmail(), USERS_ROLE, USERS_REGISTRATION_DATE
            )
    );
}
