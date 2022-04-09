package com.app.barbershopweb.barbershop.constants;

import com.app.barbershopweb.barbershop.Barbershop;
import com.app.barbershopweb.barbershop.BarbershopDto;

import java.util.List;

import static com.app.barbershopweb.barbershop.constants.BarbershopDto__TestConstants.BARBERSHOP_VALID_DTO;
import static com.app.barbershopweb.barbershop.constants.BarbershopMetadata__TestConstants.*;

public final class BarbershopList__TestConstants {

    public static final List<Barbershop> BARBERSHOP_VALID_ENTITY_LIST = List.of(
            new Barbershop(
                    BARBERSHOP_VALID_BARBERSHOP_ID, BARBERSHOP_VALID_DTO.address(),
                    BARBERSHOP_VALID_DTO.name(), BARBERSHOP_VALID_DTO.phoneNumber(),
                    BARBERSHOP_VALID_DTO.email(), BARBERSHOP_TIME_FROM, BARBERSHOP_TIME_TO
            ),
            new Barbershop(
                    BARBERSHOP_VALID_BARBERSHOP_ID + 1, BARBERSHOP_VALID_DTO.address(),
                    BARBERSHOP_VALID_DTO.name(), BARBERSHOP_VALID_DTO.phoneNumber(),
                    BARBERSHOP_VALID_DTO.email(), BARBERSHOP_TIME_FROM, BARBERSHOP_TIME_TO
            ),
            new Barbershop(
                    BARBERSHOP_VALID_BARBERSHOP_ID + 2, BARBERSHOP_VALID_DTO.address(),
                    BARBERSHOP_VALID_DTO.name(), BARBERSHOP_VALID_DTO.phoneNumber(),
                    BARBERSHOP_VALID_DTO.email(), BARBERSHOP_TIME_FROM, BARBERSHOP_TIME_TO
            )
    );

    public static final List<BarbershopDto> BARBERSHOP_VALID_DTO_LIST = List.of(
            new BarbershopDto(
                    BARBERSHOP_VALID_BARBERSHOP_ID, BARBERSHOP_VALID_ENTITY_LIST.get(0).getAddress(),
                    BARBERSHOP_VALID_ENTITY_LIST.get(0).getName(), BARBERSHOP_VALID_ENTITY_LIST.get(0).getPhoneNumber(),
                    BARBERSHOP_VALID_ENTITY_LIST.get(0).getEmail(), BARBERSHOP_TIME_FROM, BARBERSHOP_TIME_TO
            ),
            new BarbershopDto(
                    BARBERSHOP_VALID_BARBERSHOP_ID + 1, BARBERSHOP_VALID_ENTITY_LIST.get(1).getAddress(),
                    BARBERSHOP_VALID_ENTITY_LIST.get(1).getName(), BARBERSHOP_VALID_ENTITY_LIST.get(1).getPhoneNumber(),
                    BARBERSHOP_VALID_ENTITY_LIST.get(1).getEmail(), BARBERSHOP_TIME_FROM, BARBERSHOP_TIME_TO
            ),
            new BarbershopDto(
                    BARBERSHOP_VALID_BARBERSHOP_ID + 2, BARBERSHOP_VALID_ENTITY_LIST.get(2).getAddress(),
                    BARBERSHOP_VALID_ENTITY_LIST.get(2).getName(), BARBERSHOP_VALID_ENTITY_LIST.get(2).getPhoneNumber(),
                    BARBERSHOP_VALID_ENTITY_LIST.get(2).getEmail(), BARBERSHOP_TIME_FROM, BARBERSHOP_TIME_TO
            )
    );

}
