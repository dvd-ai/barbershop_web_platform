package com.app.barbershopweb.integrationtests.user;

import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.user.UserConverter;
import com.app.barbershopweb.user.UserTestConstants;
import com.app.barbershopweb.user.UsersDto;
import com.app.barbershopweb.user.repository.JdbcUsersRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static com.app.barbershopweb.user.UserTestConstants.USERS_URL;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("User IT without error handling")
class UserIT extends AbstractIT {

    @Autowired
    private JdbcUsersRepository userRepository;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private TestRestTemplate restTemplate;

    private final UserTestConstants btc = new UserTestConstants();

    @DisplayName("GET: " + USERS_URL +
            " gives empty User List(array) when they're no added users yet")
    @Test
    @Order(1)
    void shouldReturnEmptyUserList() {

        ResponseEntity<UsersDto[]> response = restTemplate.getForEntity(USERS_URL, UsersDto[].class);

        assertEquals(0, Objects.requireNonNull(response.getBody()).length);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @DisplayName("POST: " + USERS_URL +
            " after saving user entity returns its id and status code 201")
    @Test
    @Order(2)
    void shouldAddUser() {
        ResponseEntity<Long> response = restTemplate.postForEntity(
                USERS_URL, btc.VALID_USER_DTO, Long.class
        );

        assertEquals(btc.VALID_USER_ID, response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @DisplayName("GET: " + USERS_URL + "/{userId}" +
            " returns corresponding user dto")
    @Test
    @Order(3)
    void shouldReturnUser() {
        ResponseEntity<UsersDto> response = restTemplate.getForEntity(
                USERS_URL + "/" + btc.VALID_USER_ID, UsersDto.class
        );
        UsersDto body = response.getBody();
        assertEquals(btc.VALID_USER_ID, Objects.requireNonNull(body).id());
        assertEquals(btc.VALID_USER_DTO.firstName(), Objects.requireNonNull(body).firstName());
        assertEquals(btc.VALID_USER_DTO.lastName(), Objects.requireNonNull(body).lastName());
        assertEquals(btc.VALID_USER_DTO.phoneNumber(), Objects.requireNonNull(body).phoneNumber());
        assertEquals(btc.VALID_USER_DTO.email(), Objects.requireNonNull(body).email());
        assertEquals(btc.VALID_USER_DTO.role(), Objects.requireNonNull(body).role());
        assertEquals(btc.VALID_USER_DTO.registrationDate(), Objects.requireNonNull(body).registrationDate());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(btc.USERS_FIELD_AMOUNT, UsersDto.class.getDeclaredFields().length);
    }

    @Test
    @DisplayName("PUT: " + USERS_URL +
            " should return updated user (dto)")
    @Order(4)
    void shouldReturnUpdatedUser() {
        HttpEntity<UsersDto> entity = new HttpEntity<>(btc.VALID_UPDATED_USER_DTO);
        ResponseEntity<UsersDto> response = restTemplate.exchange(USERS_URL, HttpMethod.PUT, entity, UsersDto.class);
        UsersDto body = response.getBody();

        assertEquals(btc.VALID_UPDATED_USER_DTO.id(), Objects.requireNonNull(body).id());
        assertEquals(btc.VALID_UPDATED_USER_DTO.firstName(), Objects.requireNonNull(body).firstName());
        assertEquals(btc.VALID_UPDATED_USER_DTO.lastName(), Objects.requireNonNull(body).lastName());
        assertEquals(btc.VALID_UPDATED_USER_DTO.phoneNumber(), Objects.requireNonNull(body).phoneNumber());
        assertEquals(btc.VALID_UPDATED_USER_DTO.email(), Objects.requireNonNull(body).email());
        assertEquals(btc.VALID_UPDATED_USER_DTO.role(), Objects.requireNonNull(body).role());
        assertEquals(btc.VALID_UPDATED_USER_DTO.registrationDate(), Objects.requireNonNull(body).registrationDate());
        assertEquals(btc.USERS_FIELD_AMOUNT, UsersDto.class.getDeclaredFields().length);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @DisplayName("GET: " + USERS_URL +
            " gives all users at once")
    @Test
    @Order(5)
    void shouldReturnAllUsers() {
        userRepository.addUser(btc.VALID_USER_ENTITY_LIST.get(1));
        userRepository.addUser(btc.VALID_USER_ENTITY_LIST.get(2));

        ResponseEntity<UsersDto[]> response = restTemplate.getForEntity(USERS_URL, UsersDto[].class);
        UsersDto[] body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(btc.VALID_USER_DTO_LIST.size(), Objects.requireNonNull(body).length);
        assertEquals(btc.VALID_UPDATED_USER_DTO, (Objects.requireNonNull(body)[0]));
        assertEquals(btc.VALID_USER_DTO_LIST.get(1), (Objects.requireNonNull(body)[1]));
        assertEquals(btc.VALID_USER_DTO_LIST.get(2), (Objects.requireNonNull(body)[2]));

    }

    @DisplayName("DELETE: " + USERS_URL + "{userId}" +
            " returns empty body, status code 200, " +
            "when: user with existing / not existing id was deleted")
    @Test
    @Order(6)
    void shouldDeleteUserById() {
        ResponseEntity<Object> response = restTemplate.exchange(
                USERS_URL + "/" + btc.VALID_USER_ID,
                HttpMethod.DELETE, null, Object.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        assertTrue(userRepository.findUserById(btc.VALID_USER_ID).isEmpty());
    }


}
