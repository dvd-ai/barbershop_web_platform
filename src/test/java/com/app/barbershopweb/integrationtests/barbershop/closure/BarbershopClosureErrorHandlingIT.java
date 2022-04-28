package com.app.barbershopweb.integrationtests.barbershop.closure;

import com.app.barbershopweb.error.ErrorDto;
import com.app.barbershopweb.integrationtests.AbstractIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static com.app.barbershopweb.barbershop.closure.constants.BarbershopClosure_ErrorMessage__TestConstants.BARBERSHOP_CLOSURE_ERR_BARBERSHOP_NOT_FOUND;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopErrorMessage__TestConstants.BARBERSHOP_ERR_INVALID_PATH_VAR_BARBERSHOP_ID;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopMetadata__TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BarbershopClosureErrorHandlingIT extends AbstractIT {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    void deactivateBarbershop_invalidId() {
        ResponseEntity<ErrorDto> response =
                testRestTemplate.exchange(
                        BARBERSHOPS_URL + "/" + BARBERSHOP_INVALID_BARBERSHOP_ID, HttpMethod.DELETE,
                        null, ErrorDto.class
                );

        List<String> errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, errors.size());
        assertTrue(errors.contains(BARBERSHOP_ERR_INVALID_PATH_VAR_BARBERSHOP_ID));
    }

    @Test
    void deactivateBarbershop_barbershopNotExist() {
        ResponseEntity<ErrorDto> response =
                testRestTemplate.exchange(
                        BARBERSHOPS_URL + "/" + BARBERSHOP_VALID_BARBERSHOP_ID, HttpMethod.DELETE,
                        null, ErrorDto.class
                );

        List<String> errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(1, errors.size());
        assertTrue(errors.contains(BARBERSHOP_CLOSURE_ERR_BARBERSHOP_NOT_FOUND));
    }
}
