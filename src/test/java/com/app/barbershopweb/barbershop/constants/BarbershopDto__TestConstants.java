package com.app.barbershopweb.barbershop.constants;

import com.app.barbershopweb.barbershop.BarbershopDto;

import static com.app.barbershopweb.barbershop.constants.BarbershopEntity__TestConstants.BARBERSHOP_VALID_ENTITY;
import static com.app.barbershopweb.barbershop.constants.BarbershopMetadata__TestConstants.*;

public final class BarbershopDto__TestConstants {

    public static final BarbershopDto BARBERSHOP_VALID_DTO = new BarbershopDto(
            BARBERSHOP_VALID_BARBERSHOP_ID, BARBERSHOP_VALID_ENTITY.getAddress(),
            BARBERSHOP_VALID_ENTITY.getName(), BARBERSHOP_VALID_ENTITY.getPhoneNumber(),
            BARBERSHOP_VALID_ENTITY.getEmail(), BARBERSHOP_TIME_FROM, BARBERSHOP_TIME_TO
    );

    public static final BarbershopDto BARBERSHOP_INVALID_DTO = new BarbershopDto(
            BARBERSHOP_INVALID_BARBERSHOP_ID, BARBERSHOP_VALID_DTO.address(),
            "", "",
            BARBERSHOP_VALID_DTO.email(), BARBERSHOP_TIME_FROM, BARBERSHOP_TIME_TO
    );

    public static final BarbershopDto BARBERSHOP_NOT_EXISTED_ID_DTO = new BarbershopDto(
            BARBERSHOP_NOT_EXISTED_BARBERSHOP_ID, BARBERSHOP_VALID_DTO.address(),
            BARBERSHOP_VALID_DTO.name(), BARBERSHOP_VALID_DTO.phoneNumber(),
            BARBERSHOP_VALID_DTO.email(), BARBERSHOP_TIME_FROM, BARBERSHOP_TIME_TO
    );

    public static final BarbershopDto BARBERSHOP_VALID_UPDATED_DTO = new BarbershopDto(
            BARBERSHOP_VALID_BARBERSHOP_ID, "A1",
            "Barbershop1", BARBERSHOP_VALID_DTO.phoneNumber(),
            BARBERSHOP_VALID_DTO.email(), BARBERSHOP_TIME_FROM, BARBERSHOP_TIME_TO
    );

}
