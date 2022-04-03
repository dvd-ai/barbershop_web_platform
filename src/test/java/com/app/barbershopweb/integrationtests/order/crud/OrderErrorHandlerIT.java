package com.app.barbershopweb.integrationtests.order.crud;

import com.app.barbershopweb.barbershop.BarbershopTestConstants;
import com.app.barbershopweb.barbershop.repository.BarbershopRepository;
import com.app.barbershopweb.error.ErrorDto;
import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.order.crud.OrderDto;
import com.app.barbershopweb.order.crud.OrderTestConstants;
import com.app.barbershopweb.order.crud.repository.OrderRepository;
import com.app.barbershopweb.user.UserTestConstants;
import com.app.barbershopweb.user.repository.UserRepository;
import com.app.barbershopweb.workspace.WorkspaceTestConstants;
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

import static com.app.barbershopweb.order.crud.OrderTestConstants.ORDERS_URL;
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

    OrderTestConstants otc = new OrderTestConstants();
    BarbershopTestConstants btc = new BarbershopTestConstants();
    UserTestConstants utc = new UserTestConstants();
    WorkspaceTestConstants wtc = new WorkspaceTestConstants();

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
                postForEntity(ORDERS_URL, otc.INVALID_ORDER_DTO, ErrorDto.class);

        List<String> errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(4, errors.size());
        assertTrue(errors.containsAll(
                List.of(
                    otc.DTO_CV_BARBER_ID_ERR_MSG,
                    otc.DTO_CV_CUSTOMER_ID_ERR_MSG,
                    otc.DTO_CV_ORDER_ID_ERR_MSG,
                    otc.DTO_CV_BARBERSHOP_ID_ERR_MSG
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
                postForEntity(ORDERS_URL, otc.VALID_ORDER_DTO, ErrorDto.class);

        List<String>errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(2, errors.size());
        assertTrue(errors.containsAll(
                List.of(
                    otc.FK_CV_BARBER_ID_ERR_MSG,
                    otc.FK_CV_CUSTOMER_ID_ERR_MSG
                )
        ));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("POST: " + ORDERS_URL +
            " when order dto violates db uk constraints " +
            "returns status code 400 & error dto")
    void addOrderUkCv() {
        userRepository.addUser(utc.VALID_USER_ENTITY);
        userRepository.addUser(utc.VALID_USER_ENTITY);
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY);
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY_LIST.get(1));
        orderRepository.addOrder(otc.VALID_ORDER_ENTITY);


        ResponseEntity<ErrorDto> response = restTemplate.
                postForEntity(ORDERS_URL, otc.VALID_ORDER_DTO, ErrorDto.class);

        List<String>errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(2, errors.size());
        assertTrue(errors.containsAll(
                List.of(
                    otc.UK_CV_BARBER_ID_ORDER_DATE_ERR_MSG,
                    otc.UK_CV_CUSTOMER_ID_ORDER_DATE_ERR_MSG
                )
        ));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("POST: " + ORDERS_URL +
            " when order dto violates business data format " +
            "returns status code 400 & error dto")
    void addOrderBdfCv() {
        userRepository.addUser(utc.VALID_USER_ENTITY);
        userRepository.addUser(utc.VALID_USER_ENTITY);
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY);
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY_LIST.get(1));



        ResponseEntity<ErrorDto> response = restTemplate.
                postForEntity(ORDERS_URL, otc.INVALID_BDF_ORDER_DTO, ErrorDto.class);

        List<String>errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(3, errors.size());
        assertTrue(errors.containsAll(
                List.of(
                        otc.BDF_CV_TIME_FORMAT_ERR_MSG,
                        otc.BDF_CV_CUSTOMER_ID_BARBER_ID_EQ_ERR_MSG,
                        otc.BDF_CV_BARBERSHOP_HOURS_ERR_MSG
                )
        ));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("PUT: " + ORDERS_URL +
            " with invalid dto entity returns 400 and error message")
    void updateOrderWhenInvalidDto() {

        HttpEntity<OrderDto>entity = new HttpEntity<>(otc.INVALID_ORDER_DTO);
        ResponseEntity<ErrorDto> response = restTemplate.
                exchange(ORDERS_URL, HttpMethod.PUT, entity, ErrorDto.class);

        List<String> errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(4, errors.size());
        assertTrue(errors.containsAll(
                List.of(
                        otc.DTO_CV_BARBER_ID_ERR_MSG,
                        otc.DTO_CV_CUSTOMER_ID_ERR_MSG,
                        otc.DTO_CV_ORDER_ID_ERR_MSG,
                        otc.DTO_CV_BARBERSHOP_ID_ERR_MSG
                )
        ));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("PUT: " + ORDERS_URL +
            " when order dto violates db fk constraints " +
            "returns status code 404 & error dto")
    void updateOrderFkCv() {
        HttpEntity<OrderDto> entity = new HttpEntity<>(otc.VALID_ORDER_DTO);
        ResponseEntity<ErrorDto> response = restTemplate.
                exchange(ORDERS_URL, HttpMethod.PUT, entity, ErrorDto.class);

        List<String>errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(2, errors.size());
        assertTrue(errors.containsAll(
                List.of(
                        otc.FK_CV_BARBER_ID_ERR_MSG,
                        otc.FK_CV_CUSTOMER_ID_ERR_MSG
                )
        ));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("PUT: " + ORDERS_URL +
            " when order dto violates db uk constraints " +
            "returns status code 400 & error dto")
    void updateOrderUkCv() {
        userRepository.addUser(utc.VALID_USER_ENTITY);
        userRepository.addUser(utc.VALID_USER_ENTITY);
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY);
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY_LIST.get(1));

        orderRepository.addOrder(otc.VALID_ORDER_ENTITY);

        HttpEntity<OrderDto> entity = new HttpEntity<>(otc.VALID_ORDER_DTO);
        ResponseEntity<ErrorDto> response = restTemplate.
                exchange(ORDERS_URL, HttpMethod.PUT, entity, ErrorDto.class);

        List<String>errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(2, errors.size());
        assertTrue(errors.containsAll(
                List.of(
                        otc.UK_CV_CUSTOMER_ID_ORDER_DATE_ERR_MSG,
                        otc.UK_CV_BARBER_ID_ORDER_DATE_ERR_MSG
                )
        ));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("PUT: " + ORDERS_URL +
            " when order dto violates business data format " +
            "returns status code 400 & error dto")
    void updateOrderBdfCv() {
        userRepository.addUser(utc.VALID_USER_ENTITY);
        userRepository.addUser(utc.VALID_USER_ENTITY);
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY);
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY_LIST.get(1));


        HttpEntity<OrderDto> entity = new HttpEntity<>(otc.INVALID_BDF_ORDER_DTO);
        ResponseEntity<ErrorDto> response = restTemplate.
                exchange(ORDERS_URL, HttpMethod.PUT, entity, ErrorDto.class);

        List<String>errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(3, errors.size());
        assertTrue(errors.containsAll(
                List.of(
                        otc.BDF_CV_TIME_FORMAT_ERR_MSG,
                        otc.BDF_CV_CUSTOMER_ID_BARBER_ID_EQ_ERR_MSG,
                        otc.BDF_CV_BARBERSHOP_HOURS_ERR_MSG
                )
        ));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
