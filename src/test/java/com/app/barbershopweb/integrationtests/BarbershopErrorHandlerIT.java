package com.app.barbershopweb.integrationtests;

import com.app.barbershopweb.barbershop.BarbershopDto;
import com.app.barbershopweb.barbershop.controller.BarbershopTestConstants;
import com.app.barbershopweb.error.ErrorDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


@DisplayName("barbershop error handling IT")
class BarbershopErrorHandlerIT extends AbstractIT{

    private final String BARBERSHOPS_URL = "/barbershops";

    @Autowired
    private TestRestTemplate restTemplate;

    private final BarbershopTestConstants btc = new BarbershopTestConstants();

    @DisplayName("POST: " + BARBERSHOPS_URL +
            " when barbershop dto isn't valid " +
            "returns status code 400")
    @Test
    void whenBarbershopDtoNotValidPost() {
        final ResponseEntity<?> response = restTemplate.postForEntity(BARBERSHOPS_URL, btc.INVALID_BARBERSHOP_DTO, Object.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @DisplayName("GET: " + BARBERSHOPS_URL + "/{barbershopId} " +
            "When path variable input 'barbershopId' isn't valid" +
            " returns status code 400 (BAD_REQUEST) & error dto")
    @Test
    void whenBarbershopIdNotValidGet() {
        String errorMessage = "'barbershopId' must be greater than or equal to 1";
        ResponseEntity<ErrorDto> response = restTemplate.getForEntity(
                BARBERSHOPS_URL + "/" + btc.INVALID_BARBERSHOP_ID,
                ErrorDto.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, Objects.requireNonNull(response.getBody()).errors().get(0));
        assertEquals(1, response.getBody().errors().size());
    }

    @DisplayName("GET: " + BARBERSHOPS_URL + "/{barbershopId} " +
            "when there's no barbershop with id 'barbershopId' " +
            "returns status code 404 & error dto")
    @Test
    void whenNotExistedBarbershopId() {
        ResponseEntity<ErrorDto> response = restTemplate.getForEntity(BARBERSHOPS_URL + "/" + btc.NOT_EXISTED_BARBERSHOP_ID, ErrorDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Barbershop with id " + btc.NOT_EXISTED_BARBERSHOP_ID + " not found.",
                Objects.requireNonNull(response.getBody()).errors().get(0));
        assertEquals(1, response.getBody().errors().size());
    }

    @DisplayName("PUT: " + BARBERSHOPS_URL +
            " when barbershop dto isn't valid " +
            "returns status code 400")
    @Test
    void whenBarbershopDtoNotValidPut() {
        HttpEntity<BarbershopDto> requestEntity = new HttpEntity<>(btc.INVALID_BARBERSHOP_DTO);
        ResponseEntity<Object> response = restTemplate.exchange(BARBERSHOPS_URL, HttpMethod.PUT, requestEntity, Object.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("PUT: " + BARBERSHOPS_URL +
            " when entity with 'id' in barbershopDto doesn't exist " +
            "returns status code 404 and error dto")
    void whenNotFoundBarbershopId() {
        HttpEntity<BarbershopDto> requestEntity = new HttpEntity<>(btc.BARBERSHOP_DTO_NOT_EXISTED_ID);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(BARBERSHOPS_URL, HttpMethod.PUT, requestEntity, ErrorDto.class);
        ErrorDto body = response.getBody();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(
                "Barbershop with id '" + btc.BARBERSHOP_DTO_NOT_EXISTED_ID.id() + "' not found.",
                Objects.requireNonNull(Objects.requireNonNull(body).errors().get(0))
        );
        assertEquals(1, body.errors().size());
    }

    @DisplayName("DELETE: " + BARBERSHOPS_URL + "/{barbershopId}" +
            " When path variable input 'barbershopId' isn't valid" +
            " returns status code 400 (BAD_REQUEST) & error dto")
    @Test
    void whenBarbershopIdNotValidDelete() {
        String errorMessage = "'barbershopId' must be greater than or equal to 1";

        ResponseEntity<ErrorDto> response = restTemplate.exchange(BARBERSHOPS_URL + "/" + btc.INVALID_BARBERSHOP_ID, HttpMethod.DELETE, null, ErrorDto.class);
        ErrorDto body = response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(body).errors().size());
        assertEquals(errorMessage, body.errors().get(0));
    }

}
