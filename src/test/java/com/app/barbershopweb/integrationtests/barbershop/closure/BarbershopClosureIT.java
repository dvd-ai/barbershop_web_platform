package com.app.barbershopweb.integrationtests.barbershop.closure;

import com.app.barbershopweb.barbershop.crud.repository.BarbershopRepository;
import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.mailservice.pojo.ExpectedMailMetadata;
import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.crud.repository.OrderRepository;
import com.app.barbershopweb.user.crud.repository.UserRepository;
import com.app.barbershopweb.workspace.repository.WorkspaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static com.app.barbershopweb.barbershop.crud.constants.BarbershopEntity__TestConstants.BARBERSHOP_VALID_ENTITY;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopMetadata__TestConstants.BARBERSHOPS_URL;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopMetadata__TestConstants.BARBERSHOP_VALID_BARBERSHOP_ID;
import static com.app.barbershopweb.user.crud.constants.UserEntity__TestConstants.USER_VALID_ENTITY;
import static com.app.barbershopweb.workspace.constants.WorkspaceEntity__TestConstants.WORKSPACE_VALID_ENTITY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class BarbershopClosureIT extends AbstractIT {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BarbershopRepository barbershopRepository;
    @Autowired
    WorkspaceRepository workspaceRepository;

    @Autowired
    TestRestTemplate testRestTemplate;

    @BeforeEach
    void init() {
        Order order = new Order(1L, 1L, 1L, 2L,
                LocalDateTime.of(2011, 10, 1, 10, 0),
                true
        );

        userRepository.addUser(USER_VALID_ENTITY);
        userRepository.addUser(USER_VALID_ENTITY);
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY);
        orderRepository.addOrder(order);
    }

    @AfterEach
    void cleanUp() {
        userRepository.truncateAndRestartSequence();
        barbershopRepository.truncateAndRestartSequence();
        workspaceRepository.truncateAndRestartSequence();
        orderRepository.truncateAndRestartSequence();
        mailHogContainer.deleteAllMailHogMessages(testRestTemplate);
    }

    @Test
    void deactivatesBarbershop() {
        ResponseEntity<Object> response =
                testRestTemplate.exchange(
                        BARBERSHOPS_URL + "/" + BARBERSHOP_VALID_BARBERSHOP_ID, HttpMethod.DELETE,
                        null, Object.class
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertFalse(barbershopRepository.getBarbershops().get(0).getActive());
        assertFalse(workspaceRepository.getWorkspaces().get(0).getActive());
        assertFalse(orderRepository.getOrders().get(0).getActive());

        mailHogContainer.checkMailInbox(
                testRestTemplate,
                new ExpectedMailMetadata(
                        "no-reply@gmail.com",
                        USER_VALID_ENTITY.getEmail(),
                        "Your appointments canceled because barbershop is out of business",
                        "Dear " + USER_VALID_ENTITY.getFirstName() +
                                " " + USER_VALID_ENTITY.getLastName() +
                                ", your ..."
                )
        );
    }
}
