package com.app.barbershopweb.user.converter;

import com.app.barbershopweb.user.UserConverter;
import com.app.barbershopweb.user.Users;
import com.app.barbershopweb.user.UsersDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.app.barbershopweb.user.constants.UserDto__TestConstants.USERS_VALID_USER_DTO;
import static com.app.barbershopweb.user.constants.UserEntity__TestConstants.USERS_VALID_ENTITY;
import static com.app.barbershopweb.user.constants.UserList__TestConstants.USERS_USER_VALID_DTO_LIST;
import static com.app.barbershopweb.user.constants.UserList__TestConstants.USERS_USER_VALID_ENTITY_LIST;
import static com.app.barbershopweb.user.constants.UserMetadata__TestConstants.USERS_FIELD_AMOUNT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UsersConverterTest {

    private final UserConverter userConverter = new UserConverter();


    @Test
    void userDtoListToEntityList() {

        List<Users> users = USERS_USER_VALID_DTO_LIST.stream()
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
                        USERS_USER_VALID_DTO_LIST
                )
        );
    }

    @Test
    void userEntityListToDtoList() {
        List<UsersDto> dtos = USERS_USER_VALID_ENTITY_LIST.stream()
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
                        USERS_USER_VALID_ENTITY_LIST
                )
        );
    }

    @Test
    void mapToEntity() {
        assertEquals(
                USERS_VALID_ENTITY,
                userConverter.mapToEntity(USERS_VALID_USER_DTO)
        );
    }

    @Test
    void mapToDto() {
        assertEquals(
                USERS_VALID_USER_DTO,
                userConverter.mapToDto(USERS_VALID_ENTITY)
        );
    }

    @Test
    void actualFieldAmount() {
        assertEquals(Users.class.getDeclaredFields().length, USERS_FIELD_AMOUNT);
    }
}
