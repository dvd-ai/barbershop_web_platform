package com.app.barbershopweb.barbershop.converter;

import com.app.barbershopweb.barbershop.Barbershop;
import com.app.barbershopweb.barbershop.BarbershopConverter;
import com.app.barbershopweb.barbershop.BarbershopDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BarbershopConverterTest {

    private final BarbershopConverter barbershopConverter = new BarbershopConverter();

    @Test
    void barbershopDtoListToEntityList() {
        List<BarbershopDto> dtos = List.of(
          new BarbershopDto(1L, "1a", "name1", "+38091", "1@gmail.com"),
          new BarbershopDto(2L, "2a", "name2", "+38092", "2@gmail.com"),
          new BarbershopDto(3L, "3a", "name3", "+38093", "3@gmail.com"),
          new BarbershopDto(4L, "4a", "name4", "+38094", "4@gmail.com"),
          new BarbershopDto(5L, "5a", "name5", "+38095", "5@gmail.com")
        );

        List<Barbershop> barbershops = dtos.stream()
                .map(dto -> new Barbershop(
                        dto.id(),
                        dto.address(),
                        dto.name(),
                        dto.phoneNumber(),
                        dto.email()
                ))
                .toList();

        assertEquals(barbershops, barbershopConverter.barbershopDtoListToEntityList(dtos));
    }

    @Test
    void barbershopEntityListToDtoList() {

        List<Barbershop> barbershops = List.of(
                new Barbershop(1L, "1a", "name1", "+38091", "1@gmail.com"),
                new Barbershop(2L, "2a", "name2", "+38092", "2@gmail.com"),
                new Barbershop(3L, "3a", "name3", "+38093", "3@gmail.com"),
                new Barbershop(4L, "4a", "name4", "+38094", "4@gmail.com"),
                new Barbershop(5L, "5a", "name5", "+38095", "5@gmail.com")
        );

        List<BarbershopDto> dtos = barbershops.stream()
                .map(entity -> new BarbershopDto(
                        entity.getId(),
                        entity.getAddress(),
                        entity.getName(),
                        entity.getPhoneNumber(),
                        entity.getEmail()
                ))
                .toList();

        assertEquals(dtos, barbershopConverter.barbershopEntityListToDtoList(barbershops));
    }

    @Test
    void mapToEntity() {

        BarbershopDto dto = new BarbershopDto(
                1L,
                "Address1",
                "My Barbershop",
                "1234567890",
                "abcd@gmail.com"
        );

        Barbershop barbershop = new Barbershop(
                dto.id(),
                dto.address(),
                dto.name(),
                dto.phoneNumber(),
                dto.email()
        );

        assertEquals(barbershop, barbershopConverter.mapToEntity(dto));
    }

    @Test
    void mapToDto() {

        Barbershop barbershop = new Barbershop(
                1L,
                "Address1",
                "My Barbershop",
                "1234567890",
                "abcd@gmail.com"
        );

        BarbershopDto dto = new BarbershopDto(
                barbershop.getId(),
                barbershop.getAddress(),
                barbershop.getName(),
                barbershop.getPhoneNumber(),
                barbershop.getEmail()
        );

        assertEquals(dto, barbershopConverter.mapToDto(barbershop));
    }
}