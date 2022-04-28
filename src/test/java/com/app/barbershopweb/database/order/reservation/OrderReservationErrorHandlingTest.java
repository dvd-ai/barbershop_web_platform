package com.app.barbershopweb.database.order.reservation;

import com.app.barbershopweb.barbershop.crud.repository.JdbcBarbershopRepository;
import com.app.barbershopweb.exception.DbUniqueConstraintsViolationException;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.order.crud.repository.JdbcOrderRepository;
import com.app.barbershopweb.order.reservation.repository.OrderReservationRepository;
import com.app.barbershopweb.user.crud.repository.JdbcUsersRepository;
import com.app.barbershopweb.workspace.repository.JdbcWorkspaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.app.barbershopweb.order.reservation.constants.dto.OrderReservation_Dto__TestConstants.ORDER_RESERVATION_VALID_DTO;
import static com.app.barbershopweb.order.reservation.constants.error.OrderReservation_ErrorMessage_Fk__TestConstants.ORDER_RESERVATION_ERR_FK_CUSTOMER_ID;
import static com.app.barbershopweb.order.reservation.constants.error.OrderReservation_ErrorMessage_Fk__TestConstants.ORDER_RESERVATION_ERR_FK_ORDER_ID_LIST;
import static com.app.barbershopweb.order.reservation.constants.error.OrderReservation_ErrorMessage_Uk__TestConstants.ORDER_RESERVATION_ERR_UK_CUSTOMER_ID__ORDER_DATE;
import static com.app.barbershopweb.order.reservation.constants.list.entity.OrderReservation_List_OrderEntity__TestConstants.ORDER_RESERVATION_OPEN_ORDER_ENTITY_LIST;
import static com.app.barbershopweb.order.reservation.constants.list.fk.OrderReservation_FkEntityList__TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderReservationErrorHandlingTest extends AbstractIT {
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


    @AfterEach
    void cleanUpDb() {
        usersRepository.truncateAndRestartSequence();
        barbershopRepository.truncateAndRestartSequence();
        workspaceRepository.truncateAndRestartSequence();
        orderRepository.truncateAndRestartSequence();
    }

    @Test
    @DisplayName(
            """
                    When fk violation (customerId) in the dto,
                     throws NotFoundException 
                    """
    )
    void reserveOrderFkCv() {

        NotFoundException thrown = assertThrows(
                NotFoundException.class,
                () -> {
                    orderReservationRepository.reserveOrders(
                            ORDER_RESERVATION_VALID_DTO.orderIds(),
                            ORDER_RESERVATION_VALID_DTO.customerId()
                    );
                }
        );
        assertEquals(1, thrown.getMessages().size());
        assertEquals(thrown.getMessages(), List.of(ORDER_RESERVATION_ERR_FK_CUSTOMER_ID));
    }

    @Test
    @DisplayName(
            """
                    When fk violation (orderIds) in the dto,
                     throws NotFoundException
                    """
    )
    void reserveOrderOrderIdsFkCv() {
        ORDER_RESERVATION_FK_USER_ENTITY_LIST.forEach(usersRepository::addUser);

        NotFoundException thrown = assertThrows(
                NotFoundException.class,
                () -> {
                    orderReservationRepository.reserveOrders(
                            ORDER_RESERVATION_VALID_DTO.orderIds(),
                            ORDER_RESERVATION_VALID_DTO.customerId()
                    );
                }
        );
        assertEquals(
                ORDER_RESERVATION_VALID_DTO.orderIds().size(),
                thrown.getMessages().size()
        );
        assertEquals(thrown.getMessages(), ORDER_RESERVATION_ERR_FK_ORDER_ID_LIST);
    }

    @Test
    @DisplayName(
            """
                    When uk violation (customerId, orderDate) in the dto, 
                    throws DbUniqueConstraintsViolationException
                    """
    )
    void reserveOrderUkCv() {
        ORDER_RESERVATION_FK_USER_ENTITY_LIST.forEach(usersRepository::addUser);
        ORDER_RESERVATION_FK_BARBERSHOP_ENTITY_LIST.forEach(barbershopRepository::addBarbershop);
        ORDER_RESERVATION_FK_WORKSPACE_ENTITY_LIST.forEach(workspaceRepository::addWorkspace);
        ORDER_RESERVATION_OPEN_ORDER_ENTITY_LIST.forEach(orderRepository::addOrder);

        orderReservationRepository.reserveOrders(
                ORDER_RESERVATION_VALID_DTO.orderIds(),
                ORDER_RESERVATION_VALID_DTO.customerId()
        );

        DbUniqueConstraintsViolationException thrown = assertThrows(
                DbUniqueConstraintsViolationException.class,
                () -> {
                    orderReservationRepository.reserveOrders(
                            ORDER_RESERVATION_VALID_DTO.orderIds(),
                            ORDER_RESERVATION_VALID_DTO.customerId()
                    );
                }
        );
        assertEquals(
                1,
                thrown.getMessages().size()
        );
        assertEquals(thrown.getMessages(), List.of(ORDER_RESERVATION_ERR_UK_CUSTOMER_ID__ORDER_DATE));
    }

}

