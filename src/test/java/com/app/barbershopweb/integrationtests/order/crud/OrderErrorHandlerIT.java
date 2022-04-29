package com.app.barbershopweb.integrationtests.order.crud;

import com.app.barbershopweb.barbershop.crud.repository.BarbershopRepository;
import com.app.barbershopweb.error.ErrorDto;
import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.order.crud.OrderDto;
import com.app.barbershopweb.order.crud.repository.OrderRepository;
import com.app.barbershopweb.user.crud.repository.UserRepository;
import com.app.barbershopweb.workspace.repository.WorkspaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static com.app.barbershopweb.barbershop.crud.constants.BarbershopEntity__TestConstants.BARBERSHOP_VALID_ENTITY;
import static com.app.barbershopweb.order.crud.constants.OrderDto__TestConstants.*;
import static com.app.barbershopweb.order.crud.constants.OrderEntity__TestConstants.ORDER_VALID_ENTITY;
import static com.app.barbershopweb.order.crud.constants.OrderMetadata__TestConstants.ORDERS_URL;
import static com.app.barbershopweb.order.crud.constants.error.OrderErrorMessage_Business_TestConstants.*;
import static com.app.barbershopweb.order.crud.constants.error.OrderErrorMessage_Dto__TestConstants.*;
import static com.app.barbershopweb.order.crud.constants.error.OrderErrorMessage_Fk__TestConstants.ORDER_ERR_FK_BARBER_ID;
import static com.app.barbershopweb.order.crud.constants.error.OrderErrorMessage_Fk__TestConstants.ORDER_ERR_FK_CUSTOMER_ID;
import static com.app.barbershopweb.order.crud.constants.error.OrderErrorMessage_Uk__TestConstants.ORDER_ERR_UK_BARBER_ID__ORDER_DATE;
import static com.app.barbershopweb.order.crud.constants.error.OrderErrorMessage_Uk__TestConstants.ORDER_ERR_UK_CUSTOMER_ID__ORDER_DATE;
import static com.app.barbershopweb.user.crud.constants.UserEntity__TestConstants.USER_VALID_ENTITY;
import static com.app.barbershopweb.workspace.constants.WorkspaceEntity__TestConstants.WORKSPACE_VALID_ENTITY;
import static com.app.barbershopweb.workspace.constants.WorkspaceList__TestConstants.WORKSPACE_VALID_ENTITY_LIST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("error handling order integration test ")
class OrderErrorHandlerIT extends AbstractIT {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BarbershopRepository barbershopRepository;
    @Autowired
    WorkspaceRepository workspaceRepository;

    @Autowired
    TestRestTemplate restTemplate;


    @AfterEach
    void cleanUpDb() {
        userRepository.truncateAndRestartSequence();
        barbershopRepository.truncateAndRestartSequence();
        workspaceRepository.truncateAndRestartSequence();
        orderRepository.truncateAndRestartSequence();

    }

    @Test
    @DisplayName("POST: " + ORDERS_URL +
            " with invalid dto entity returns 400 and error message")
    void addOrderWhenInvalidDto() {
        ResponseEntity<ErrorDto> response = restTemplate.
                postForEntity(ORDERS_URL, ORDER_INVALID_DTO, ErrorDto.class);

        List<String> errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(4, errors.size());
        assertTrue(errors.containsAll(
                List.of(
                        ORDER_ERR_INVALID_DTO_BARBER_ID,
                        ORDER_ERR_INVALID_DTO_CUSTOMER_ID,
                        ORDER_ERR_INVALID_DTO_ORDER_ID,
                        ORDER_ERR_INVALID_DTO_BARBERSHOP_ID
                )
        ));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("POST: " + ORDERS_URL +
            " when order dto violates db fk constraints " +
            "returns status code 404 & error dto")
    void addOrderFkCv() {
        ResponseEntity<ErrorDto> response = restTemplate.
                postForEntity(ORDERS_URL, ORDER_VALID_DTO, ErrorDto.class);

        List<String> errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(2, errors.size());
        assertTrue(errors.containsAll(
                List.of(
                        ORDER_ERR_FK_BARBER_ID,
                        ORDER_ERR_FK_CUSTOMER_ID
                )
        ));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("POST: " + ORDERS_URL +
            " when order dto violates db uk constraints " +
            "returns status code 400 & error dto")
    void addOrderUkCv() {
        userRepository.addUser(USER_VALID_ENTITY);
        userRepository.addUser(USER_VALID_ENTITY);
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY);
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY_LIST.get(1));
        orderRepository.addOrder(ORDER_VALID_ENTITY);


        ResponseEntity<ErrorDto> response = restTemplate.
                postForEntity(ORDERS_URL, ORDER_VALID_DTO, ErrorDto.class);

        List<String> errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(2, errors.size());
        assertTrue(errors.containsAll(
                List.of(
                        ORDER_ERR_UK_BARBER_ID__ORDER_DATE,
                        ORDER_ERR_UK_CUSTOMER_ID__ORDER_DATE
                )
        ));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("POST: " + ORDERS_URL +
            " when order dto violates business data format " +
            "returns status code 400 & error dto")
    void addOrderBdfCv() {
        userRepository.addUser(USER_VALID_ENTITY);
        userRepository.addUser(USER_VALID_ENTITY);
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY);
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY_LIST.get(1));


