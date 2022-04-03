package com.app.barbershopweb.database.order.reservation;

import com.app.barbershopweb.barbershop.repository.JdbcBarbershopRepository;
import com.app.barbershopweb.database.AbstractJdbcRepositoryTest;
import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.crud.repository.JdbcOrderRepository;
import com.app.barbershopweb.order.reservation.OrderReservationTestConstants;
import com.app.barbershopweb.order.reservation.repository.JdbcOrderReservationRepository;
import com.app.barbershopweb.user.repository.JdbcUsersRepository;
import com.app.barbershopweb.workspace.repository.JdbcWorkspaceRepository;
import org.junit.jupiter.api.*;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DisplayName("jdbc order reservation repository test without error handling")
class JdbcOrderReservationRepoTest extends AbstractJdbcRepositoryTest {

    static JdbcOrderReservationRepository orderReservationRepository;
    static JdbcUsersRepository usersRepository;
    static JdbcBarbershopRepository barbershopRepository;
    static JdbcOrderRepository orderRepository;
    static JdbcWorkspaceRepository workspaceRepository;

    OrderReservationTestConstants ortc = new OrderReservationTestConstants();

    @BeforeAll
    static void init() {
        DataSource ds = getDataSource();
        usersRepository = new JdbcUsersRepository(ds);
        barbershopRepository = new JdbcBarbershopRepository(ds);
        workspaceRepository = new JdbcWorkspaceRepository(
                ds, usersRepository, barbershopRepository
        );
        orderRepository = new JdbcOrderRepository(
                ds, usersRepository, barbershopRepository, workspaceRepository
        );
        orderReservationRepository = new JdbcOrderReservationRepository(
                orderRepository, ds, usersRepository);
    }

    @BeforeEach
    void initFks() {
        ortc.FK_USER_ENTITY_LIST.forEach(usersRepository::addUser);
        ortc.FK_BARBERSHOP_ENTITY_LIST.forEach(barbershopRepository::addBarbershop);
        ortc.FK_WORKSPACE_ENTITY_LIST.forEach(workspaceRepository::addWorkspace);
    }

    @AfterEach
    void cleanUpDb() {
        usersRepository.truncateAndRestartSequence();
        barbershopRepository.truncateAndRestartSequence();
        workspaceRepository.truncateAndRestartSequence();
        orderRepository.truncateAndRestartSequence();
    }

    @Test
    void getAvailableOrders() {
        assertTrue(
                orderReservationRepository.getAvailableOrders(
                    ortc.SUOR_DTO_NO_FILTERS.barbershopId(),
                    ortc.SUOR_DTO_NO_FILTERS.startWeekDate()
                ).isEmpty()
        );

        ortc.UNRESERVED_ORDER_ENTITY_LIST.forEach(orderRepository::addOrder);

        List<Order> availableOrders = orderReservationRepository.getAvailableOrders(
                ortc.SUOR_DTO_NO_FILTERS.barbershopId(),
                ortc.SUOR_DTO_NO_FILTERS.startWeekDate()
        );

        //database returns '0' instead of null, but it was saved as null before
        assertTrue(availableOrders.stream().allMatch(order -> order.getCustomerId() == 0));
        availableOrders.forEach(order -> order.setCustomerId(null));

        assertEquals(ortc.UNRESERVED_ORDER_ENTITY_LIST.size(), availableOrders.size());
        assertTrue(ortc.UNRESERVED_ORDER_ENTITY_LIST.containsAll(availableOrders));
    }

    @Test
    @DisplayName("when there are no suitable orders (doesn't meet sql conditions), " +
            "but they EXIST  " +
            "- returns empty list")
    void getAvailableOrdersNotSuitableOrders() {
        assertTrue(
                orderReservationRepository.getAvailableOrders(
                        ortc.SUOR_DTO_NO_FILTERS.barbershopId(),
                        ortc.SUOR_DTO_NO_FILTERS.startWeekDate()
                ).isEmpty()
        );

        ortc.NOT_SUITABLE_UNRESERVED_ORDER_ENTITY_LIST.forEach(orderRepository::addOrder);

        List<Order> availableOrders = orderReservationRepository.getAvailableOrders(
                ortc.SUOR_DTO_NO_FILTERS.barbershopId(),
                ortc.SUOR_DTO_NO_FILTERS.startWeekDate()
        );

        assertTrue(availableOrders.isEmpty());
    }


    @Test
    void getAvailableFilteredOrders() {
        assertTrue(
                orderReservationRepository.getAvailableFilteredOrders(
                        ortc.SUOR_DTO_WITH_FILTERS.barbershopId(),
                        ortc.SUOR_DTO_WITH_FILTERS.startWeekDate(),
                        ortc.SUOR_DTO_WITH_FILTERS.orderFilters().getBarberIds()
                ).isEmpty()
        );

        ortc.UNRESERVED_FILTERED_ORDER_ENTITY_LIST.forEach(orderRepository::addOrder);

        List<Order> filteredOrders = orderReservationRepository.getAvailableFilteredOrders(
                ortc.SUOR_DTO_WITH_FILTERS.barbershopId(),
                ortc.SUOR_DTO_WITH_FILTERS.startWeekDate(),
                ortc.SUOR_DTO_WITH_FILTERS.orderFilters().getBarberIds()
        );


        //database returns '0' instead of null, but it was saved as null before
        assertTrue(filteredOrders.stream().allMatch(order -> order.getCustomerId() == 0));
        filteredOrders.forEach(order -> order.setCustomerId(null));

        assertEquals(ortc.UNRESERVED_FILTERED_ORDER_ENTITY_LIST.size(), filteredOrders.size());
        assertTrue(ortc.UNRESERVED_FILTERED_ORDER_ENTITY_LIST.containsAll(filteredOrders));

    }

    @Test
    @DisplayName("when there are no suitable orders (doesn't meet sql conditions + filter), " +
            "but they EXIST  " +
            "- returns empty list")
    void getFilteredAvailableOrdersNotSuitableOrders() {
        assertTrue(
                orderReservationRepository.getAvailableFilteredOrders(
                        ortc.SUOR_DTO_WITH_FILTERS.barbershopId(),
                        ortc.SUOR_DTO_WITH_FILTERS.startWeekDate(),
                        ortc.SUOR_DTO_WITH_FILTERS.orderFilters().getBarberIds()
                ).isEmpty()
        );

        ortc.NOT_SUITABLE_UNRESERVED_ORDER_ENTITY_LIST.forEach(orderRepository::addOrder);

        List<Order> availableOrders = orderReservationRepository.getAvailableFilteredOrders(
                ortc.SUOR_DTO_WITH_FILTERS.barbershopId(),
                ortc.SUOR_DTO_WITH_FILTERS.startWeekDate(),
                ortc.SUOR_DTO_WITH_FILTERS.orderFilters().getBarberIds()
        );

        assertTrue(availableOrders.isEmpty());
    }


    @Test
    void reserveOrdersByOrderIdsAndByCustomerId() {
        ortc.UNRESERVED_ORDER_ENTITY_LIST.forEach(orderRepository::addOrder);

        List<Order> reservedOrders = orderReservationRepository.reserveOrdersByOrderIdsAndByCustomerId(
                ortc.VALID_ORDER_RESERV_DTO.orderIds(),
                ortc.VALID_ORDER_RESERV_DTO.customerId()
        );

        assertEquals(ortc.RESERVED_ORDER_ENTITY_LIST.size(), reservedOrders.size());
        assertTrue(ortc.RESERVED_ORDER_ENTITY_LIST.containsAll(reservedOrders));
    }
}
