package com.app.barbershopweb.database.order.crud;

import com.app.barbershopweb.barbershop.BarbershopTestConstants;
import com.app.barbershopweb.barbershop.repository.JdbcBarbershopRepository;
import com.app.barbershopweb.database.AbstractJdbcRepositoryTest;
import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.crud.OrderTestConstants;
import com.app.barbershopweb.order.crud.repository.JdbcOrderRepository;
import com.app.barbershopweb.user.UserTestConstants;
import com.app.barbershopweb.user.repository.JdbcUsersRepository;
import com.app.barbershopweb.workspace.WorkspaceTestConstants;
import com.app.barbershopweb.workspace.repository.JdbcWorkspaceRepository;
import org.junit.jupiter.api.*;

import javax.sql.DataSource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("jdbc order repository test without error handling")
class JdbcOrderRepositoryTest extends AbstractJdbcRepositoryTest {

    static JdbcWorkspaceRepository workspaceRepository;
    static JdbcBarbershopRepository barbershopRepository;
    static JdbcUsersRepository usersRepository;
    static JdbcOrderRepository orderRepository;

    WorkspaceTestConstants wtc = new WorkspaceTestConstants();
    UserTestConstants utc = new UserTestConstants();
    BarbershopTestConstants btc = new BarbershopTestConstants();
    OrderTestConstants otc = new OrderTestConstants();

    @BeforeAll
    static void init() {
        DataSource ds = getDataSource();
        barbershopRepository = new JdbcBarbershopRepository(ds);
        usersRepository = new JdbcUsersRepository(ds);
        workspaceRepository = new JdbcWorkspaceRepository(
                ds, usersRepository, barbershopRepository
        );
        orderRepository = new JdbcOrderRepository(
                ds, usersRepository,
                barbershopRepository, workspaceRepository
        );
    }

    @BeforeEach
    void initFks() {
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        usersRepository.addUser(utc.VALID_USER_ENTITY);
        usersRepository.addUser(utc.VALID_USER_ENTITY);
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY);
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY_LIST.get(1));
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
        Long id = orderRepository.addOrder(otc.VALID_ORDER_ENTITY);

        assertTrue(id > 0);
    }

    @Test
    void findOrderByOrderId() {
        assertTrue(orderRepository.findOrderByOrderId(otc.VALID_ORDER_ID).isEmpty());

        Long id = orderRepository.addOrder(otc.VALID_ORDER_ENTITY);
        Optional<Order> foundOrderOpt = orderRepository.findOrderByOrderId(id);

        assertTrue(foundOrderOpt.isPresent());
        assertEquals(otc.VALID_ORDER_ENTITY, foundOrderOpt.get());
    }

    @Test
    void updateOrder() {
        usersRepository.addUser(utc.VALID_USER_ENTITY);
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY_LIST.get(2));

        orderRepository.addOrder(otc.VALID_ORDER_ENTITY);
        Optional<Order> updOrderOpt = orderRepository.updateOrder(
                otc.VALID_UPDATED_ORDER_ENTITY
        );

        assertTrue(updOrderOpt.isPresent());
        assertEquals(otc.VALID_UPDATED_ORDER_ENTITY, updOrderOpt.get());
    }

    @Test
    void getOrders() {
        assertTrue(orderRepository.getOrders().isEmpty());

        usersRepository.addUser(utc.VALID_USER_ENTITY);
        usersRepository.addUser(utc.VALID_USER_ENTITY);
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY_LIST.get(2));

        orderRepository.addOrder(otc.VALID_ORDER_ENTITY_LIST.get(0));
        orderRepository.addOrder(otc.VALID_ORDER_ENTITY_LIST.get(1));
        orderRepository.addOrder(otc.VALID_ORDER_ENTITY_LIST.get(2));

        List<Order> orders = orderRepository.getOrders();

        assertEquals(otc.VALID_ORDER_ENTITY_LIST.size(), orders.size());
        assertEquals(otc.VALID_ORDER_ENTITY_LIST, orders);
    }

    @Test
    void deleteOrderByOrderId() {
        Long id = orderRepository.addOrder(otc.VALID_ORDER_ENTITY);
        orderRepository.deleteOrderByOrderId(id);

        assertTrue(orderRepository.getOrders().isEmpty());
    }

    @Test
    void orderExistsByOrderId() {
        assertFalse(orderRepository.orderExistsByOrderId(otc.VALID_ORDER_ID));

        Long id = orderRepository.addOrder(otc.VALID_ORDER_ENTITY);

        assertTrue(orderRepository.orderExistsByOrderId(id));
    }

    @Test
    void orderExistsByCustomerIdAndOrderDate() {
        assertFalse(
                orderRepository.orderExistsByCustomerIdAndOrderDate(
                        otc.VALID_CUSTOMER_ID, otc.VALID_ORDER_DATE
                )
        );

        orderRepository.addOrder(otc.VALID_ORDER_ENTITY);

        assertTrue(
                orderRepository.orderExistsByCustomerIdAndOrderDate(
                    otc.VALID_CUSTOMER_ID, otc.VALID_ORDER_DATE
                )
        );
    }

    @Test
    void orderExistsByBarberIdAndOrderDate() {
        assertFalse(
                orderRepository.orderExistsByBarberIdAndOrderDate(
                    otc.VALID_BARBER_ID, otc.VALID_ORDER_DATE
                )
        );

        orderRepository.addOrder(otc.VALID_ORDER_ENTITY);

        assertTrue(
                orderRepository.orderExistsByBarberIdAndOrderDate(
                        otc.VALID_BARBER_ID, otc.VALID_ORDER_DATE
                )
        );
    }

    @Test
    void truncateAndRestartSequence() {
        orderRepository.addOrder(otc.VALID_ORDER_ENTITY);
        orderRepository.truncateAndRestartSequence();

        assertTrue(orderRepository.getOrders().isEmpty());

        Long id = orderRepository.addOrder(otc.VALID_ORDER_ENTITY);
        assertEquals(1, id);
    }

}
