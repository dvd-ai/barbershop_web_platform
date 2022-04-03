package com.app.barbershopweb.integrationtests.order.reservation;

import com.app.barbershopweb.barbershop.repository.BarbershopRepository;
import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.crud.OrderConverter;
import com.app.barbershopweb.order.crud.OrderDto;
import com.app.barbershopweb.order.crud.repository.OrderRepository;
import com.app.barbershopweb.order.reservation.OrderReservationTestConstants;
import com.app.barbershopweb.order.reservation.dto.OrderReservationDto;
import com.app.barbershopweb.user.repository.UserRepository;
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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.app.barbershopweb.order.reservation.OrderReservationTestConstants.ORDER_RESERV_FILTER_URL;
import static com.app.barbershopweb.order.reservation.OrderReservationTestConstants.ORDER_RESERV_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("order reservation integration test without error handling")
class OrderReservationIT extends AbstractIT {

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

    @Autowired
    OrderConverter orderConverter;

    OrderReservationTestConstants ortc = new OrderReservationTestConstants();


    @BeforeEach
    void init() {
        ortc.FK_USER_ENTITY_LIST.forEach(userRepository::addUser);
        ortc.FK_BARBERSHOP_ENTITY_LIST.forEach(barbershopRepository::addBarbershop);
        ortc.FK_WORKSPACE_ENTITY_LIST.forEach(workspaceRepository::addWorkspace);
    }

    @AfterEach
    void cleanUpDb() {
        userRepository.truncateAndRestartSequence();
        barbershopRepository.truncateAndRestartSequence();
        workspaceRepository.truncateAndRestartSequence();
        orderRepository.truncateAndRestartSequence();
    }