        ResponseEntity<ErrorDto> response = restTemplate.
                postForEntity(ORDERS_URL, ORDER_INVALID_BUSINESS_DTO, ErrorDto.class);

        List<String> errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(3, errors.size());
        assertTrue(errors.containsAll(
                List.of(
                        ORDER_ERR_BUSINESS_TIME_FORMAT,
                        ORDER_ERR_BUSINESS_CUSTOMER_ID_BARBER_ID,
                        ORDER_ERR_BUSINESS_BARBERSHOP_HOURS
                )
        ));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("PUT: " + ORDERS_URL +
            " with invalid dto entity returns 400 and error message")
    void updateOrderWhenInvalidDto() {

        HttpEntity<OrderDto> entity = new HttpEntity<>(ORDER_INVALID_DTO);
        ResponseEntity<ErrorDto> response = restTemplate.
                exchange(ORDERS_URL, HttpMethod.PUT, entity, ErrorDto.class);

        List<String> errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(4, errors.size());
        assertTrue(errors.containsAll(
                List.of(
                        ORDER_ERR_INVALID_DTO_BARBER_ID,
                        ORDER_ERR_INVALID_DTO_CUSTOMER_ID,
                        ORDER_ERR_INVALID_DTO_ORDER_ID,
                        ORDER_ERR_INVALID_DTO_BARBERSHOP_ID
                )
        ));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("PUT: " + ORDERS_URL +
            " when order dto violates db fk constraints " +
            "returns status code 404 & error dto")
    void updateOrderFkCv() {
        HttpEntity<OrderDto> entity = new HttpEntity<>(ORDER_VALID_DTO);
        ResponseEntity<ErrorDto> response = restTemplate.
                exchange(ORDERS_URL, HttpMethod.PUT, entity, ErrorDto.class);

        List<String> errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(2, errors.size());
        assertTrue(errors.containsAll(
                List.of(
                        ORDER_ERR_FK_BARBER_ID,
                        ORDER_ERR_FK_CUSTOMER_ID
                )
        ));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("PUT: " + ORDERS_URL +
            " when order dto violates db uk constraints " +
            "returns status code 400 & error dto")
    void updateOrderUkCv() {
        userRepository.addUser(USER_VALID_ENTITY);
        userRepository.addUser(USER_VALID_ENTITY);
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY);
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY_LIST.get(1));

        orderRepository.addOrder(ORDER_VALID_ENTITY);

        HttpEntity<OrderDto> entity = new HttpEntity<>(ORDER_VALID_DTO);
        ResponseEntity<ErrorDto> response = restTemplate.
                exchange(ORDERS_URL, HttpMethod.PUT, entity, ErrorDto.class);

        List<String> errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(2, errors.size());
        assertTrue(errors.containsAll(
                List.of(
                        ORDER_ERR_UK_CUSTOMER_ID__ORDER_DATE,
                        ORDER_ERR_UK_BARBER_ID__ORDER_DATE
                )
        ));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("PUT: " + ORDERS_URL +
            " when order dto violates business data format " +
            "returns status code 400 & error dto")
    void updateOrderBdfCv() {
        userRepository.addUser(USER_VALID_ENTITY);
        userRepository.addUser(USER_VALID_ENTITY);
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY);
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY_LIST.get(1));


        HttpEntity<OrderDto> entity = new HttpEntity<>(ORDER_INVALID_BUSINESS_DTO);
        ResponseEntity<ErrorDto> response = restTemplate.
                exchange(ORDERS_URL, HttpMethod.PUT, entity, ErrorDto.class);

        List<String> errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(3, errors.size());
        assertTrue(errors.containsAll(
                List.of(
                        ORDER_ERR_BUSINESS_TIME_FORMAT,
                        ORDER_ERR_BUSINESS_CUSTOMER_ID_BARBER_ID,
                        ORDER_ERR_BUSINESS_BARBERSHOP_HOURS
                )
        ));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
