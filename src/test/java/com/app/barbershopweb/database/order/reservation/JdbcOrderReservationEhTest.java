package com.app.barbershopweb.database.order.reservation;

import com.app.barbershopweb.barbershop.repository.JdbcBarbershopRepository;
import com.app.barbershopweb.database.AbstractJdbcRepositoryTest;
import com.app.barbershopweb.exception.DbUniqueConstraintsViolationException;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.order.crud.repository.JdbcOrderRepository;
import com.app.barbershopweb.order.reservation.OrderReservationTestConstants;
import com.app.barbershopweb.order.reservation.repository.JdbcOrderReservationRepository;
import com.app.barbershopweb.user.repository.JdbcUsersRepository;
import com.app.barbershopweb.workspace.repository.JdbcWorkspaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcOrderReservationEhTest extends AbstractJdbcRepositoryTest {

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
    void reserveOrderByOrderIdAndCustomerIdFkCv() {

        NotFoundException thrown = assertThrows(
                NotFoundException.class,
                () -> {
                    orderReservationRepository.reserveOrdersByOrderIdsAndByCustomerId(
                            ortc.VALID_ORDER_RESERV_DTO.orderIds(),
                            ortc.VALID_ORDER_RESERV_DTO.customerId()
                    );
                }
        );
        assertEquals(1, thrown.getMessages().size());
        assertEquals(thrown.getMessages(), List.of(ortc.FK_CV_CUSTOMER_ID_ERR_MSG));
    }

    @Test
    @DisplayName(
            """
            When fk violation (orderIds) in the dto,
             throws NotFoundException
            """
    )
    void reserveOrderByOrderIdAndCustomerIdOrderIdsFkCv() {
        ortc.FK_USER_ENTITY_LIST.forEach(usersRepository::addUser);

        NotFoundException thrown = assertThrows(
                NotFoundException.class,
                () -> {
                    orderReservationRepository.reserveOrdersByOrderIdsAndByCustomerId(
                            ortc.VALID_ORDER_RESERV_DTO.orderIds(),
                            ortc.VALID_ORDER_RESERV_DTO.customerId()
                    );
                }
        );
        assertEquals(
                ortc.VALID_ORDER_RESERV_DTO.orderIds().size(),
                thrown.getMessages().size()
        );
        assertEquals(thrown.getMessages(), ortc.FK_CV_ORDER_ID_LIST_ERR_MSG);
    }

    @Test
    @DisplayName(
            """
            When uk violation (customerId, orderDate) in the dto, 
            throws DbUniqueConstraintsViolationException
            """
    )
    void reserveOrderByOrderIdAndCustomerIdOrderIdsUkCv() {
        ortc.FK_USER_ENTITY_LIST.forEach(usersRepository::addUser);
        ortc.FK_BARBERSHOP_ENTITY_LIST.forEach(barbershopRepository::addBarbershop);
        ortc.FK_WORKSPACE_ENTITY_LIST.forEach(workspaceRepository::addWorkspace);
        ortc.UNRESERVED_ORDER_ENTITY_LIST.forEach(orderRepository::addOrder);

        orderReservationRepository.reserveOrdersByOrderIdsAndByCustomerId(
          ortc.VALID_ORDER_RESERV_DTO.orderIds(),
          ortc.VALID_ORDER_RESERV_DTO.customerId()
        );

        DbUniqueConstraintsViolationException thrown = assertThrows(
                DbUniqueConstraintsViolationException.class,
                () -> {
                    orderReservationRepository.reserveOrdersByOrderIdsAndByCustomerId(
                            ortc.VALID_ORDER_RESERV_DTO.orderIds(),
                            ortc.VALID_ORDER_RESERV_DTO.customerId()
                    );
                }
        );
        assertEquals(
                1,
                thrown.getMessages().size()
        );
        assertEquals(thrown.getMessages(), List.of(ortc.UK_CV_CUSTOMER_ID_ORDER_DATE_ERR_MSG));
    }

}

