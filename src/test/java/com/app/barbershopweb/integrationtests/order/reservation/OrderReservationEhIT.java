package com.app.barbershopweb.integrationtests.order.reservation;

import com.app.barbershopweb.barbershop.repository.BarbershopRepository;
import com.app.barbershopweb.error.ErrorDto;
import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.order.crud.OrderConverter;
import com.app.barbershopweb.order.crud.repository.OrderRepository;
import com.app.barbershopweb.order.reservation.OrderReservationTestConstants;
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

import static com.app.barbershopweb.order.reservation.OrderReservationTestConstants.ORDER_RESERV_FILTER_URL;
import static com.app.barbershopweb.order.reservation.OrderReservationTestConstants.ORDER_RESERV_URL;
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

    OrderReservationTestConstants ortc = new OrderReservationTestConstants();

    @AfterEach
    void cleanUpDb() {
        userRepository.truncateAndRestartSequence();
        barbershopRepository.truncateAndRestartSequence();
        workspaceRepository.truncateAndRestartSequence();
        orderRepository.truncateAndRestartSequence();
    }

    @Test
    @DisplayName(
            "TESTING POST: " + ORDER_RESERV_URL +
            " when invalid dto, returns" +
            " status code 400 and error dto"
    )
    void getBarbershopActiveUnreservedOrdersForWeekInvalidDto() {
        ResponseEntity<ErrorDto> response = restTemplate.postForEntity(
                ORDER_RESERV_URL, ortc.INVALID_SUOR_DTO_NO_FILTERS,
                ErrorDto.class
        );
        List<String> errors = Objects.requireNonNull(response.getBody()).errors();
        assertEquals(3, errors.size());
        assertTrue(errors.contains(ortc.DTO_CV_BARBERSHOP_ID_ERR_MSG));
        assertTrue(errors.contains(ortc.DTO_CV_ORDER_FILTERS_ERR_MSG));
        assertTrue(errors.contains(ortc.DTO_CV_START_WEEK_DATE_ERR_MSG));
    }

    @Test
    @DisplayName(
            "TESTING POST: " + ORDER_RESERV_FILTER_URL +
                    " when invalid dto, returns" +
                    " status code 400 and error dto"
    )
    void getBarbershopActiveFilteredUnreservedOrdersForWeekInvalidDto() {
        ResponseEntity<ErrorDto> response = restTemplate.postForEntity(
                ORDER_RESERV_FILTER_URL, ortc.INVALID_SUOR_DTO_NO_FILTERS,
                ErrorDto.class
        );
        List<String> errors = Objects.requireNonNull(response.getBody()).errors();
        assertEquals(3, errors.size());
        assertTrue(errors.contains(ortc.DTO_CV_BARBERSHOP_ID_ERR_MSG));
        assertTrue(errors.contains(ortc.DTO_CV_ORDER_FILTERS_ERR_MSG));
        assertTrue(errors.contains(ortc.DTO_CV_START_WEEK_DATE_ERR_MSG));
    }

    @Test
    @DisplayName(
            "TESTING PUT: " + ORDER_RESERV_URL +
                    " when invalid dto, returns" +
                    " status code 400 and error dto"
    )
    void reserveCustomerOrdersInvalidDto() {
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                ORDER_RESERV_URL, HttpMethod.PUT,
                new HttpEntity<>(ortc.INVALID_ORDER_RESERV_DTO),
                ErrorDto.class
        );
        List<String> errors = Objects.requireNonNull(response.getBody()).errors();
        assertEquals(2, errors.size());
        assertTrue(errors.contains(ortc.DTO_CV_CUSTOMER_ID_ERR_MSG));
        assertTrue(errors.contains(ortc.DTO_CV_ORDER_ID_LIST_ERR_MSG));
    }

    @Test
    @DisplayName(
            "TESTING PUT: " + ORDER_RESERV_URL +
            " when fk violation (customerId) in the dto, returns" +
            " status code 404 and error dto"
    )
    void reserveCustomerOrdersCustomerIdFkCv() {
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                ORDER_RESERV_URL, HttpMethod.PUT,
                new HttpEntity<>(ortc.VALID_ORDER_RESERV_DTO),
                ErrorDto.class
        );
        List<String> errors = Objects.requireNonNull(response.getBody()).errors();
        assertEquals(1, errors.size());
        assertTrue(errors.contains(ortc.FK_CV_CUSTOMER_ID_ERR_MSG));
    }

    @Test
    @DisplayName(
            "TESTING PUT: " + ORDER_RESERV_URL +
                    " when fk violation (orderIds) in the dto, returns" +
                    " status code 404 and error dto"
    )
    void reserveCustomerOrdersOrderIdsFkCv() {
        ortc.FK_USER_ENTITY_LIST.forEach(userRepository::addUser);

        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                ORDER_RESERV_URL, HttpMethod.PUT,
                new HttpEntity<>(ortc.VALID_ORDER_RESERV_DTO),
                ErrorDto.class
        );
        List<String> errors = Objects.requireNonNull(response.getBody()).errors();
        assertEquals(ortc.FK_CV_ORDER_ID_LIST_ERR_MSG.size(), errors.size());
        assertTrue(ortc.FK_CV_ORDER_ID_LIST_ERR_MSG.containsAll(errors));
    }

    @Test
    @DisplayName(
            "TESTING PUT: " + ORDER_RESERV_URL +
            " When uk violation (customerId, orderDate) in the dto, returns" +
            " status code 400 and error dto"
    )
    void reserveCustomerOrdersUkCv() {
        ortc.FK_USER_ENTITY_LIST.forEach(userRepository::addUser);
        ortc.FK_BARBERSHOP_ENTITY_LIST.forEach(barbershopRepository::addBarbershop);
        ortc.FK_WORKSPACE_ENTITY_LIST.forEach(workspaceRepository::addWorkspace);
        ortc.RESERVED_ORDER_ENTITY_LIST.forEach(orderRepository::addOrder);

        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                ORDER_RESERV_URL, HttpMethod.PUT,
                new HttpEntity<>(ortc.VALID_ORDER_RESERV_DTO),
                ErrorDto.class
        );
        List<String> errors = Objects.requireNonNull(response.getBody()).errors();
        assertEquals(1, errors.size());
        assertTrue(errors.contains(ortc.UK_CV_CUSTOMER_ID_ORDER_DATE_ERR_MSG));
    }
}
