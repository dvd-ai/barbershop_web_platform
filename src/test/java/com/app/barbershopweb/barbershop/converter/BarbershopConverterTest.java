package com.app.barbershopweb.barbershop.converter;

import com.app.barbershopweb.barbershop.Barbershop;
import com.app.barbershopweb.barbershop.BarbershopConverter;
import com.app.barbershopweb.barbershop.BarbershopDto;
import com.app.barbershopweb.barbershop.BarbershopTestConstants;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BarbershopConverterTest {

    private final BarbershopConverter barbershopConverter = new BarbershopConverter();
    private final BarbershopTestConstants btc = new BarbershopTestConstants();

    @Test
    void barbershopDtoListToEntityList() {

        List<Barbershop> barbershops = btc.VALID_BARBERSHOP_DTO_LIST.stream()
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
                        btc.VALID_BARBERSHOP_DTO_LIST
                )
        );
    }

    @Test
    void barbershopEntityListToDtoList() {
        List<BarbershopDto> dtos = btc.VALID_BARBERSHOP_ENTITY_LIST.stream()
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
                        btc.VALID_BARBERSHOP_ENTITY_LIST
                )
        );
    }

    @Test
    void mapToEntity() {
        assertEquals(
                btc.VALID_BARBERSHOP_ENTITY,
                barbershopConverter.mapToEntity(btc.VALID_BARBERSHOP_DTO)
        );
    }

    @Test
    void mapToDto() {
        assertEquals(
                btc.VALID_BARBERSHOP_DTO,
                barbershopConverter.mapToDto(btc.VALID_BARBERSHOP_ENTITY)
        );
    }

    @Test
    void actualFieldAmount() {
        assertEquals(Barbershop.class.getDeclaredFields().length, btc.BARBERSHOP_FIELD_AMOUNT);
    }
}