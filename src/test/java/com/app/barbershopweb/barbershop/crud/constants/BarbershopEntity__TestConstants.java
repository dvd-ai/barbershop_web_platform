package com.app.barbershopweb.barbershop.crud.constants;

import com.app.barbershopweb.barbershop.crud.Barbershop;

import static com.app.barbershopweb.barbershop.crud.constants.BarbershopMetadata__TestConstants.*;

public final class BarbershopEntity__TestConstants {

    public static final Barbershop BARBERSHOP_VALID_ENTITY = new Barbershop(
            BARBERSHOP_VALID_BARBERSHOP_ID, "a1",
            "name1", "+38091",
            "1@gmail.com", BARBERSHOP_TIME_FROM, BARBERSHOP_TIME_TO, true
    );

    public static final Barbershop BARBERSHOP_VALID_UPDATED_ENTITY = new Barbershop(
            BARBERSHOP_VALID_BARBERSHOP_ID, "A1",
            "Barbershop1", BARBERSHOP_VALID_ENTITY.getPhoneNumber(),
            BARBERSHOP_VALID_ENTITY.getEmail(), BARBERSHOP_TIME_FROM, BARBERSHOP_TIME_TO, true
    );

    public static final Barbershop BARBERSHOP_NOT_EXISTED_ID_ENTITY = new Barbershop(
            BARBERSHOP_NOT_EXISTED_BARBERSHOP_ID, BARBERSHOP_VALID_ENTITY.getAddress(),
            BARBERSHOP_VALID_ENTITY.getName(), BARBERSHOP_VALID_ENTITY.getPhoneNumber(),
            BARBERSHOP_VALID_ENTITY.getEmail(), BARBERSHOP_TIME_FROM, BARBERSHOP_TIME_TO, true
    );
}
