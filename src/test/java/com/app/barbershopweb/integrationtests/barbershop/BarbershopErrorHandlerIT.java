package com.app.barbershopweb.integrationtests.barbershop;

import com.app.barbershopweb.barbershop.BarbershopDto;
import com.app.barbershopweb.barbershop.BarbershopTestConstants;
import com.app.barbershopweb.error.ErrorDto;
import com.app.barbershopweb.integrationtests.AbstractIT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Objects;

import static com.app.barbershopweb.barbershop.BarbershopTestConstants.BARBERSHOPS_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DisplayName("barbershop error handling IT")
class BarbershopErrorHandlerIT extends AbstractIT {
    @Autowired
    private TestRestTemplate restTemplate;

    private final BarbershopTestConstants btc = new BarbershopTestConstants();

    @DisplayName("POST: " + BARBERSHOPS_URL +
            " when barbershop dto isn't valid " +
            "returns status code 400 & error dto")
    @Test
    void whenBarbershopDtoNotValidPost() {
        final ResponseEntity<ErrorDto> response = restTemplate.postForEntity(BARBERSHOPS_URL, btc.INVALID_BARBERSHOP_DTO, ErrorDto.class);
        ErrorDto body = response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(3, Objects.requireNonNull(body).errors().size());
        assertTrue(body.errors().contains(btc.DTO_CV_ID_ERR_MSG));
        assertTrue(body.errors().contains(btc.DTO_CV_PHONE_NUMBER_ERR_MSG));
        assertTrue(body.errors().contains(btc.DTO_CV_NAME_ERR_MSG));
    }

    @DisplayName("GET: " + BARBERSHOPS_URL + "/{barbershopId} " +
            "When path variable input 'barbershopId' isn't valid" +
            " returns status code 400 (BAD_REQUEST) & error dto")
    @Test
    void whenBarbershopIdNotValidGet() {
        ResponseEntity<ErrorDto> response = restTemplate.getForEntity(
                BARBERSHOPS_URL + "/" + btc.INVALID_BARBERSHOP_ID,
                ErrorDto.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(btc.PV_BARBERSHOP_ID_ERR_MSG, Objects.requireNonNull(response.getBody()).errors().get(0));
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
            "returns status code 400 & error dto")
    @Test
    void whenBarbershopDtoNotValidPut() {
        HttpEntity<BarbershopDto> requestEntity = new HttpEntity<>(btc.INVALID_BARBERSHOP_DTO);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(BARBERSHOPS_URL, HttpMethod.PUT, requestEntity, ErrorDto.class);
        ErrorDto body = response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(3, Objects.requireNonNull(body).errors().size());
        assertTrue(body.errors().contains(btc.DTO_CV_ID_ERR_MSG));
        assertTrue(body.errors().contains(btc.DTO_CV_PHONE_NUMBER_ERR_MSG));
        assertTrue(body.errors().contains(btc.DTO_CV_NAME_ERR_MSG));
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
        ResponseEntity<ErrorDto> response = restTemplate.exchange(BARBERSHOPS_URL + "/" + btc.INVALID_BARBERSHOP_ID, HttpMethod.DELETE, null, ErrorDto.class);
        ErrorDto body = response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(body).errors().size());
        assertEquals(btc.PV_BARBERSHOP_ID_ERR_MSG, body.errors().get(0));
    }

}
