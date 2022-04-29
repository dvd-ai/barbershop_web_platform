package com.app.barbershopweb.integrationtests.user.crud;

import com.app.barbershopweb.error.ErrorDto;
import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.user.crud.UsersDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static com.app.barbershopweb.user.crud.constants.UserDto__TestConstants.USERS_INVALID_USER_DTO;
import static com.app.barbershopweb.user.crud.constants.UserDto__TestConstants.USERS_USER_DTO_NOT_EXISTED_ID;
import static com.app.barbershopweb.user.crud.constants.UserErrorMessage__TestConstants.*;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("user error handling IT")
class UserErrorHandlerIT extends AbstractIT {

    @Autowired
    private TestRestTemplate restTemplate;


    @DisplayName("POST: " + USERS_URL +
            " when user dto isn't valid " +
            "returns status code 400 & error dto")
    @Test
    void whenUserDtoNotValidPost() {
        final ResponseEntity<ErrorDto> response = restTemplate.postForEntity(USERS_URL, USERS_INVALID_USER_DTO, ErrorDto.class);
        ErrorDto body = response.getBody();


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(3, Objects.requireNonNull(body).errors().size());

        assertTrue(body.errors().contains(USER_ERR_INVALID_DTO_ID));
        assertTrue(body.errors().contains(USER_ERR_INVALID_DTO_LAST_NAME));
        assertTrue(body.errors().contains(USER_ERR_INVALID_DTO_PHONE_NUMBER));
    }

    @DisplayName("GET: " + USERS_URL + "/{userId} " +
            "When path variable input 'userId' isn't valid" +
            " returns status code 400 (BAD_REQUEST) & error dto")
    @Test
    void whenUserIdNotValidGet() {
        ResponseEntity<ErrorDto> response = restTemplate.getForEntity(
                USERS_URL + "/" + USERS_INVALID_USER_ID,
                ErrorDto.class
        );


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(USER_ERR_INVALID_PATH_VAR_USER_ID, Objects.requireNonNull(response.getBody()).errors().get(0));
        assertEquals(1, response.getBody().errors().size());
    }

    @DisplayName("GET: " + USERS_URL + "/{userId} " +
            "when there's no user with id 'userId' " +
            "returns status code 404 & error dto")
    @Test
    void whenNotExistedUserId() {
        ResponseEntity<ErrorDto> response = restTemplate.getForEntity(USERS_URL + "/" + USERS_NOT_EXISTING_USER_ID, ErrorDto.class);


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User with id '" + USERS_NOT_EXISTING_USER_ID + "' not found.",
                Objects.requireNonNull(response.getBody()).errors().get(0));
        assertEquals(1, response.getBody().errors().size());
    }

    @DisplayName("PUT: " + USERS_URL +
            " when user dto isn't valid " +
            "returns status code 400 & error dto")
    @Test
    void whenUserDtoNotValidPut() {
        HttpEntity<UsersDto> requestEntity = new HttpEntity<>(USERS_INVALID_USER_DTO);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(USERS_URL, HttpMethod.PUT, requestEntity, ErrorDto.class);
        ErrorDto body = response.getBody();


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(3, Objects.requireNonNull(body).errors().size());

        assertTrue(body.errors().contains(USER_ERR_INVALID_DTO_ID));
        assertTrue(body.errors().contains(USER_ERR_INVALID_DTO_LAST_NAME));
        assertTrue(body.errors().contains(USER_ERR_INVALID_DTO_PHONE_NUMBER));
    }

    @Test
    @DisplayName("PUT: " + USERS_URL +
            " when entity with 'id' in userDto doesn't exist " +
            "returns status code 404 and error dto")
    void whenNotFoundUserId() {
        HttpEntity<UsersDto> requestEntity = new HttpEntity<>(USERS_USER_DTO_NOT_EXISTED_ID);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(USERS_URL, HttpMethod.PUT, requestEntity, ErrorDto.class);
        ErrorDto body = response.getBody();


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(
                "User with id '" + USERS_USER_DTO_NOT_EXISTED_ID.id() + "' not found.",
                Objects.requireNonNull(Objects.requireNonNull(body).errors().get(0))
        );
        assertEquals(1, body.errors().size());
    }

    @DisplayName("DELETE: " + USERS_URL + "/{userId}" +
            " When path variable input 'userId' isn't valid" +
            " returns status code 400 (BAD_REQUEST) & error dto")
    @Test
    void whenUserIdNotValidDelete() {
        ResponseEntity<ErrorDto> response = restTemplate.exchange(USERS_URL + "/" + USERS_INVALID_USER_ID, HttpMethod.DELETE, null, ErrorDto.class);
        ErrorDto body = response.getBody();


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(body).errors().size());
        assertEquals(USER_ERR_INVALID_PATH_VAR_USER_ID, body.errors().get(0));
    }
}
