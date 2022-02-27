package com.app.barbershopweb.integrationtests.barbershop;


import com.app.barbershopweb.barbershop.BarbershopConverter;
import com.app.barbershopweb.barbershop.BarbershopDto;
import com.app.barbershopweb.barbershop.BarbershopTestConstants;
import com.app.barbershopweb.barbershop.repository.JdbcBarbershopRepository;
import com.app.barbershopweb.integrationtests.AbstractIT;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Objects;

import static com.app.barbershopweb.barbershop.BarbershopTestConstants.*;
import static org.junit.jupiter.api.Assertions.*;



@DisplayName("Barbershop IT without error handling")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BarbershopIT extends AbstractIT {
    
    @Autowired
    private JdbcBarbershopRepository barbershopRepository;

    @Autowired
    private BarbershopConverter barbershopConverter;

    @Autowired
    private TestRestTemplate restTemplate;

    private final BarbershopTestConstants btc = new BarbershopTestConstants();
    
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
                BARBERSHOPS_URL, btc.VALID_BARBERSHOP_DTO, Long.class
        );

        assertEquals(btc.VALID_BARBERSHOP_ID, response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @DisplayName("GET: " + BARBERSHOPS_URL + "/{barbershopId}" +
            " returns corresponding barbershop dto")
    @Test
    @Order(3)
    void shouldReturnBarbershop() {
        ResponseEntity<BarbershopDto> response = restTemplate.getForEntity(
                BARBERSHOPS_URL + "/" + btc.VALID_BARBERSHOP_ID, BarbershopDto.class
        );
        BarbershopDto body = response.getBody();
        assertEquals(btc.VALID_BARBERSHOP_ID, Objects.requireNonNull(body).id());
        assertEquals(btc.VALID_BARBERSHOP_DTO.address(), Objects.requireNonNull(body).address());
        assertEquals(btc.VALID_BARBERSHOP_DTO.name(), Objects.requireNonNull(body).name());
        assertEquals(btc.VALID_BARBERSHOP_DTO.phoneNumber(), Objects.requireNonNull(body).phoneNumber());
        assertEquals(btc.VALID_BARBERSHOP_DTO.email(), Objects.requireNonNull(body).email());
        assertEquals(btc.VALID_BARBERSHOP_DTO.workTimeFrom(), Objects.requireNonNull(body).workTimeFrom());
        assertEquals(btc.VALID_BARBERSHOP_DTO.workTimeTo(), Objects.requireNonNull(body).workTimeTo());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(btc.BARBERSHOP_FIELD_AMOUNT, BarbershopDto.class.getDeclaredFields().length);
    }

    @Test
    @DisplayName("PUT: " + BARBERSHOPS_URL +
            " should return updated barbershop (dto)")
    @Order(4)
    void shouldReturnUpdatedBarbershop() {
        HttpEntity<BarbershopDto>entity = new HttpEntity<>(btc.VALID_UPDATED_BARBERSHOP_DTO);
        ResponseEntity<BarbershopDto> response = restTemplate.exchange(BARBERSHOPS_URL, HttpMethod.PUT, entity, BarbershopDto.class);
        BarbershopDto body = response.getBody();

        assertEquals(btc.VALID_UPDATED_BARBERSHOP_DTO.id(), Objects.requireNonNull(body).id());
        assertEquals(btc.VALID_UPDATED_BARBERSHOP_DTO.address(), Objects.requireNonNull(body).address());
        assertEquals(btc.VALID_UPDATED_BARBERSHOP_DTO.name(), Objects.requireNonNull(body).name());
        assertEquals(btc.VALID_UPDATED_BARBERSHOP_DTO.phoneNumber(), Objects.requireNonNull(body).phoneNumber());
        assertEquals(btc.VALID_UPDATED_BARBERSHOP_DTO.email(), Objects.requireNonNull(body).email());
        assertEquals(btc.VALID_UPDATED_BARBERSHOP_DTO.workTimeFrom(), Objects.requireNonNull(body).workTimeFrom());
        assertEquals(btc.VALID_UPDATED_BARBERSHOP_DTO.workTimeTo(), Objects.requireNonNull(body).workTimeTo());
        assertEquals(btc.BARBERSHOP_FIELD_AMOUNT, BarbershopDto.class.getDeclaredFields().length);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @DisplayName("GET: " + BARBERSHOPS_URL +
            " gives all barbershops at once")
    @Test
    @Order(5)
    void shouldReturnAllBarbershops() {
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY_LIST.get(1));
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY_LIST.get(2));

        ResponseEntity<BarbershopDto[]> response = restTemplate.getForEntity(BARBERSHOPS_URL, BarbershopDto[].class);
        BarbershopDto[] body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(btc.VALID_BARBERSHOP_DTO_LIST.size(), Objects.requireNonNull(body).length);
        assertEquals(btc.VALID_UPDATED_BARBERSHOP_DTO, (Objects.requireNonNull(body)[0]));
        assertEquals(btc.VALID_BARBERSHOP_DTO_LIST.get(1), (Objects.requireNonNull(body)[1]));
        assertEquals(btc.VALID_BARBERSHOP_DTO_LIST.get(2), (Objects.requireNonNull(body)[2]));

    }

    @DisplayName("DELETE: " + BARBERSHOPS_URL + "{barbershopId}" +
            " returns empty body, status code 200, " +
            "when: barbershop with existing / not existing id was deleted")
    @Test
    @Order(6)
    void shouldDeleteBarbershopById() {
        ResponseEntity<Object> response = restTemplate.exchange(
                BARBERSHOPS_URL + "/" + btc.VALID_BARBERSHOP_ID,
                HttpMethod.DELETE, null, Object.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        assertTrue(barbershopRepository.findBarbershopById(btc.VALID_BARBERSHOP_ID).isEmpty());
    }


}
