package com.app.barbershopweb.user.crud;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserConverter {

    public List<User> userDtoListToEntityList(List<UsersDto> dtos) {
        return dtos.stream()
                .map(this::mapToEntity)
                .toList();
    }

    public List<UsersDto> userEntityListToDtoList(List<User> entities) {
        return entities.stream()
                .map(this::mapToDto)
                .toList();
    }

    public User mapToEntity(UsersDto dto) {
        return new User(
                dto.id(),
                dto.firstName(),
                dto.lastName(),
                dto.phoneNumber(),
                dto.email(),
                dto.role(),
                dto.registrationDate()

        );
    }

    public UsersDto mapToDto(User entity) {
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
