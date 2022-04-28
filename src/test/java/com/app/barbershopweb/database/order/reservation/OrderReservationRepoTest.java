package com.app.barbershopweb.database.order.reservation;

import com.app.barbershopweb.barbershop.crud.repository.JdbcBarbershopRepository;
import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.order.crud.Order;
import com.app.barbershopweb.order.crud.repository.JdbcOrderRepository;
import com.app.barbershopweb.order.reservation.repository.OrderReservationRepository;
import com.app.barbershopweb.user.crud.repository.JdbcUsersRepository;
import com.app.barbershopweb.workspace.repository.JdbcWorkspaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.app.barbershopweb.order.reservation.constants.dto.OrderReservation_Dto__TestConstants.ORDER_RESERVATION_VALID_DTO;
import static com.app.barbershopweb.order.reservation.constants.dto.OrderReservation_GetOpenOrders_Dto__TestConstants.GET_OPEN_ORDERS__REQUEST_VALID_DTO;
import static com.app.barbershopweb.order.reservation.constants.dto.OrderReservation_GetOpenOrders_Filtered_Dto__TestConstants.GET_OPEN_FILTERED_ORDERS__REQUEST_DTO;
import static com.app.barbershopweb.order.reservation.constants.list.entity.OrderReservation_List_OrderEntity__TestConstants.*;
import static com.app.barbershopweb.order.reservation.constants.list.fk.OrderReservation_FkEntityList__TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DisplayName("order reservation repository test without error handling")
class OrderReservationRepoTest extends AbstractIT {

    @Autowired
    OrderReservationRepository orderReservationRepository;
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
                        GET_OPEN_ORDERS__REQUEST_VALID_DTO.barbershopId(),
                        GET_OPEN_ORDERS__REQUEST_VALID_DTO.startWeekDate()
                ).isEmpty()
        );

        ORDER_RESERVATION_OPEN_ORDER_ENTITY_LIST.forEach(orderRepository::addOrder);

        List<Order> availableOrders = orderReservationRepository.getAvailableOrders(
                GET_OPEN_ORDERS__REQUEST_VALID_DTO.barbershopId(),
                GET_OPEN_ORDERS__REQUEST_VALID_DTO.startWeekDate()
        );

        //database returns '0' instead of null, but it was saved as null before
        assertTrue(availableOrders.stream().allMatch(order -> order.getCustomerId() == 0));
        availableOrders.forEach(order -> order.setCustomerId(null));

        assertTrue(ORDER_RESERVATION_OPEN_ORDER_ENTITY_LIST.containsAll(availableOrders));
    }

    @Test
    @DisplayName("when there are no suitable orders (doesn't meet sql conditions), " +
            "but they EXIST  " +
            "- returns empty list")
    void getAvailableOrdersNotSuitableOrders() {
        assertTrue(
                orderReservationRepository.getAvailableOrders(
                        GET_OPEN_ORDERS__REQUEST_VALID_DTO.barbershopId(),
                        GET_OPEN_ORDERS__REQUEST_VALID_DTO.startWeekDate()
                ).isEmpty()
        );

        ORDER_RESERVATION_UNFIT_OPEN_ORDER_ENTITY_LIST.forEach(orderRepository::addOrder);

        List<Order> availableOrders = orderReservationRepository.getAvailableOrders(
                GET_OPEN_ORDERS__REQUEST_VALID_DTO.barbershopId(),
                GET_OPEN_ORDERS__REQUEST_VALID_DTO.startWeekDate()
        );

        assertTrue(availableOrders.isEmpty());
    }


    @Test
    void getAvailableFilteredOrders() {
        assertTrue(
                orderReservationRepository.getAvailableFilteredOrders(
                        GET_OPEN_FILTERED_ORDERS__REQUEST_DTO.barbershopId(),
                        GET_OPEN_FILTERED_ORDERS__REQUEST_DTO.startWeekDate(),
                        GET_OPEN_FILTERED_ORDERS__REQUEST_DTO.orderFilters().getBarberIds()
                ).isEmpty()
        );

        ORDER_RESERVATION_OPEN_FILTERED_ORDER_ENTITY_LIST.forEach(orderRepository::addOrder);

        List<Order> filteredOrders = orderReservationRepository.getAvailableFilteredOrders(
                GET_OPEN_FILTERED_ORDERS__REQUEST_DTO.barbershopId(),
                GET_OPEN_FILTERED_ORDERS__REQUEST_DTO.startWeekDate(),
                GET_OPEN_FILTERED_ORDERS__REQUEST_DTO.orderFilters().getBarberIds()
        );


        //database returns '0' instead of null, but it was saved as null before
        assertTrue(filteredOrders.stream().allMatch(order -> order.getCustomerId() == 0));
        filteredOrders.forEach(order -> order.setCustomerId(null));

        assertTrue(ORDER_RESERVATION_OPEN_FILTERED_ORDER_ENTITY_LIST.containsAll(filteredOrders));

    }

    @Test
    @DisplayName("when there are no suitable orders (doesn't meet sql conditions + filter), " +
            "but they EXIST  " +
            "- returns empty list")
    void getFilteredAvailableOrdersNotSuitableOrders() {
        assertTrue(
                orderReservationRepository.getAvailableFilteredOrders(
                        GET_OPEN_FILTERED_ORDERS__REQUEST_DTO.barbershopId(),
                        GET_OPEN_FILTERED_ORDERS__REQUEST_DTO.startWeekDate(),
                        GET_OPEN_FILTERED_ORDERS__REQUEST_DTO.orderFilters().getBarberIds()
                ).isEmpty()
        );

        ORDER_RESERVATION_UNFIT_OPEN_ORDER_ENTITY_LIST.forEach(orderRepository::addOrder);

        List<Order> availableOrders = orderReservationRepository.getAvailableFilteredOrders(
                GET_OPEN_FILTERED_ORDERS__REQUEST_DTO.barbershopId(),
                GET_OPEN_FILTERED_ORDERS__REQUEST_DTO.startWeekDate(),
                GET_OPEN_FILTERED_ORDERS__REQUEST_DTO.orderFilters().getBarberIds()
        );

        assertTrue(availableOrders.isEmpty());
    }


    @Test
    void reserveOrders() {
        ORDER_RESERVATION_OPEN_ORDER_ENTITY_LIST.forEach(orderRepository::addOrder);

        List<Order> reservedOrders = orderReservationRepository.reserveOrders(
                ORDER_RESERVATION_VALID_DTO.orderIds(),
                ORDER_RESERVATION_VALID_DTO.customerId()
        );

        assertTrue(ORDER_RESERVATION_CLOSED_ORDER_ENTITY_LIST.containsAll(reservedOrders));
    }
}
