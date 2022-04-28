package com.app.barbershopweb.barbershop.crud.converter;

import com.app.barbershopweb.barbershop.crud.Barbershop;
import com.app.barbershopweb.barbershop.crud.BarbershopConverter;
import com.app.barbershopweb.barbershop.crud.BarbershopDto;
import com.app.barbershopweb.barbershop.crud.constants.BarbershopList__TestConstants;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.app.barbershopweb.barbershop.crud.constants.BarbershopDto__TestConstants.BARBERSHOP_VALID_DTO;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopEntity__TestConstants.BARBERSHOP_VALID_ENTITY;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopMetadata__TestConstants.BARBERSHOP_FIELD_AMOUNT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BarbershopConverterTest {

    private final BarbershopConverter barbershopConverter = new BarbershopConverter();

    @Test
    void barbershopDtoListToEntityList() {

        List<Barbershop> barbershops = BarbershopList__TestConstants.BARBERSHOP_VALID_DTO_LIST.stream()
                .map(dto -> new Barbershop(
                        dto.id(),
                        dto.address(),
                        dto.name(),
                        dto.phoneNumber(),
                        dto.email(),
                        dto.workTimeFrom(),
                        dto.workTimeTo(),
                        dto.isActive()
                ))
                .toList();

        assertEquals(
                barbershops,
                barbershopConverter.barbershopDtoListToEntityList(
                        BarbershopList__TestConstants.BARBERSHOP_VALID_DTO_LIST
                )
        );
    }

    @Test
    void barbershopEntityListToDtoList() {
        List<BarbershopDto> dtos = BarbershopList__TestConstants.BARBERSHOP_VALID_ENTITY_LIST.stream()
                .map(entity -> new BarbershopDto(
                        entity.getId(),
                        entity.getAddress(),
                        entity.getName(),
                        entity.getPhoneNumber(),
                        entity.getEmail(),
                        entity.getWorkTimeFrom(),
                        entity.getWorkTimeTo(),
                        entity.IsActive()
                ))
                .toList();

        assertEquals(
                dtos,
                barbershopConverter.barbershopEntityListToDtoList(
                        BarbershopList__TestConstants.BARBERSHOP_VALID_ENTITY_LIST
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