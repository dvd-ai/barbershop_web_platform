package com.app.barbershopweb.database.barbershop.closure;

import com.app.barbershopweb.barbershop.closure.repository.JdbcBarbershopClosureRepository;
import com.app.barbershopweb.barbershop.crud.repository.JdbcBarbershopRepository;
import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.order.crud.repository.JdbcOrderRepository;
import com.app.barbershopweb.user.crud.Users;
import com.app.barbershopweb.user.crud.repository.JdbcUsersRepository;
import com.app.barbershopweb.workspace.repository.JdbcWorkspaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.app.barbershopweb.order.reservation.constants.list.entity.OrderReservation_List_OrderEntity__TestConstants.ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST;
import static com.app.barbershopweb.order.reservation.constants.list.fk.OrderReservation_FkEntityList__TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JdbcBarbershopClosureRepositoryTest extends AbstractIT {

    @Autowired
    JdbcBarbershopClosureRepository barbershopClosureRepository;
    @Autowired
    JdbcUsersRepository usersRepository;
    @Autowired
    JdbcBarbershopRepository barbershopRepository;
    @Autowired
    JdbcOrderRepository orderRepository;
    @Autowired
    JdbcWorkspaceRepository workspaceRepository;

    @BeforeEach
    void initFks() {
        ORDER_RESERVATION_FK_USER_ENTITY_LIST.forEach(usersRepository::addUser);
        ORDER_RESERVATION_FK_BARBERSHOP_ENTITY_LIST.forEach(barbershopRepository::addBarbershop);
        ORDER_RESERVATION_FK_WORKSPACE_ENTITY_LIST.forEach(workspaceRepository::addWorkspace);
        ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST.forEach(orderRepository::addOrder);
    }

    @AfterEach
    void cleanUpDb() {
        usersRepository.truncateAndRestartSequence();
        barbershopRepository.truncateAndRestartSequence();
        workspaceRepository.truncateAndRestartSequence();
        orderRepository.truncateAndRestartSequence();
    }

    @Test
    void getBarbershopVictimCustomers() {
        List<Users> customers = barbershopClosureRepository.getBarbershopVictimCustomers(
                ORDER_RESERVATION_FK_BARBERSHOP_ENTITY_LIST.get(0).getId()
        );

        assertEquals(1, customers.size());
        assertEquals(ORDER_RESERVATION_FK_USER_ENTITY_LIST.get(0).getFirstName(), customers.get(0).getFirstName());
        assertEquals(ORDER_RESERVATION_FK_USER_ENTITY_LIST.get(0).getLastName(), customers.get(0).getLastName());
        assertEquals(ORDER_RESERVATION_FK_USER_ENTITY_LIST.get(0).getEmail(), customers.get(0).getEmail());
    }
}