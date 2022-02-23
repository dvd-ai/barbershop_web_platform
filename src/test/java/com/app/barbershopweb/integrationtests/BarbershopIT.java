package com.app.barbershopweb.integrationtests;


import com.app.barbershopweb.barbershop.BarbershopConverter;
import com.app.barbershopweb.barbershop.BarbershopDto;
import com.app.barbershopweb.barbershop.BarbershopService;
import com.app.barbershopweb.barbershop.repository.JdbcBarbershopRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayName("Barbershop integration test")
class BarbershopIT extends AbstractIT{

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcBarbershopRepository barbershopRepository;

    @Autowired
    private BarbershopService barbershopService;

    @Autowired
    private BarbershopConverter barbershopConverter;

    private final String suffix = "/barbershops";
    private final String URL = "http://localhost:" + port + suffix;

    @Autowired
    private TestRestTemplate restTemplate;

    @DisplayName("POST: " + suffix +
            " when barbershop dto isn't valid " +
            "returns status code 400")
    @Test
    void whenBarbershopDtoNotValid() {
        BarbershopDto barbershopDto = new BarbershopDto(
                1L, "a1",
                "", "+38091",
                "1@gmail.com"
        );

        final ResponseEntity<?> result = restTemplate.postForEntity(URL, barbershopDto, Object.class);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
}
