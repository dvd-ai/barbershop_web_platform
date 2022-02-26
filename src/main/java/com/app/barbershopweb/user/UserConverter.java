package com.app.barbershopweb.user;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserConverter {

    public List<Users> userDtoListToEntityList(List<UsersDto> dtos) {
        return dtos.stream()
                .map(this::mapToEntity)
                .toList();
    }

    public List<UsersDto> userEntityListToDtoList(List<Users> entities) {
        return entities.stream()
                .map(this::mapToDto)
                .toList();
    }

    public Users mapToEntity(UsersDto dto) {
        return new Users(
                dto.id(),
                dto.firstName(),
                dto.lastName(),
                dto.phoneNumber(),
                dto.email(),
                dto.role(),
                dto.registrationDate()

        );
    }

    public UsersDto mapToDto(Users entity) {
        return new UsersDto(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getPhoneNumber(),
                entity.getEmail(),
                entity.getRole(),
                entity.getRegistrationDate()
        );
    }
}