    @Test
    @DisplayName(
            "TESTING POST: " +  ORDER_RESERV_URL +
            " should return list of available orders, status code 200"
    )
    void getBarbershopActiveUnreservedOrdersForWeek() {
        ortc.UNRESERVED_ORDER_ENTITY_LIST.forEach(orderRepository::addOrder);

        ResponseEntity<OrderDto[]> response = restTemplate.postForEntity(
                ORDER_RESERV_URL, ortc.SUOR_DTO_NO_FILTERS,
                OrderDto[].class
        );
        List<OrderDto> body = Arrays.asList(Objects.requireNonNull(response.getBody()));

        //database returns '0' instead of null, but it was saved as null before
        assertTrue(body.stream().allMatch(orderDto -> orderDto.customerId() == 0));

        List<Order> orders = orderConverter.orderDtoListToEntityList(body);
        orders.forEach(o -> o.setCustomerId(null));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ortc.UNRESERVED_ORDER_DTO_LIST.size(), orders.size());
        assertTrue(
                ortc.UNRESERVED_ORDER_DTO_LIST.containsAll(
                    orderConverter.orderEntityListToDtoList(orders)
                )
        );
    }

    @Test
    @DisplayName(
            "TESTING POST: " +  ORDER_RESERV_URL +
                    " should return empty list of available orders, " +
                    "when they doesn't meet sql conditions;  status code 200"
    )
    void getBarbershopActiveUnreservedOrdersForWeekNotSuitableOrders() {
        ortc.NOT_SUITABLE_UNRESERVED_ORDER_ENTITY_LIST.forEach(orderRepository::addOrder);

        ResponseEntity<OrderDto[]> response = restTemplate.postForEntity(
                ORDER_RESERV_URL, ortc.SUOR_DTO_NO_FILTERS,
                OrderDto[].class
        );
        List<OrderDto> body = Arrays.asList(Objects.requireNonNull(response.getBody()));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(body.isEmpty());
    }

    @Test
    @DisplayName(
            "TESTING POST: " +  ORDER_RESERV_FILTER_URL +
                    " should return list of available filtered orders, status code 200"
    )
    void getBarbershopActiveFilteredUnreservedOrdersForWeek() {
        ortc.UNRESERVED_FILTERED_ORDER_ENTITY_LIST.forEach(orderRepository::addOrder);

        ResponseEntity<OrderDto[]> response = restTemplate.postForEntity(
                ORDER_RESERV_FILTER_URL, ortc.SUOR_DTO_WITH_FILTERS,
                OrderDto[].class
        );
        List<OrderDto> body = Arrays.asList(Objects.requireNonNull(response.getBody()));

        //database returns '0' instead of null, but it was saved as null before
        assertTrue(body.stream().allMatch(orderDto -> orderDto.customerId() == 0));

        List<Order> orders = orderConverter.orderDtoListToEntityList(body);
        orders.forEach(o -> o.setCustomerId(null));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ortc.UNRESERVED_FILTERED_ORDER_DTO_LIST.size(), orders.size());
        assertTrue(
                ortc.UNRESERVED_FILTERED_ORDER_DTO_LIST.containsAll(
                        orderConverter.orderEntityListToDtoList(orders)
                )
        );
    }

    @Test
    @DisplayName(
            "TESTING POST: " +  ORDER_RESERV_FILTER_URL +
                    " should return empty list of filtered available orders, " +
                    "when they doesn't meet sql conditions;  status code 200"
    )
    void getBarbershopActiveFilteredUnreservedOrdersForWeekNotSuitableOrders() {
        ortc.NOT_SUITABLE_UNRESERVED_ORDER_ENTITY_LIST.forEach(orderRepository::addOrder);

        ResponseEntity<OrderDto[]> response = restTemplate.postForEntity(
                ORDER_RESERV_URL, ortc.SUOR_DTO_WITH_FILTERS,
                OrderDto[].class
        );
        List<OrderDto> body = Arrays.asList(Objects.requireNonNull(response.getBody()));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(body.isEmpty());
    }

    @Test
    @DisplayName(
            "TESTING POST: " +  ORDER_RESERV_FILTER_URL +
                    " should return empty list of available orders, " +
                    "when filters weren't specified " +
                    " status code 200"
    )
    void getBarbershopActiveFilteredUnreservedOrdersForWeekNoFilters() {
        ortc.UNRESERVED_ORDER_ENTITY_LIST.forEach(orderRepository::addOrder);

        ResponseEntity<OrderDto[]> response = restTemplate.postForEntity(
                ORDER_RESERV_FILTER_URL, ortc.SUOR_DTO_NO_FILTERS,
                OrderDto[].class
        );
        List<OrderDto> body = Arrays.asList(Objects.requireNonNull(response.getBody()));

        //database returns '0' instead of null, but it was saved as null before
        assertTrue(body.stream().allMatch(orderDto -> orderDto.customerId() == 0));

        List<Order> orders = orderConverter.orderDtoListToEntityList(body);
        orders.forEach(o -> o.setCustomerId(null));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ortc.UNRESERVED_ORDER_DTO_LIST.size(), orders.size());
        assertTrue(
                ortc.UNRESERVED_ORDER_DTO_LIST.containsAll(
                        orderConverter.orderEntityListToDtoList(orders)
                )
        );
    }

    @Test
    @DisplayName(
            "TESTING PUT: " +  ORDER_RESERV_URL +
                    " reserves the orders with customerId" +
                    " and returns the orders, " +
                    " status code 200"
    )
    void reserveCustomerOrders() {
        ortc.UNRESERVED_ORDER_ENTITY_LIST.forEach(orderRepository::addOrder);
        HttpEntity<OrderReservationDto> entity = new HttpEntity<>(ortc.VALID_ORDER_RESERV_DTO);

        ResponseEntity<OrderDto[]> response = restTemplate.exchange(ORDER_RESERV_URL, HttpMethod.PUT, entity, OrderDto[].class);
        List<OrderDto> body = Arrays.asList(Objects.requireNonNull(response.getBody()));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ortc.RESERVED_ORDER_DTO_LIST.size(), body.size());
        assertTrue(ortc.RESERVED_ORDER_DTO_LIST.containsAll(body));
    }
}
