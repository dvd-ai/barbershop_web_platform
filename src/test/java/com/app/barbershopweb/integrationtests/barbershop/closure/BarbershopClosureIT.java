package com.app.barbershopweb.integrationtests.barbershop.closure;

import com.app.barbershopweb.barbershop.crud.repository.BarbershopRepository;
import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.crud.repository.OrderRepository;
import com.app.barbershopweb.user.crud.repository.UserRepository;
import com.app.barbershopweb.workspace.repository.WorkspaceRepository;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.app.barbershopweb.barbershop.crud.constants.BarbershopMetadata__TestConstants.BARBERSHOPS_URL;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopMetadata__TestConstants.BARBERSHOP_VALID_BARBERSHOP_ID;
import static com.app.barbershopweb.integrationtests.barbershop.closure.constants.BarbershopClosure_Fk__TestConstants.BARBERSHOP_CLOSURE_FK_ORDER;
import static com.app.barbershopweb.mailservice.constants.MailService_Metadata__TestConstants.MAIL_SERVICE_MESSAGES_URL;
import static com.app.barbershopweb.mailservice.constants.outofbusiness.MailService_Metadata_OutOfBusiness__TestConstants.*;
import static com.app.barbershopweb.order.reservation.constants.list.fk.OrderReservation_FkEntityList__TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        ORDER_RESERVATION_FK_USER_ENTITY_LIST.forEach(userRepository::addUser);
        ORDER_RESERVATION_FK_BARBERSHOP_ENTITY_LIST.forEach(barbershopRepository::addBarbershop);
        ORDER_RESERVATION_FK_WORKSPACE_ENTITY_LIST.forEach(workspaceRepository::addWorkspace);
    }

    @AfterEach
    void cleanUp() {
        userRepository.truncateAndRestartSequence();
        barbershopRepository.truncateAndRestartSequence();
        workspaceRepository.truncateAndRestartSequence();
        orderRepository.truncateAndRestartSequence();

        String url = mailHogContainer.getWebInterfaceUrl() + MAIL_SERVICE_MESSAGES_URL;
        testRestTemplate.delete(url, Object.class);
    }

    @Test
    void deactivatesBarbershop() {
        orderRepository.addOrder(BARBERSHOP_CLOSURE_FK_ORDER);

        ResponseEntity<Object> response =
                testRestTemplate.exchange(
                        BARBERSHOPS_URL + "/" + BARBERSHOP_VALID_BARBERSHOP_ID, HttpMethod.DELETE,
                        null, Object.class
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        checkActiveBarbershops();
        checkActiveWorkspaces();
        checkActiveOrders();
        checkMailInbox();
    }

    private void checkActiveBarbershops() {
        assertEquals(1,
                barbershopRepository.getBarbershops()
                        .stream().filter(e -> !e.IsActive())
                        .toList()
                        .size()
        );
    }

    private void checkActiveWorkspaces() {
        assertEquals(2,
                workspaceRepository.getWorkspaces()
                        .stream().filter(e -> !e.getActive())
                        .toList()
                        .size()
        );
    }

    private void checkActiveOrders() {
        orderRepository.getOrders()
                .stream()
                .map(Order::getActive)
                .forEach(Assertions::assertFalse);
    }

    private void checkMailInbox() {
        String url = mailHogContainer.getWebInterfaceUrl() + MAIL_SERVICE_MESSAGES_URL;
        String json = testRestTemplate.getForObject(url, String.class);

        DocumentContext context = JsonPath.parse(json);
        List<Object> object = context.read("$");

        assertEquals(1, object.size());
        assertEquals(MAIL_OUT_OF_BUSINESS_EMAIL_FROM, context.read("$.[0].Content.Headers.From[0]"));
        assertEquals(ORDER_RESERVATION_FK_USER_ENTITY_LIST.get(0).getEmail(), context.read("$.[0].Content.Headers.To[0]"));
        assertEquals(MAIL_OUT_OF_BUSINESS_SUBJECT, context.read("$.[0].Content.Headers.Subject[0]"));
        assertEquals(MAIL_OUT_OF_BUSINESS_EMAIL_TO, context.read("$.[0].Content.Body"));
    }
}
