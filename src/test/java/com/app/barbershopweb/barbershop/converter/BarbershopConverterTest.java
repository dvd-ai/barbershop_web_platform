package com.app.barbershopweb.barbershop.converter;

import com.app.barbershopweb.barbershop.Barbershop;
import com.app.barbershopweb.barbershop.BarbershopConverter;
import com.app.barbershopweb.barbershop.BarbershopDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.app.barbershopweb.barbershop.constants.BarbershopDto__TestConstants.BARBERSHOP_VALID_DTO;
import static com.app.barbershopweb.barbershop.constants.BarbershopEntity__TestConstants.BARBERSHOP_VALID_ENTITY;
import static com.app.barbershopweb.barbershop.constants.BarbershopList__TestConstants.BARBERSHOP_VALID_DTO_LIST;
import static com.app.barbershopweb.barbershop.constants.BarbershopList__TestConstants.BARBERSHOP_VALID_ENTITY_LIST;
import static com.app.barbershopweb.barbershop.constants.BarbershopMetadata__TestConstants.BARBERSHOP_FIELD_AMOUNT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BarbershopConverterTest {

    private final BarbershopConverter barbershopConverter = new BarbershopConverter();

    @Test
    void barbershopDtoListToEntityList() {

        List<Barbershop> barbershops = BARBERSHOP_VALID_DTO_LIST.stream()
                .map(dto -> new Barbershop(
                        dto.id(),
                        dto.address(),
                        dto.name(),
                        dto.phoneNumber(),
                        dto.email(),
                        dto.workTimeFrom(),
                        dto.workTimeTo()
                ))
                .toList();

        assertEquals(
                barbershops,
                barbershopConverter.barbershopDtoListToEntityList(
                        BARBERSHOP_VALID_DTO_LIST
                )
        );
    }

    @Test
    void barbershopEntityListToDtoList() {
        List<BarbershopDto> dtos = BARBERSHOP_VALID_ENTITY_LIST.stream()
                .map(entity -> new BarbershopDto(
                        entity.getId(),
                        entity.getAddress(),
                        entity.getName(),
                        entity.getPhoneNumber(),
                        entity.getEmail(),
                        entity.getWorkTimeFrom(),
                        entity.getWorkTimeTo()
                ))
                .toList();

        assertEquals(
                dtos,
                barbershopConverter.barbershopEntityListToDtoList(
                        BARBERSHOP_VALID_ENTITY_LIST
                )
        );
    }

    @Test
    void mapToEntity() {
        assertEquals(
                BARBERSHOP_VALID_ENTITY,
                barbershopConverter.mapToEntity(BARBERSHOP_VALID_DTO)
        );
    }

    @Test
    void mapToDto() {
        assertEquals(
                BARBERSHOP_VALID_DTO,
                barbershopConverter.mapToDto(BARBERSHOP_VALID_ENTITY)
        );
    }

    @Test
    void actualFieldAmount() {
        assertEquals(Barbershop.class.getDeclaredFields().length, BARBERSHOP_FIELD_AMOUNT);
    }
}