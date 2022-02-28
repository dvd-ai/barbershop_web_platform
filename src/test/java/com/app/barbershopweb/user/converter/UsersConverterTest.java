package com.app.barbershopweb.user.converter;

import com.app.barbershopweb.user.UserConverter;
import com.app.barbershopweb.user.Users;
import com.app.barbershopweb.user.UsersDto;
import com.app.barbershopweb.user.UserTestConstants;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UsersConverterTest {

    private final UserConverter userConverter = new UserConverter();
    private final UserTestConstants utc = new UserTestConstants();

    @Test
    void userDtoListToEntityList() {

        List<Users> users = utc.VALID_USER_DTO_LIST.stream()
                .map(dto -> new Users(
                        dto.id(),
                        dto.firstName(),
                        dto.lastName(),
                        dto.phoneNumber(),
                        dto.email(),
                        dto.role(),
                        dto.registrationDate()
                ))
                .toList();

        assertEquals(
                users,
                userConverter.userDtoListToEntityList(
                        utc.VALID_USER_DTO_LIST
                )
        );
    }

    @Test
    void userEntityListToDtoList() {
        List<UsersDto> dtos = utc.VALID_USER_ENTITY_LIST.stream()
                .map(entity -> new UsersDto(
                        entity.getId(),
                        entity.getFirstName(),
                        entity.getLastName(),
                        entity.getPhoneNumber(),
                        entity.getEmail(),
                        entity.getRole(),
                        entity.getRegistrationDate()
                ))
                .toList();

        assertEquals(
                dtos,
                userConverter.userEntityListToDtoList(
                        utc.VALID_USER_ENTITY_LIST
                )
        );
    }

    @Test
    void mapToEntity() {
        assertEquals(
                utc.VALID_USER_ENTITY,
                userConverter.mapToEntity(utc.VALID_USER_DTO)
        );
    }

    @Test
    void mapToDto() {
        assertEquals(
                utc.VALID_USER_DTO,
                userConverter.mapToDto(utc.VALID_USER_ENTITY)
        );
    }

    @Test
    void actualFieldAmount() {
        assertEquals(Users.class.getFields().length, utc.USERS_FIELD_AMOUNT);
    }
}
