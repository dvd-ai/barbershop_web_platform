package com.app.barbershopweb.integrationtests.order.reservation;

import com.app.barbershopweb.barbershop.repository.BarbershopRepository;
import com.app.barbershopweb.error.ErrorDto;
import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.order.crud.OrderConverter;
import com.app.barbershopweb.order.crud.repository.OrderRepository;
import com.app.barbershopweb.user.repository.UserRepository;
import com.app.barbershopweb.workspace.repository.WorkspaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static com.app.barbershopweb.order.reservation.constants.OrderReservation_Metadata__TestConstants.ORDER_RESERVATION_FILTER_URL;
import static com.app.barbershopweb.order.reservation.constants.OrderReservation_Metadata__TestConstants.ORDER_RESERVATION_URL;
import static com.app.barbershopweb.order.reservation.constants.dto.OrderReservation_Dto__TestConstants.ORDER_RESERVATION_INVALID_DTO;
import static com.app.barbershopweb.order.reservation.constants.dto.OrderReservation_Dto__TestConstants.ORDER_RESERVATION_VALID_DTO;
import static com.app.barbershopweb.order.reservation.constants.dto.OrderReservation_GetOpenOrders_Dto__TestConstants.GET_OPEN_ORDERS__REQUEST_INVALID_DTO;
import static com.app.barbershopweb.order.reservation.constants.dto.OrderReservation_GetOpenOrders_Filtered_Dto__TestConstants.GET_OPEN_FILTERED_ORDERS__REQUEST_INVALID_DTO;
import static com.app.barbershopweb.order.reservation.constants.error.OrderReservation_ErrorMessage_Dto__TestConstants.*;
import static com.app.barbershopweb.order.reservation.constants.error.OrderReservation_ErrorMessage_Fk__TestConstants.ORDER_RESERVATION_ERR_FK_CUSTOMER_ID;
import static com.app.barbershopweb.order.reservation.constants.error.OrderReservation_ErrorMessage_Fk__TestConstants.ORDER_RESERVATION_ERR_FK_ORDER_ID_LIST;
import static com.app.barbershopweb.order.reservation.constants.error.OrderReservation_ErrorMessage_Uk__TestConstants.ORDER_RESERVATION_ERR_UK_CUSTOMER_ID__ORDER_DATE;
import static com.app.barbershopweb.order.reservation.constants.list.entity.OrderReservation_List_OrderEntity__TestConstants.ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST;
import static com.app.barbershopweb.order.reservation.constants.list.fk.OrderReservation_FkEntityList__TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("order reservation error handling it")
class OrderReservationEhIT extends AbstractIT {

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


    @AfterEach
    void cleanUpDb() {
        userRepository.truncateAndRestartSequence();
        barbershopRepository.truncateAndRestartSequence();
        workspaceRepository.truncateAndRestartSequence();
        orderRepository.truncateAndRestartSequence();
    }

    @Test
    @DisplayName(
            "TESTING POST: " + ORDER_RESERVATION_URL +
                    " when invalid dto, returns" +
                    " status code 400 and error dto"
    )
    void getBarbershopActiveUnreservedOrdersForWeekInvalidDto() {
        ResponseEntity<ErrorDto> response = restTemplate.postForEntity(
                ORDER_RESERVATION_URL, GET_OPEN_ORDERS__REQUEST_INVALID_DTO,
                ErrorDto.class
        );
        List<String> errors = Objects.requireNonNull(response.getBody()).errors();
        assertEquals(2, errors.size());
        assertTrue(errors.contains(GET_OPEN_ORDERS_ERR_INVALID_DTO_BARBERSHOP_ID));
        assertTrue(errors.contains(GET_OPEN_ORDERS_ERR_INVALID_DTO_START_WEEK_DATE));
    }

