package com.app.barbershopweb.barbershop.crud;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BarbershopConverter {

    public List<Barbershop> barbershopDtoListToEntityList(List<BarbershopDto> dtos) {
        return dtos.stream()
                .map(this::mapToEntity)
                .toList();
    }

    public List<BarbershopDto> barbershopEntityListToDtoList(List<Barbershop> entities) {
        return entities.stream()
                .map(this::mapToDto)
                .toList();
    }

    public Barbershop mapToEntity(BarbershopDto dto) {
        return new Barbershop(
                dto.id(),
                dto.address(),
                dto.name(),
                dto.phoneNumber(),
                dto.email(),
                dto.workTimeFrom(),
                dto.workTimeTo(),
                dto.isActive()
        );
    }

    public BarbershopDto mapToDto(Barbershop entity) {
        return new BarbershopDto(
                entity.getId(),
                entity.getAddress(),
                entity.getName(),
                entity.getPhoneNumber(),
                entity.getEmail(),
                entity.getWorkTimeFrom(),
                entity.getWorkTimeTo(),
                entity.getActive()
        );
    }
}
