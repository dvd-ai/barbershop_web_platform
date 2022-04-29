package com.app.barbershopweb.database.order.crud;

import com.app.barbershopweb.barbershop.crud.repository.BarbershopRepository;
import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.crud.repository.OrderRepository;
import com.app.barbershopweb.user.crud.repository.UserRepository;
import com.app.barbershopweb.workspace.repository.WorkspaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static com.app.barbershopweb.barbershop.crud.constants.BarbershopEntity__TestConstants.BARBERSHOP_VALID_ENTITY;
import static com.app.barbershopweb.order.crud.constants.OrderEntity__TestConstants.ORDER_VALID_ENTITY;
import static com.app.barbershopweb.order.crud.constants.OrderEntity__TestConstants.ORDER_VALID_UPDATED_ENTITY;
import static com.app.barbershopweb.order.crud.constants.OrderList__TestConstants.ORDER_VALID_ENTITY_LIST;
import static com.app.barbershopweb.order.crud.constants.OrderMetadata__TestConstants.*;
import static com.app.barbershopweb.user.crud.constants.UserEntity__TestConstants.USER_VALID_ENTITY;
import static com.app.barbershopweb.workspace.constants.WorkspaceEntity__TestConstants.WORKSPACE_VALID_ENTITY;
import static com.app.barbershopweb.workspace.constants.WorkspaceList__TestConstants.WORKSPACE_VALID_ENTITY_LIST;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("jdbc order repository test without error handling")
class OrderRepositoryTest extends AbstractIT {

    @Autowired
    WorkspaceRepository workspaceRepository;
    @Autowired
    BarbershopRepository barbershopRepository;
    @Autowired
    UserRepository usersRepository;
    @Autowired
    OrderRepository orderRepository;


    @BeforeEach
    void initFks() {
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        usersRepository.addUser(USER_VALID_ENTITY);
        usersRepository.addUser(USER_VALID_ENTITY);
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY);
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY_LIST.get(1));
    }

    @AfterEach
    void cleanUpDb() {
        barbershopRepository.truncateAndRestartSequence();
        usersRepository.truncateAndRestartSequence();
        workspaceRepository.truncateAndRestartSequence();
        orderRepository.truncateAndRestartSequence();
    }


    @Test
    void addOrder() {
        Long id = orderRepository.addOrder(ORDER_VALID_ENTITY);

        assertTrue(id > 0);
    }

    @Test
    void findOrder() {
        assertTrue(orderRepository.findOrder(ORDER_VALID_ORDER_ID).isEmpty());

        Long id = orderRepository.addOrder(ORDER_VALID_ENTITY);
        Optional<Order> foundOrderOpt = orderRepository.findOrder(id);

        assertTrue(foundOrderOpt.isPresent());
        assertEquals(ORDER_VALID_ENTITY, foundOrderOpt.get());
    }

    @Test
    void updateOrder() {
        usersRepository.addUser(USER_VALID_ENTITY);
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY_LIST.get(2));

        orderRepository.addOrder(ORDER_VALID_ENTITY);
        Optional<Order> updOrderOpt = orderRepository.updateOrder(
                ORDER_VALID_UPDATED_ENTITY
        );

        assertTrue(updOrderOpt.isPresent());
        assertEquals(ORDER_VALID_UPDATED_ENTITY, updOrderOpt.get());
    }

    @Test
    void getOrders() {
        assertTrue(orderRepository.getOrders().isEmpty());

        usersRepository.addUser(USER_VALID_ENTITY);
        usersRepository.addUser(USER_VALID_ENTITY);
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY_LIST.get(2));

        orderRepository.addOrder(ORDER_VALID_ENTITY_LIST.get(0));
        orderRepository.addOrder(ORDER_VALID_ENTITY_LIST.get(1));
        orderRepository.addOrder(ORDER_VALID_ENTITY_LIST.get(2));

        List<Order> orders = orderRepository.getOrders();

        assertEquals(ORDER_VALID_ENTITY_LIST.size(), orders.size());
        assertEquals(ORDER_VALID_ENTITY_LIST, orders);
    }

    @Test
    void deleteOrder() {
        Long id = orderRepository.addOrder(ORDER_VALID_ENTITY);
        orderRepository.deleteOrder(id);

        assertTrue(orderRepository.getOrders().isEmpty());
    }

    @Test
    void orderExistsByOrderId() {
        assertFalse(orderRepository.orderExistsByOrderId(ORDER_VALID_ORDER_ID));

        Long id = orderRepository.addOrder(ORDER_VALID_ENTITY);

        assertTrue(orderRepository.orderExistsByOrderId(id));
    }

    @Test
    void orderExistsByCustomerIdAndOrderDate() {
        assertFalse(
                orderRepository.orderExistsByCustomerIdAndOrderDate(
                        ORDER_VALID_CUSTOMER_ID, ORDER_VALID_ORDER_DATE
                )
        );

        orderRepository.addOrder(ORDER_VALID_ENTITY);

        assertTrue(
                orderRepository.orderExistsByCustomerIdAndOrderDate(
                        ORDER_VALID_CUSTOMER_ID, ORDER_VALID_ORDER_DATE
                )
        );
    }

    @Test
    void orderExistsByBarberIdAndOrderDate() {
        assertFalse(
                orderRepository.orderExistsByBarberIdAndOrderDate(
                        ORDER_VALID_BARBER_ID, ORDER_VALID_ORDER_DATE
                )
        );

        orderRepository.addOrder(ORDER_VALID_ENTITY);

        assertTrue(
                orderRepository.orderExistsByBarberIdAndOrderDate(
                        ORDER_VALID_BARBER_ID, ORDER_VALID_ORDER_DATE
                )
        );
    }

    @Test
    void truncateAndRestartSequence() {
        orderRepository.addOrder(ORDER_VALID_ENTITY);
        orderRepository.truncateAndRestartSequence();

        assertTrue(orderRepository.getOrders().isEmpty());

        Long id = orderRepository.addOrder(ORDER_VALID_ENTITY);
        assertEquals(1, id);
    }

    @Test
    void deactivateOrdersByBarbershopId() {
        orderRepository.addOrder(ORDER_VALID_ENTITY);

        orderRepository.deactivateOrdersByBarbershopId(ORDER_VALID_ENTITY.getBarbershopId());

        Order order = orderRepository.findOrder(ORDER_VALID_ENTITY.getOrderId()).get();

        assertFalse(order.getActive());
    }
}
