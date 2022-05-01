package com.app.barbershopweb.user.crud;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserConverter {

    public List<User> userDtoListToEntityList(List<UserDto> dtos) {
        return dtos.stream()
                .map(this::mapToEntity)
                .toList();
    }

    public List<UserDto> userEntityListToDtoList(List<User> entities) {
        return entities.stream()
                .map(this::mapToDto)
                .toList();
    }

    public User mapToEntity(UserDto dto) {
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

    public UserDto mapToDto(User entity) {
        return new UserDto(
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
