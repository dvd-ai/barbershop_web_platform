package com.app.barbershopweb.barbershop;

import java.time.LocalTime;
import java.util.List;


public final class BarbershopTestConstants {
    public final int BARBERSHOP_FIELD_AMOUNT = 7;
    public final static String BARBERSHOPS_URL = "/barbershops";

    public final long INVALID_BARBERSHOP_ID = -100L;
    public final long VALID_BARBERSHOP_ID = 1L;
    public final long NOT_EXISTED_BARBERSHOP_ID = 100_000L;

    public final LocalTime TIME_FROM = LocalTime.of(8, 0, 0);
    public final LocalTime TIME_TO = LocalTime.of(20, 0, 0);


    public final Barbershop VALID_BARBERSHOP_ENTITY = new Barbershop(
            VALID_BARBERSHOP_ID, "a1",
            "name1", "+38091",
            "1@gmail.com", TIME_FROM, TIME_TO
    );

    public final Barbershop BARBERSHOP_ENTITY_NOT_EXISTED_ID = new Barbershop(
            NOT_EXISTED_BARBERSHOP_ID, VALID_BARBERSHOP_ENTITY.getAddress(),
            VALID_BARBERSHOP_ENTITY.getName(), VALID_BARBERSHOP_ENTITY.getPhoneNumber(),
            VALID_BARBERSHOP_ENTITY.getEmail(), TIME_FROM, TIME_TO
    );

    public final BarbershopDto VALID_BARBERSHOP_DTO = new BarbershopDto(
            VALID_BARBERSHOP_ID, VALID_BARBERSHOP_ENTITY.getAddress(),
            VALID_BARBERSHOP_ENTITY.getName(), VALID_BARBERSHOP_ENTITY.getPhoneNumber(),
            VALID_BARBERSHOP_ENTITY.getEmail(), TIME_FROM, TIME_TO
    );

    public final BarbershopDto INVALID_BARBERSHOP_DTO = new BarbershopDto(
            INVALID_BARBERSHOP_ID, VALID_BARBERSHOP_DTO.address(),
            "", "",
            VALID_BARBERSHOP_DTO.email(), TIME_FROM, TIME_TO
    );

    public final BarbershopDto BARBERSHOP_DTO_NOT_EXISTED_ID = new BarbershopDto(
            NOT_EXISTED_BARBERSHOP_ID, VALID_BARBERSHOP_DTO.address(),
            VALID_BARBERSHOP_DTO.name(), VALID_BARBERSHOP_DTO.phoneNumber(),
            VALID_BARBERSHOP_DTO.email(), TIME_FROM, TIME_TO
    );

    public final BarbershopDto VALID_UPDATED_BARBERSHOP_DTO = new BarbershopDto(
            VALID_BARBERSHOP_ID, "A1",
            "Barbershop1", VALID_BARBERSHOP_DTO.phoneNumber(),
            VALID_BARBERSHOP_DTO.email(), TIME_FROM, TIME_TO
    );


    public final List<Barbershop>VALID_BARBERSHOP_ENTITY_LIST = List.of(
            new Barbershop(
                    VALID_BARBERSHOP_ID, VALID_BARBERSHOP_DTO.address(),
                    VALID_BARBERSHOP_DTO.name(), VALID_BARBERSHOP_DTO.phoneNumber(),
                    VALID_BARBERSHOP_DTO.email(), TIME_FROM, TIME_TO
            ),
            new Barbershop(
                    VALID_BARBERSHOP_ID + 1, "a2",
                    "name2", "+38092",
                    "2@gmail.com", TIME_FROM, TIME_TO
            ),
            new Barbershop(
                    VALID_BARBERSHOP_ID + 2, "a3",
                    "name3", "+38093",
                    "3@gmail.com", TIME_FROM, TIME_TO
            )
    );

    public final List<BarbershopDto>VALID_BARBERSHOP_DTO_LIST = List.of(
            new BarbershopDto(
                    VALID_BARBERSHOP_ID, VALID_BARBERSHOP_ENTITY_LIST.get(0).getAddress(),
                    VALID_BARBERSHOP_ENTITY_LIST.get(0).getName(), VALID_BARBERSHOP_ENTITY_LIST.get(0).getPhoneNumber(),
                    VALID_BARBERSHOP_ENTITY_LIST.get(0).getEmail(), TIME_FROM, TIME_TO
            ),
            new BarbershopDto(
                    VALID_BARBERSHOP_ID + 1, VALID_BARBERSHOP_ENTITY_LIST.get(1).getAddress(),
                    VALID_BARBERSHOP_ENTITY_LIST.get(1).getName(), VALID_BARBERSHOP_ENTITY_LIST.get(1).getPhoneNumber(),
                    VALID_BARBERSHOP_ENTITY_LIST.get(1).getEmail(), TIME_FROM, TIME_TO
            ),
            new BarbershopDto(
                    VALID_BARBERSHOP_ID + 2, VALID_BARBERSHOP_ENTITY_LIST.get(2).getAddress(),
                    VALID_BARBERSHOP_ENTITY_LIST.get(2).getName(), VALID_BARBERSHOP_ENTITY_LIST.get(2).getPhoneNumber(),
                    VALID_BARBERSHOP_ENTITY_LIST.get(2).getEmail(), TIME_FROM, TIME_TO
            )
    );

    //ERROR MESSAGES:

    //CV means 'constraint violation'
    public final String DTO_CV_ID_ERR_MSG = "'barbershopDto.id' must be greater than or equal to 1";
    public final String DTO_CV_PHONE_NUMBER_ERR_MSG = "'barbershopDto.phoneNumber' must not be blank";
    public final String DTO_CV_NAME_ERR_MSG = "'barbershopDto.name' must not be blank";
}