package com.app.barbershopweb.users;

import com.app.barbershopweb.user.Users;
import com.app.barbershopweb.user.UsersDto;

import java.time.LocalDateTime;
import java.util.List;

public final class UserTestConstants {

    public final int USERS_FIELD_AMOUNT = 7;
    public final static String USERS_URL = "/users";

    public final long INVALID_USER_ID = -100L;
    public final long VALID_USER_ID = 1L;
    public final long NOT_EXISTED_USER_ID = 100_000L;
    public final String ROLE = "admin";

    public final LocalDateTime REGISTRATION_DATE = LocalDateTime.of(2022, 12,
            15, 14,
            30);


    public final Users VALID_USER_ENTITY = new Users(
            VALID_USER_ID, "firstname1",
            "lastname1", "+38091",
            "1@gmail.com", ROLE, REGISTRATION_DATE
    );

    public final Users USER_ENTITY_NOT_EXISTED_ID = new Users(
            NOT_EXISTED_USER_ID, VALID_USER_ENTITY.getFirstName(),
            VALID_USER_ENTITY.getLastName(), VALID_USER_ENTITY.getPhoneNumber(),
            VALID_USER_ENTITY.getEmail(), VALID_USER_ENTITY.getRole(), REGISTRATION_DATE
    );

    public final UsersDto VALID_USER_DTO = new UsersDto(
            VALID_USER_ID, VALID_USER_ENTITY.getFirstName(),
            VALID_USER_ENTITY.getLastName(), VALID_USER_ENTITY.getPhoneNumber(),
            VALID_USER_ENTITY.getEmail(), VALID_USER_ENTITY.getRole(), REGISTRATION_DATE
    );

    public final UsersDto INVALID_USER_DTO = new UsersDto(
            INVALID_USER_ID, VALID_USER_DTO.firstName(),
            "", "",
            VALID_USER_DTO.email(), VALID_USER_DTO.role(), REGISTRATION_DATE
    );

    public final UsersDto USER_DTO_NOT_EXISTED_ID = new UsersDto(
            NOT_EXISTED_USER_ID, VALID_USER_DTO.firstName(),
            VALID_USER_DTO.lastName(), VALID_USER_DTO.phoneNumber(),
            VALID_USER_DTO.email(), VALID_USER_DTO.role(), REGISTRATION_DATE
    );

    public final UsersDto VALID_UPDATED_USER_DTO = new UsersDto(
            VALID_USER_DTO.id(), "Antonio",
            "Petru44i", VALID_USER_DTO.phoneNumber(),
            VALID_USER_DTO.email(), ROLE, REGISTRATION_DATE
    );


    public final List<Users> VALID_USER_ENTITY_LIST = List.of(
            new Users(
                    VALID_USER_ID, VALID_USER_ENTITY.getFirstName(),
                    VALID_USER_ENTITY.getLastName(), VALID_USER_ENTITY.getPhoneNumber(),
                    VALID_USER_ENTITY.getEmail(), VALID_USER_ENTITY.getRole(), REGISTRATION_DATE
            ),
            new Users(
                    VALID_USER_ID + 1, "firstname2",
                    "lastname2", "+38092",
                    "2@gmail.com", ROLE, REGISTRATION_DATE
            ),
            new Users(
                    VALID_USER_ID + 1, "firstname3",
                    "lastname3", "+38093",
                    "3@gmail.com", ROLE, REGISTRATION_DATE
            )
    );

    public final List<UsersDto>VALID_USER_DTO_LIST = List.of(
            new UsersDto(
                    VALID_USER_ID, VALID_USER_ENTITY_LIST.get(0).getFirstName(),
                    VALID_USER_ENTITY_LIST.get(0).getLastName(), VALID_USER_ENTITY_LIST.get(0).getPhoneNumber(),
                    VALID_USER_ENTITY_LIST.get(0).getEmail(), ROLE, REGISTRATION_DATE
            ),
            new UsersDto(
                    VALID_USER_ID + 1, VALID_USER_ENTITY_LIST.get(1).getFirstName(),
                    VALID_USER_ENTITY_LIST.get(1).getLastName(), VALID_USER_ENTITY_LIST.get(1).getPhoneNumber(),
                    VALID_USER_ENTITY_LIST.get(1).getEmail(), ROLE, REGISTRATION_DATE
            ),
            new UsersDto(
                    VALID_USER_ID + 2, VALID_USER_ENTITY_LIST.get(2).getFirstName(),
                    VALID_USER_ENTITY_LIST.get(2).getLastName(), VALID_USER_ENTITY_LIST.get(2).getPhoneNumber(),
                    VALID_USER_ENTITY_LIST.get(2).getEmail(), ROLE, REGISTRATION_DATE
            )
    );

    //ERROR MESSAGES:

    //CV means 'constraint violation'
    public final String DTO_CV_ID_ERR_MSG = "'userDto.id' must be greater than or equal to 1";
    public final String DTO_CV_PHONE_NUMBER_ERR_MSG = "'userDto.phoneNumber' must not be blank";
    public final String DTO_CV_LAST_NAME_ERR_MSG = "'userDto.lastName' must not be blank";
}
