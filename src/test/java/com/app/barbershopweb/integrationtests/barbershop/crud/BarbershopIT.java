package com.app.barbershopweb.integrationtests.barbershop.crud;


import com.app.barbershopweb.barbershop.crud.BarbershopDto;
import com.app.barbershopweb.barbershop.crud.repository.JdbcBarbershopRepository;
import com.app.barbershopweb.integrationtests.AbstractIT;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static com.app.barbershopweb.barbershop.crud.constants.BarbershopDto__TestConstants.BARBERSHOP_VALID_DTO;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopDto__TestConstants.BARBERSHOP_VALID_UPDATED_DTO;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopList__TestConstants.BARBERSHOP_VALID_DTO_LIST;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopList__TestConstants.BARBERSHOP_VALID_ENTITY_LIST;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopMetadata__TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DisplayName("Barbershop IT without error handling")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BarbershopIT extends AbstractIT {

    @Autowired
    private JdbcBarbershopRepository barbershopRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @DisplayName("GET: " + BARBERSHOPS_URL +
            " gives empty Barbershop List(array) when they're no added barbershops yet")
    @Test
    @Order(1)
    void shouldReturnEmptyBarbershopList() {

        ResponseEntity<BarbershopDto[]> response = restTemplate.getForEntity(BARBERSHOPS_URL, BarbershopDto[].class);

        assertEquals(0, Objects.requireNonNull(response.getBody()).length);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @DisplayName("POST: " + BARBERSHOPS_URL +
            " after saving barbershop entity returns its id and status code 201")
    @Test
    @Order(2)
    void shouldAddBarbershop() {
        ResponseEntity<Long> response = restTemplate.postForEntity(
                BARBERSHOPS_URL, BARBERSHOP_VALID_DTO, Long.class
        );

        assertEquals(BARBERSHOP_VALID_BARBERSHOP_ID, response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @DisplayName("GET: " + BARBERSHOPS_URL + "/{barbershopId}" +
            " returns corresponding barbershop dto")
    @Test
    @Order(3)
    void shouldReturnBarbershop() {
        ResponseEntity<BarbershopDto> response = restTemplate.getForEntity(
                BARBERSHOPS_URL + "/" + BARBERSHOP_VALID_BARBERSHOP_ID, BarbershopDto.class
        );
        BarbershopDto body = response.getBody();
        assertEquals(BARBERSHOP_VALID_BARBERSHOP_ID, Objects.requireNonNull(body).id());
        assertEquals(BARBERSHOP_VALID_DTO.address(), Objects.requireNonNull(body).address());
        assertEquals(BARBERSHOP_VALID_DTO.name(), Objects.requireNonNull(body).name());
        assertEquals(BARBERSHOP_VALID_DTO.phoneNumber(), Objects.requireNonNull(body).phoneNumber());
        assertEquals(BARBERSHOP_VALID_DTO.email(), Objects.requireNonNull(body).email());
        assertEquals(BARBERSHOP_VALID_DTO.isActive(), Objects.requireNonNull(body).isActive());
        assertEquals(BARBERSHOP_VALID_DTO.workTimeFrom(), Objects.requireNonNull(body).workTimeFrom());
        assertEquals(BARBERSHOP_VALID_DTO.workTimeTo(), Objects.requireNonNull(body).workTimeTo());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(BARBERSHOP_FIELD_AMOUNT, BarbershopDto.class.getDeclaredFields().length);
    }

    @Test
    @DisplayName("PUT: " + BARBERSHOPS_URL +
            " should return updated barbershop (dto)")
    @Order(4)
    void shouldReturnUpdatedBarbershop() {
        HttpEntity<BarbershopDto> entity = new HttpEntity<>(BARBERSHOP_VALID_UPDATED_DTO);
        ResponseEntity<BarbershopDto> response = restTemplate.exchange(BARBERSHOPS_URL, HttpMethod.PUT, entity, BarbershopDto.class);
        BarbershopDto body = response.getBody();

        assertEquals(BARBERSHOP_VALID_UPDATED_DTO.id(), Objects.requireNonNull(body).id());
        assertEquals(BARBERSHOP_VALID_UPDATED_DTO.address(), Objects.requireNonNull(body).address());
        assertEquals(BARBERSHOP_VALID_UPDATED_DTO.name(), Objects.requireNonNull(body).name());
        assertEquals(BARBERSHOP_VALID_UPDATED_DTO.phoneNumber(), Objects.requireNonNull(body).phoneNumber());
        assertEquals(BARBERSHOP_VALID_UPDATED_DTO.email(), Objects.requireNonNull(body).email());
        assertEquals(BARBERSHOP_VALID_UPDATED_DTO.isActive(), Objects.requireNonNull(body).isActive());
        assertEquals(BARBERSHOP_VALID_UPDATED_DTO.workTimeFrom(), Objects.requireNonNull(body).workTimeFrom());
        assertEquals(BARBERSHOP_VALID_UPDATED_DTO.workTimeTo(), Objects.requireNonNull(body).workTimeTo());
        assertEquals(BARBERSHOP_FIELD_AMOUNT, BarbershopDto.class.getDeclaredFields().length);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @DisplayName("GET: " + BARBERSHOPS_URL +
            " gives all barbershops at once")
    @Test
    @Order(5)
    void shouldReturnAllBarbershops() {
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY_LIST.get(1));
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY_LIST.get(2));

        ResponseEntity<BarbershopDto[]> response = restTemplate.getForEntity(BARBERSHOPS_URL, BarbershopDto[].class);
        List<BarbershopDto> body = List.of(Objects.requireNonNull(response.getBody()));


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(BARBERSHOP_VALID_DTO_LIST.size(), Objects.requireNonNull(body).size());

        assertTrue(body.contains(BARBERSHOP_VALID_UPDATED_DTO));
        assertTrue(body.contains(BARBERSHOP_VALID_DTO_LIST.get(1)));
        assertTrue(body.contains(BARBERSHOP_VALID_DTO_LIST.get(2)));
    }

    @AfterAll
    void cleanUpDb() {
        barbershopRepository.truncateAndRestartSequence();
    }
}