    @Test
    @DisplayName(
            "TESTING POST: " + ORDER_RESERVATION_FILTER_URL +
                    " when invalid dto, returns" +
                    " status code 400 and error dto"
    )
    void getBarbershopActiveFilteredUnreservedOrdersForWeekInvalidDto() {
        ResponseEntity<ErrorDto> response = restTemplate.postForEntity(
                ORDER_RESERVATION_FILTER_URL, GET_OPEN_FILTERED_ORDERS__REQUEST_INVALID_DTO,
                ErrorDto.class
        );
        List<String> errors = Objects.requireNonNull(response.getBody()).errors();
        assertEquals(3, errors.size());
        assertTrue(errors.contains(GET_OPEN_FILTERED_ORDERS_ERR_INVALID_DTO_BARBERSHOP_ID));
        assertTrue(errors.contains(GET_OPEN_FILTERED_ORDERS_ERR_INVALID_DTO_START_WEEK_DATE));
        assertTrue(errors.contains(GET_OPEN_FILTERED_ORDERS_ERR_INVALID_DTO_ORDER_FILTERS));
    }

    @Test
    @DisplayName(
            "TESTING PUT: " + ORDER_RESERVATION_URL +
                    " when invalid dto, returns" +
                    " status code 400 and error dto"
    )
    void reserveCustomerOrdersInvalidDto() {
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                ORDER_RESERVATION_URL, HttpMethod.PUT,
                new HttpEntity<>(ORDER_RESERVATION_INVALID_DTO),
                ErrorDto.class
        );
        List<String> errors = Objects.requireNonNull(response.getBody()).errors();
        assertEquals(2, errors.size());
        assertTrue(errors.contains(ORDER_RESERVATION_ERR_INVALID_DTO_CUSTOMER_ID));
        assertTrue(errors.contains(ORDER_RESERVATION_ERR_INVALID_DTO_ORDER_ID_LIST));
    }

    @Test
    @DisplayName(
            "TESTING PUT: " + ORDER_RESERVATION_URL +
                    " when fk violation (customerId) in the dto, returns" +
                    " status code 404 and error dto"
    )
    void reserveCustomerOrdersCustomerIdFkCv() {
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                ORDER_RESERVATION_URL, HttpMethod.PUT,
                new HttpEntity<>(ORDER_RESERVATION_VALID_DTO),
                ErrorDto.class
        );
        List<String> errors = Objects.requireNonNull(response.getBody()).errors();
        assertEquals(1, errors.size());
        assertTrue(errors.contains(ORDER_RESERVATION_ERR_FK_CUSTOMER_ID));
    }

    @Test
    @DisplayName(
            "TESTING PUT: " + ORDER_RESERVATION_URL +
                    " when fk violation (orderIds) in the dto, returns" +
                    " status code 404 and error dto"
    )
    void reserveCustomerOrdersOrderIdsFkCv() {
        ORDER_RESERVATION_FK_USER_ENTITY_LIST.forEach(userRepository::addUser);

        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                ORDER_RESERVATION_URL, HttpMethod.PUT,
                new HttpEntity<>(ORDER_RESERVATION_VALID_DTO),
                ErrorDto.class
        );
        List<String> errors = Objects.requireNonNull(response.getBody()).errors();
        assertEquals(ORDER_RESERVATION_ERR_FK_ORDER_ID_LIST.size(), errors.size());
        assertTrue(ORDER_RESERVATION_ERR_FK_ORDER_ID_LIST.containsAll(errors));
    }

    @Test
    @DisplayName(
            "TESTING PUT: " + ORDER_RESERVATION_URL +
                    " When uk violation (customerId, orderDate) in the dto, returns" +
                    " status code 400 and error dto"
    )
    void reserveCustomerOrdersUkCv() {
        ORDER_RESERVATION_FK_USER_ENTITY_LIST.forEach(userRepository::addUser);
        ORDER_RESERVATION_FK_BARBERSHOP_ENTITY_LIST.forEach(barbershopRepository::addBarbershop);
        ORDER_RESERVATION_FK_WORKSPACE_ENTITY_LIST.forEach(workspaceRepository::addWorkspace);
        ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST.forEach(orderRepository::addOrder);

        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                ORDER_RESERVATION_URL, HttpMethod.PUT,
                new HttpEntity<>(ORDER_RESERVATION_VALID_DTO),
                ErrorDto.class
        );
        List<String> errors = Objects.requireNonNull(response.getBody()).errors();
        assertEquals(1, errors.size());
        assertTrue(errors.contains(ORDER_RESERVATION_ERR_UK_CUSTOMER_ID__ORDER_DATE));
    }
}
