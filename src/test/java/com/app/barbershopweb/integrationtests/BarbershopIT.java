package com.app.barbershopweb.integrationtests;


import com.app.barbershopweb.barbershop.BarbershopConverter;
import com.app.barbershopweb.barbershop.BarbershopDto;
import com.app.barbershopweb.barbershop.repository.JdbcBarbershopRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;



@DisplayName("Barbershop IT without error handling")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BarbershopIT extends AbstractIT{

    private final String url = "/barbershops";

    @Autowired
    private JdbcBarbershopRepository barbershopRepository;

    @Autowired
    private BarbershopConverter barbershopConverter;

    @Autowired
    private TestRestTemplate restTemplate;

    @DisplayName("gives empty Barbershop List(array) when they're no added barbershops yet")
    @Test
    @Order(1)
    void shouldReturnEmptyBarbershopList() {

        ResponseEntity<BarbershopDto[]> response = restTemplate.getForEntity(url, BarbershopDto[].class);

        assertEquals(0, Objects.requireNonNull(response.getBody()).length);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @DisplayName("POST: " + url +
            " after saving barbershop entity returns its id and status code 201")
    @Test
    @Order(2)
    void shouldAddBarbershop() {
        BarbershopDto dto = new BarbershopDto(
                1L, "a1",
                "name1", "+38091",
                "1@gmail.com"
        );

        ResponseEntity<Long> response = restTemplate.postForEntity(url, dto, Long.class);

        assertEquals(1L, response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @DisplayName("GET: " + url + "/{barbershopId}" +
            " returns corresponding barbershop dto")
    @Test
    @Order(3)
    void shouldReturnBarbershop() {
        long barbershopId = 1L;

        ResponseEntity<BarbershopDto> response = restTemplate.getForEntity(url + "/" + barbershopId, BarbershopDto.class);
        BarbershopDto body = response.getBody();
        assertEquals(barbershopId, Objects.requireNonNull(body).id());
        assertEquals("a1", Objects.requireNonNull(body).address());
        assertEquals("name1", Objects.requireNonNull(body).name());
        assertEquals("+38091", Objects.requireNonNull(body).phoneNumber());
        assertEquals("1@gmail.com", Objects.requireNonNull(body).email());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("PUT: " + url +
            " should return updated barbershop (dto)")
    @Order(4)
    void shouldReturnUpdatedBarbershop() {
        BarbershopDto barbershopDtoRequest = new BarbershopDto(
                1L, "A1",
                "Barbershop1", "+38091",
                "1@gmail.com"
        );

        HttpEntity<BarbershopDto>entity = new HttpEntity<>(barbershopDtoRequest);
        ResponseEntity<BarbershopDto> response = restTemplate.exchange(url, HttpMethod.PUT, entity, BarbershopDto.class);
        BarbershopDto body = response.getBody();

        assertEquals(barbershopDtoRequest.id(), Objects.requireNonNull(body).id());
        assertEquals(barbershopDtoRequest.address(), Objects.requireNonNull(body).address());
        assertEquals(barbershopDtoRequest.name(), Objects.requireNonNull(body).name());
        assertEquals(barbershopDtoRequest.phoneNumber(), Objects.requireNonNull(body).phoneNumber());
        assertEquals(barbershopDtoRequest.email(), Objects.requireNonNull(body).email());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @DisplayName("GET: " + url +
            " gives all barbershops at once")
    @Test
    @Order(5)
    void shouldReturnAllBarbershops() {
        List<BarbershopDto> barbershopDtos = List.of(
                new BarbershopDto(1L, "A1", "Barbershop1", "+38091", "1@gmail.com"),
                new BarbershopDto(2L, "A2", "Barbershop2", "+38092", "2@gmail.com"),
                new BarbershopDto(3L, "A3", "Barbershop3", "+38093", "3@gmail.com")
        );

        barbershopRepository.addBarbershop(
                barbershopConverter.mapToEntity(barbershopDtos.get(1))
        );
        barbershopRepository.addBarbershop(
                barbershopConverter.mapToEntity(barbershopDtos.get(2))
        );

        ResponseEntity<BarbershopDto[]> response = restTemplate.getForEntity(url, BarbershopDto[].class);
        BarbershopDto[] body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(barbershopDtos.size(), Objects.requireNonNull(body).length);
        assertEquals(barbershopDtos.get(0), (Objects.requireNonNull(body)[0]));
        assertEquals(barbershopDtos.get(1), (Objects.requireNonNull(body)[1]));
        assertEquals(barbershopDtos.get(2), (Objects.requireNonNull(body)[2]));

    }

    @DisplayName("DELETE: " + url + "{barbershopId}" +
            " returns empty body, status code 200, " +
            "when: barbershop with existing / not existing id was deleted")
    @Test
    @Order(6)
    void shouldDeleteBarbershopById() {
        long barbershopId = 1L;

        ResponseEntity<Object> response = restTemplate.exchange(url + "/" + barbershopId, HttpMethod.DELETE, null, Object.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        assertTrue(barbershopRepository.findBarbershopById(barbershopId).isEmpty());
    }


}
