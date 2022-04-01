package com.app.barbershopweb.integrationtests.order.crud;

import com.app.barbershopweb.barbershop.BarbershopTestConstants;
import com.app.barbershopweb.barbershop.repository.BarbershopRepository;
import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.order.crud.OrderDto;
import com.app.barbershopweb.order.crud.OrderTestConstants;
import com.app.barbershopweb.order.crud.repository.OrderRepository;
import com.app.barbershopweb.user.UserTestConstants;
import com.app.barbershopweb.user.repository.UserRepository;
import com.app.barbershopweb.workspace.WorkspaceTestConstants;
import com.app.barbershopweb.workspace.repository.WorkspaceRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static com.app.barbershopweb.order.crud.OrderTestConstants.ORDERS_URL;
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

    OrderTestConstants otc = new OrderTestConstants();
    BarbershopTestConstants btc = new BarbershopTestConstants();
    UserTestConstants utc = new UserTestConstants();
    WorkspaceTestConstants wtc = new WorkspaceTestConstants();

    @BeforeEach
    void init() {
        userRepository.addUser(utc.VALID_USER_ENTITY);
        userRepository.addUser(utc.VALID_USER_ENTITY);

        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);

        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY);
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY_LIST.get(1));

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
                postForEntity(ORDERS_URL, otc.VALID_ORDER_DTO, Long.class);

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
        userRepository.addUser(utc.VALID_USER_ENTITY);
        userRepository.addUser(utc.VALID_USER_ENTITY);
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY_LIST.get(2));

        orderRepository.addOrder(otc.VALID_ORDER_ENTITY_LIST.get(0));
        orderRepository.addOrder(otc.VALID_ORDER_ENTITY_LIST.get(1));
        orderRepository.addOrder(otc.VALID_ORDER_ENTITY_LIST.get(2));

        ResponseEntity<OrderDto[]> response = restTemplate.getForEntity(ORDERS_URL, OrderDto[].class);
        List<OrderDto> orders = List.of(Objects.requireNonNull(response.getBody()));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(otc.VALID_ORDER_ENTITY_LIST.size(), orders.size());
        assertEquals(otc.VALID_ORDER_DTO_LIST, orders);
    }

    @Test
    @DisplayName("PUT: " + ORDERS_URL +
            " should return updated order (dto)")
    void shouldUpdateOrder() {
        userRepository.addUser(utc.VALID_USER_ENTITY);
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY_LIST.get(2));
        orderRepository.addOrder(otc.VALID_ORDER_ENTITY);

        HttpEntity<OrderDto> entity = new HttpEntity<>(otc.VALID_UPDATED_ORDER_DTO);
        ResponseEntity<OrderDto> response = restTemplate
                .exchange(ORDERS_URL, HttpMethod.PUT, entity, OrderDto.class);

        OrderDto body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(otc.VALID_UPDATED_ORDER_DTO, body);
        assertEquals(otc.ORDER_FIELD_AMOUNT, OrderDto.class.getDeclaredFields().length);
    }

    @Test
    @DisplayName("GET: " + ORDERS_URL + "/{orderId}" +
            " returns corresponding order dto")
    void shouldGetOrderByOrderId() {
        orderRepository.addOrder(otc.VALID_ORDER_ENTITY);

        ResponseEntity<OrderDto> response = restTemplate.getForEntity(
                ORDERS_URL + "/" + otc.VALID_ORDER_ID,
                OrderDto.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(otc.ORDER_FIELD_AMOUNT, OrderDto.class.getDeclaredFields().length);
        assertEquals(otc.VALID_ORDER_DTO, Objects.requireNonNull(response.getBody()));
    }

    @Test
    @DisplayName("DELETE: " + ORDERS_URL + "{orderId}" +
            " deletes order entity, " +
            "returns empty body, status code 200 "
            )
    void shouldDeleteOrderByOrderId() {
        orderRepository.addOrder(otc.VALID_ORDER_ENTITY);

        ResponseEntity<Object> response = restTemplate.exchange(
                ORDERS_URL + "/" + otc.VALID_ORDER_ID,
                HttpMethod.DELETE, null, Object.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        assertFalse(orderRepository.orderExistsByOrderId(otc.VALID_ORDER_ID));
    }

}
