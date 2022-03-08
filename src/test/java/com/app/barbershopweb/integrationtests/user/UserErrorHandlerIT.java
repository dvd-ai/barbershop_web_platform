package com.app.barbershopweb.integrationtests.user;

import com.app.barbershopweb.error.ErrorDto;
import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.user.UserTestConstants;
import com.app.barbershopweb.user.UsersDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static com.app.barbershopweb.user.UserTestConstants.USERS_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("user error handling IT")
class UserErrorHandlerIT extends AbstractIT {

    @Autowired
    private TestRestTemplate restTemplate;

    private final UserTestConstants utc = new UserTestConstants();

    @DisplayName("POST: " + USERS_URL +
            " when user dto isn't valid " +
            "returns status code 400 & error dto")
    @Test
    void whenUserDtoNotValidPost() {
        final ResponseEntity<ErrorDto> response = restTemplate.postForEntity(USERS_URL, utc.INVALID_USER_DTO, ErrorDto.class);
        ErrorDto body = response.getBody();


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(3, Objects.requireNonNull(body).errors().size());

        assertTrue(body.errors().contains(utc.DTO_CV_ID_ERR_MSG));
        assertTrue(body.errors().contains(utc.DTO_CV_LAST_NAME_ERR_MSG));
        assertTrue(body.errors().contains(utc.DTO_CV_PHONE_NUMBER_ERR_MSG));
    }

    @DisplayName("GET: " + USERS_URL + "/{userId} " +
            "When path variable input 'userId' isn't valid" +
            " returns status code 400 (BAD_REQUEST) & error dto")
    @Test
    void whenUserIdNotValidGet() {
        ResponseEntity<ErrorDto> response = restTemplate.getForEntity(
                USERS_URL + "/" + utc.INVALID_USER_ID,
                ErrorDto.class
        );


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(utc.PV_USER_ID_ERR_MSG, Objects.requireNonNull(response.getBody()).errors().get(0));
        assertEquals(1, response.getBody().errors().size());
    }

    @DisplayName("GET: " + USERS_URL + "/{userId} " +
            "when there's no user with id 'userId' " +
            "returns status code 404 & error dto")
    @Test
    void whenNotExistedUserId() {
        ResponseEntity<ErrorDto> response = restTemplate.getForEntity(USERS_URL + "/" + utc.NOT_EXISTED_USER_ID, ErrorDto.class);


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Users with id '" + utc.NOT_EXISTED_USER_ID + "' not found.",
                Objects.requireNonNull(response.getBody()).errors().get(0));
        assertEquals(1, response.getBody().errors().size());
    }

    @DisplayName("PUT: " + USERS_URL +
            " when user dto isn't valid " +
            "returns status code 400 & error dto")
    @Test
    void whenUserDtoNotValidPut() {
        HttpEntity<UsersDto> requestEntity = new HttpEntity<>(utc.INVALID_USER_DTO);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(USERS_URL, HttpMethod.PUT, requestEntity, ErrorDto.class);
        ErrorDto body = response.getBody();


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(3, Objects.requireNonNull(body).errors().size());

        assertTrue(body.errors().contains(utc.DTO_CV_ID_ERR_MSG));
        assertTrue(body.errors().contains(utc.DTO_CV_LAST_NAME_ERR_MSG));
        assertTrue(body.errors().contains(utc.DTO_CV_PHONE_NUMBER_ERR_MSG));
    }

    @Test
    @DisplayName("PUT: " + USERS_URL +
            " when entity with 'id' in userDto doesn't exist " +
            "returns status code 404 and error dto")
    void whenNotFoundUserId() {
        HttpEntity<UsersDto> requestEntity = new HttpEntity<>(utc.USER_DTO_NOT_EXISTED_ID);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(USERS_URL, HttpMethod.PUT, requestEntity, ErrorDto.class);
        ErrorDto body = response.getBody();


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(
                "User with id '" + utc.USER_DTO_NOT_EXISTED_ID.id() + "' not found.",
                Objects.requireNonNull(Objects.requireNonNull(body).errors().get(0))
        );
        assertEquals(1, body.errors().size());
    }

    @DisplayName("DELETE: " + USERS_URL + "/{userId}" +
            " When path variable input 'userId' isn't valid" +
            " returns status code 400 (BAD_REQUEST) & error dto")
    @Test
    void whenUserIdNotValidDelete() {
        ResponseEntity<ErrorDto> response = restTemplate.exchange(USERS_URL + "/" + utc.INVALID_USER_ID, HttpMethod.DELETE, null, ErrorDto.class);
        ErrorDto body = response.getBody();


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(body).errors().size());
        assertEquals(utc.PV_USER_ID_ERR_MSG, body.errors().get(0));
    }
}
