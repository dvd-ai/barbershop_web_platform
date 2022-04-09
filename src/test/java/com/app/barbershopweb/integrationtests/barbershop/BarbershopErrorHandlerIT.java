package com.app.barbershopweb.integrationtests.barbershop;

import com.app.barbershopweb.barbershop.BarbershopDto;
import com.app.barbershopweb.error.ErrorDto;
import com.app.barbershopweb.integrationtests.AbstractIT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static com.app.barbershopweb.barbershop.constants.BarbershopDto__TestConstants.BARBERSHOP_INVALID_DTO;
import static com.app.barbershopweb.barbershop.constants.BarbershopDto__TestConstants.BARBERSHOP_NOT_EXISTED_ID_DTO;
import static com.app.barbershopweb.barbershop.constants.BarbershopErrorMessage__TestConstants.*;
import static com.app.barbershopweb.barbershop.constants.BarbershopMetadata__TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DisplayName("barbershop error handling IT")
class BarbershopErrorHandlerIT extends AbstractIT {
    @Autowired
    private TestRestTemplate restTemplate;

    @DisplayName("POST: " + BARBERSHOPS_URL +
            " when barbershop dto isn't valid " +
            "returns status code 400 & error dto")
    @Test
    void whenBarbershopDtoNotValidPost() {
        final ResponseEntity<ErrorDto> response = restTemplate.postForEntity(BARBERSHOPS_URL, BARBERSHOP_INVALID_DTO, ErrorDto.class);
        ErrorDto body = response.getBody();


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(3, Objects.requireNonNull(body).errors().size());

        assertTrue(body.errors().contains(BARBERSHOP_ERR_INVALID_DTO_ID));
        assertTrue(body.errors().contains(BARBERSHOP_ERR_INVALID_DTO_PHONE_NUMBER));
        assertTrue(body.errors().contains(BARBERSHOP_ERR_INVALID_DTO_NAME));
    }

    @DisplayName("GET: " + BARBERSHOPS_URL + "/{barbershopId} " +
            "When path variable input 'barbershopId' isn't valid" +
            " returns status code 400 (BAD_REQUEST) & error dto")
    @Test
    void whenBarbershopIdNotValidGet() {
        ResponseEntity<ErrorDto> response = restTemplate.getForEntity(
                BARBERSHOPS_URL + "/" + BARBERSHOP_INVALID_BARBERSHOP_ID,
                ErrorDto.class
        );


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(BARBERSHOP_ERR_INVALID_PATH_VAR_BARBERSHOP_ID, Objects.requireNonNull(response.getBody()).errors().get(0));
        assertEquals(1, response.getBody().errors().size());
    }

    @DisplayName("GET: " + BARBERSHOPS_URL + "/{barbershopId} " +
            "when there's no barbershop with id 'barbershopId' " +
            "returns status code 404 & error dto")
    @Test
    void whenNotExistedBarbershopId() {
        ResponseEntity<ErrorDto> response = restTemplate.getForEntity(BARBERSHOPS_URL + "/" + BARBERSHOP_NOT_EXISTED_BARBERSHOP_ID, ErrorDto.class);


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Barbershop with id '" + BARBERSHOP_NOT_EXISTED_BARBERSHOP_ID + "' not found.",
                Objects.requireNonNull(response.getBody()).errors().get(0));
        assertEquals(1, response.getBody().errors().size());
    }

    @DisplayName("PUT: " + BARBERSHOPS_URL +
            " when barbershop dto isn't valid " +
            "returns status code 400 & error dto")
    @Test
    void whenBarbershopDtoNotValidPut() {
        HttpEntity<BarbershopDto> requestEntity = new HttpEntity<>(BARBERSHOP_INVALID_DTO);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(BARBERSHOPS_URL, HttpMethod.PUT, requestEntity, ErrorDto.class);
        ErrorDto body = response.getBody();


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(3, Objects.requireNonNull(body).errors().size());

        assertTrue(body.errors().contains(BARBERSHOP_ERR_INVALID_DTO_ID));
        assertTrue(body.errors().contains(BARBERSHOP_ERR_INVALID_DTO_PHONE_NUMBER));
        assertTrue(body.errors().contains(BARBERSHOP_ERR_INVALID_DTO_NAME));
    }

    @Test
    @DisplayName("PUT: " + BARBERSHOPS_URL +
            " when entity with 'id' in barbershopDto doesn't exist " +
            "returns status code 404 and error dto")
    void whenNotFoundBarbershopId() {
        HttpEntity<BarbershopDto> requestEntity = new HttpEntity<>(BARBERSHOP_NOT_EXISTED_ID_DTO);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(BARBERSHOPS_URL, HttpMethod.PUT, requestEntity, ErrorDto.class);
        ErrorDto body = response.getBody();


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(
                "Barbershop with id '" + BARBERSHOP_NOT_EXISTED_ID_DTO.id() + "' not found.",
                Objects.requireNonNull(Objects.requireNonNull(body).errors().get(0))
        );
        assertEquals(1, body.errors().size());
    }

    @DisplayName("DELETE: " + BARBERSHOPS_URL + "/{barbershopId}" +
            " When path variable input 'barbershopId' isn't valid" +
            " returns status code 400 (BAD_REQUEST) & error dto")
    @Test
    void whenBarbershopIdNotValidDelete() {
        ResponseEntity<ErrorDto> response = restTemplate.exchange(BARBERSHOPS_URL + "/" + BARBERSHOP_INVALID_BARBERSHOP_ID, HttpMethod.DELETE, null, ErrorDto.class);
        ErrorDto body = response.getBody();


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(body).errors().size());
        assertEquals(BARBERSHOP_ERR_INVALID_PATH_VAR_BARBERSHOP_ID, body.errors().get(0));
    }

}
