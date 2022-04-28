package com.app.barbershopweb.integrationtests.order.crud;

import com.app.barbershopweb.barbershop.crud.repository.BarbershopRepository;
import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.order.crud.OrderDto;
import com.app.barbershopweb.order.crud.repository.OrderRepository;
import com.app.barbershopweb.user.crud.repository.UserRepository;
import com.app.barbershopweb.workspace.repository.WorkspaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import static com.app.barbershopweb.order.crud.constants.OrderDto__TestConstants.ORDER_VALID_DTO;
import static com.app.barbershopweb.order.crud.constants.OrderDto__TestConstants.ORDER_VALID_UPDATED_DTO;
import static com.app.barbershopweb.order.crud.constants.OrderEntity__TestConstants.ORDER_VALID_ENTITY;
import static com.app.barbershopweb.order.crud.constants.OrderList__TestConstants.ORDER_VALID_DTO_LIST;
import static com.app.barbershopweb.order.crud.constants.OrderList__TestConstants.ORDER_VALID_ENTITY_LIST;
import static com.app.barbershopweb.order.crud.constants.OrderMetadata__TestConstants.*;
import static com.app.barbershopweb.user.crud.constants.UserEntity__TestConstants.USERS_VALID_ENTITY;
import static com.app.barbershopweb.workspace.constants.WorkspaceEntity__TestConstants.WORKSPACE_VALID_ENTITY;
import static com.app.barbershopweb.workspace.constants.WorkspaceList__TestConstants.WORKSPACE_VALID_ENTITY_LIST;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("order integration test without error handling")
class OrderIT extends AbstractIT {
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


    @BeforeEach
    void init() {
        userRepository.addUser(USERS_VALID_ENTITY);
        userRepository.addUser(USERS_VALID_ENTITY);

        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);

        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY);
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY_LIST.get(1));

    }

    @AfterEach
    void cleanUpDb() {
        userRepository.truncateAndRestartSequence();
        barbershopRepository.truncateAndRestartSequence();
        workspaceRepository.truncateAndRestartSequence();
        orderRepository.truncateAndRestartSequence();

    }

    @Test
    @DisplayName("POST: " + ORDERS_URL +
            " after saving order entity returns its id and status code 201")
    void shouldAddOrder() {
        ResponseEntity<Long> response = restTemplate.
                postForEntity(ORDERS_URL, ORDER_VALID_DTO, Long.class);

        assertTrue(Objects.requireNonNull(response.getBody()) > 0);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("GET: " + ORDERS_URL +
            " gives empty Order List(array) when they're no added orders yet")
    void shouldReturnEmptyOrderList() {

        ResponseEntity<OrderDto[]> response = restTemplate.getForEntity(ORDERS_URL, OrderDto[].class);
        assertEquals(0, Objects.requireNonNull(response.getBody()).length);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("GET: " + ORDERS_URL +
            " gives Order List(array) when they were added")
    void shouldReturnOrderList() {
        userRepository.addUser(USERS_VALID_ENTITY);
        userRepository.addUser(USERS_VALID_ENTITY);
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY_LIST.get(2));

        orderRepository.addOrder(ORDER_VALID_ENTITY_LIST.get(0));
        orderRepository.addOrder(ORDER_VALID_ENTITY_LIST.get(1));
        orderRepository.addOrder(ORDER_VALID_ENTITY_LIST.get(2));

        ResponseEntity<OrderDto[]> response = restTemplate.getForEntity(ORDERS_URL, OrderDto[].class);
        List<OrderDto> orders = List.of(Objects.requireNonNull(response.getBody()));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ORDER_VALID_ENTITY_LIST.size(), orders.size());
        assertEquals(ORDER_VALID_DTO_LIST, orders);
    }

    @Test
    @DisplayName("PUT: " + ORDERS_URL +
            " should return updated order (dto)")
    void shouldUpdateOrder() {
        userRepository.addUser(USERS_VALID_ENTITY);
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY_LIST.get(2));
        orderRepository.addOrder(ORDER_VALID_ENTITY);

        HttpEntity<OrderDto> entity = new HttpEntity<>(ORDER_VALID_UPDATED_DTO);
        ResponseEntity<OrderDto> response = restTemplate
                .exchange(ORDERS_URL, HttpMethod.PUT, entity, OrderDto.class);

        OrderDto body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ORDER_VALID_UPDATED_DTO, body);
        assertEquals(ORDER_FIELD_AMOUNT, OrderDto.class.getDeclaredFields().length);
    }

    @Test
    @DisplayName("GET: " + ORDERS_URL + "/{orderId}" +
            " returns corresponding order dto")
    void shouldGetOrderByOrderId() {
        orderRepository.addOrder(ORDER_VALID_ENTITY);

        ResponseEntity<OrderDto> response = restTemplate.getForEntity(
                ORDERS_URL + "/" + ORDER_VALID_ORDER_ID,
                OrderDto.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ORDER_FIELD_AMOUNT, OrderDto.class.getDeclaredFields().length);
        assertEquals(ORDER_VALID_DTO, Objects.requireNonNull(response.getBody()));
    }

    @Test
    @DisplayName("DELETE: " + ORDERS_URL + "{orderId}" +
            " deletes order entity, " +
            "returns empty body, status code 200 "
    )
    void shouldDeleteOrderByOrderId() {
        orderRepository.addOrder(ORDER_VALID_ENTITY);

        ResponseEntity<Object> response = restTemplate.exchange(
                ORDERS_URL + "/" + ORDER_VALID_ORDER_ID,
                HttpMethod.DELETE, null, Object.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        assertFalse(orderRepository.orderExistsByOrderId(ORDER_VALID_ORDER_ID));
    }

}
