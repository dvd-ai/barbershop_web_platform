package com.app.barbershopweb.barbershop.crud.constants;

import com.app.barbershopweb.barbershop.crud.BarbershopDto;

public final class BarbershopDto__TestConstants {

    public static final BarbershopDto BARBERSHOP_VALID_DTO = new BarbershopDto(
            BarbershopMetadata__TestConstants.BARBERSHOP_VALID_BARBERSHOP_ID, BarbershopEntity__TestConstants.BARBERSHOP_VALID_ENTITY.getAddress(),
            BarbershopEntity__TestConstants.BARBERSHOP_VALID_ENTITY.getName(), BarbershopEntity__TestConstants.BARBERSHOP_VALID_ENTITY.getPhoneNumber(),
            BarbershopEntity__TestConstants.BARBERSHOP_VALID_ENTITY.getEmail(), BarbershopMetadata__TestConstants.BARBERSHOP_TIME_FROM, BarbershopMetadata__TestConstants.BARBERSHOP_TIME_TO,
            BarbershopEntity__TestConstants.BARBERSHOP_VALID_ENTITY.getActive()
    );

    public static final BarbershopDto BARBERSHOP_INVALID_DTO = new BarbershopDto(
            BarbershopMetadata__TestConstants.BARBERSHOP_INVALID_BARBERSHOP_ID, BARBERSHOP_VALID_DTO.address(),
            "", "",
            BARBERSHOP_VALID_DTO.email(), BarbershopMetadata__TestConstants.BARBERSHOP_TIME_FROM, BarbershopMetadata__TestConstants.BARBERSHOP_TIME_TO,
            BARBERSHOP_VALID_DTO.isActive()
    );

    public static final BarbershopDto BARBERSHOP_NOT_EXISTED_ID_DTO = new BarbershopDto(
            BarbershopMetadata__TestConstants.BARBERSHOP_NOT_EXISTED_BARBERSHOP_ID, BARBERSHOP_VALID_DTO.address(),
            BARBERSHOP_VALID_DTO.name(), BARBERSHOP_VALID_DTO.phoneNumber(),
            BARBERSHOP_VALID_DTO.email(), BarbershopMetadata__TestConstants.BARBERSHOP_TIME_FROM, BarbershopMetadata__TestConstants.BARBERSHOP_TIME_TO,
            BARBERSHOP_VALID_DTO.isActive()
    );

    public static final BarbershopDto BARBERSHOP_VALID_UPDATED_DTO = new BarbershopDto(
            BarbershopMetadata__TestConstants.BARBERSHOP_VALID_BARBERSHOP_ID, "A1",
            "Barbershop1", BARBERSHOP_VALID_DTO.phoneNumber(),
            BARBERSHOP_VALID_DTO.email(), BarbershopMetadata__TestConstants.BARBERSHOP_TIME_FROM, BarbershopMetadata__TestConstants.BARBERSHOP_TIME_TO,
            BARBERSHOP_VALID_DTO.isActive()
    );

}
