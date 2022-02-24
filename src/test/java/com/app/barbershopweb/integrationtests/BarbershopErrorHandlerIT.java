package com.app.barbershopweb.integrationtests;

import com.app.barbershopweb.barbershop.BarbershopDto;
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

    private final String url = "/barbershops";

    @Autowired
    private TestRestTemplate restTemplate;

    @DisplayName("POST: " + url +
            " when barbershop dto isn't valid " +
            "returns status code 400")
    @Test
    void whenBarbershopDtoNotValidPost() {
        BarbershopDto barbershopDto = new BarbershopDto(
                1L, "a1",
                "", "+38091",
                "1@gmail.com"
        );

        final ResponseEntity<?> response = restTemplate.postForEntity(url, barbershopDto, Object.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @DisplayName("GET: " + url + "/{barbershopId} " +
            "When path variable input 'barbershopId' isn't valid" +
            " returns status code 400 (BAD_REQUEST) & error dto")
    @Test
    void whenBarbershopIdNotValidGet() {
        long barbershopId = -100L;
        String errorMessage = "'barbershopId' must be greater than or equal to 1";
        ResponseEntity<ErrorDto> response = restTemplate.getForEntity(url + "/" + barbershopId, ErrorDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, Objects.requireNonNull(response.getBody()).errors().get(0));
        assertEquals(1, response.getBody().errors().size());
    }

    @DisplayName("GET: " + url + "/{barbershopId} " +
            "when there's no barbershop with id 'barbershopId' " +
            "returns status code 404 & error dto")
    @Test
    void whenNotExistedBarbershopId() {
        long barbershopId = 100_000L;

        ResponseEntity<ErrorDto> response = restTemplate.getForEntity(url + "/" + barbershopId, ErrorDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Barbershop with id " + barbershopId + " not found.",
                Objects.requireNonNull(response.getBody()).errors().get(0));
        assertEquals(1, response.getBody().errors().size());
    }

    @DisplayName("PUT: " + url +
            " when barbershop dto isn't valid " +
            "returns status code 400")
    @Test
    void whenBarbershopDtoNotValidPut() {

        BarbershopDto barbershopDto = new BarbershopDto(
                1L, "",
                "", "+38091",
                "1@gmail.com"
        );
        HttpEntity<BarbershopDto> requestEntity = new HttpEntity<>(barbershopDto);
        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Object.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("PUT: " + url +
            " when entity with 'id' in barbershopDto doesn't exist " +
            "returns status code 404 and error dto")
    void whenNotFoundBarbershopId() {
        BarbershopDto barbershopDto = new BarbershopDto(
                100_000L, "a1",
                "name1", "+38091",
                "1@gmail.com"
        );
        HttpEntity<BarbershopDto> requestEntity = new HttpEntity<>(barbershopDto);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, ErrorDto.class);
        ErrorDto body = response.getBody();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Barbershop with id '" + barbershopDto.id() + "' not found.",
                Objects.requireNonNull(Objects.requireNonNull(body).errors().get(0)));
        assertEquals(1, body.errors().size());
    }

    @DisplayName("DELETE: " + url + "/{barbershopId}" +
            " When path variable input 'barbershopId' isn't valid" +
            " returns status code 400 (BAD_REQUEST) & error dto")
    @Test
    void whenBarbershopIdNotValidDelete() {
        long barbershopId = -100L;
        String errorMessage = "'barbershopId' must be greater than or equal to 1";

        ResponseEntity<ErrorDto> response = restTemplate.exchange(url + "/" + barbershopId, HttpMethod.DELETE, null, ErrorDto.class);
        ErrorDto body = response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(body).errors().size());
        assertEquals(errorMessage, body.errors().get(0));
    }

}
