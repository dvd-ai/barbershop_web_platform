package com.app.barbershopweb.database.order.crud;

import com.app.barbershopweb.barbershop.BarbershopTestConstants;
import com.app.barbershopweb.barbershop.repository.JdbcBarbershopRepository;
import com.app.barbershopweb.database.AbstractJdbcRepositoryTest;
import com.app.barbershopweb.exception.DbUniqueConstraintsViolationException;
import com.app.barbershopweb.exception.InvalidBusinessDataFormatException;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.order.crud.OrderTestConstants;
import com.app.barbershopweb.order.crud.repository.JdbcOrderRepository;
import com.app.barbershopweb.user.UserTestConstants;
import com.app.barbershopweb.user.repository.JdbcUsersRepository;
import com.app.barbershopweb.workspace.WorkspaceTestConstants;
import com.app.barbershopweb.workspace.repository.JdbcWorkspaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

class JdbcOrderRepositoryEhTest extends AbstractJdbcRepositoryTest {
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

    @AfterEach
    void cleanUpDb() {
        barbershopRepository.truncateAndRestartSequence();
        usersRepository.truncateAndRestartSequence();
        workspaceRepository.truncateAndRestartSequence();
        orderRepository.truncateAndRestartSequence();
    }

    @Test
    @DisplayName(
            """
            when trying to add order with not existing fk-s 
            should throw NotFoundException with fk error messages
            """
    )
    void addOrderNotExistingFks() {
        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> {
                    orderRepository.addOrder(otc.VALID_ORDER_ENTITY);
                }
        );

        assertEquals(2, thrown.getMessages().size());
        assertTrue(thrown.getMessages().contains(otc.FK_CV_BARBER_ID_ERR_MSG));
        assertTrue(thrown.getMessages().contains(otc.FK_CV_CUSTOMER_ID_ERR_MSG));
    }

    @Test
    @DisplayName(
            """
            when trying to add order with existing uk-s 
            should throw DbUniqueConstraintsViolationException with uk error message
            """
    )
    void addOrderUkViolation() {
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        usersRepository.addUser(utc.VALID_USER_ENTITY);
        usersRepository.addUser(utc.VALID_USER_ENTITY);
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY);
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY_LIST.get(1));

        orderRepository.addOrder(otc.VALID_ORDER_ENTITY);

        DbUniqueConstraintsViolationException thrown =
                assertThrows(DbUniqueConstraintsViolationException.class,
                    () -> {
                        orderRepository.addOrder(otc.VALID_ORDER_ENTITY);
                    }
        );

        assertEquals(2, thrown.getMessages().size());
        assertTrue(thrown.getMessages().contains(otc.UK_CV_BARBER_ID_ORDER_DATE_ERR_MSG));
        assertTrue(thrown.getMessages().contains(otc.UK_CV_CUSTOMER_ID_ORDER_DATE_ERR_MSG));
    }

    @Test
    @DisplayName(
            """
            when trying to add order with wrong business data format 
            should throw InvalidBusinessDataFormatException with bdf error message
            """
    )
    void addOrderBdfViolation() {
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        usersRepository.addUser(utc.VALID_USER_ENTITY);
        usersRepository.addUser(utc.VALID_USER_ENTITY);
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY);
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY_LIST.get(1));

        InvalidBusinessDataFormatException thrown = assertThrows(
                InvalidBusinessDataFormatException.class,
                () -> {
                    orderRepository.addOrder(otc.INVALID_BDF_ORDER_ENTITY);
                }
        );

        assertEquals(3, thrown.getMessages().size());
        assertTrue(thrown.getMessages().contains(otc.BDF_CV_BARBERSHOP_HOURS_ERR_MSG));
        assertTrue(thrown.getMessages().contains(otc.BDF_CV_CUSTOMER_ID_BARBER_ID_EQ_ERR_MSG));
        assertTrue(thrown.getMessages().contains(otc.BDF_CV_TIME_FORMAT_ERR_MSG));
    }

    @Test
    @DisplayName(
            """
            when trying to update order with not existing fk-s 
            should throw NotFoundException with fk error messages
            """
    )
    void updateOrderNotExistingFks() {


        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> {
                    orderRepository.updateOrder(otc.VALID_ORDER_ENTITY);
                }
        );

        assertEquals(2, thrown.getMessages().size());
        assertTrue(thrown.getMessages().contains(otc.FK_CV_BARBER_ID_ERR_MSG));
        assertTrue(thrown.getMessages().contains(otc.FK_CV_CUSTOMER_ID_ERR_MSG));
    }

    @Test
    @DisplayName(
            """
            when trying to update order with existing uk-s 
            should throw DbUniqueConstraintsViolationException with uk error message
            """
    )
    void updateOrderUkViolation() {
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        usersRepository.addUser(utc.VALID_USER_ENTITY);
        usersRepository.addUser(utc.VALID_USER_ENTITY);
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY);
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY_LIST.get(1));

        orderRepository.addOrder(otc.VALID_ORDER_ENTITY);

        DbUniqueConstraintsViolationException thrown =
                assertThrows(DbUniqueConstraintsViolationException.class,
                        () -> {
                            orderRepository.updateOrder(otc.VALID_ORDER_ENTITY);
                        }
                );

        assertEquals(2, thrown.getMessages().size());
        assertTrue(thrown.getMessages().contains(otc.UK_CV_BARBER_ID_ORDER_DATE_ERR_MSG));
        assertTrue(thrown.getMessages().contains(otc.UK_CV_CUSTOMER_ID_ORDER_DATE_ERR_MSG));
    }

    @Test
    @DisplayName(
            """
            when trying to update order with wrong business data format 
            should throw InvalidBusinessDataFormatException with bdf error message
            """
    )
    void updateOrderBdfViolation() {
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        usersRepository.addUser(utc.VALID_USER_ENTITY);
        usersRepository.addUser(utc.VALID_USER_ENTITY);
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY);
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY_LIST.get(1));

        orderRepository.addOrder(otc.VALID_ORDER_ENTITY);

        InvalidBusinessDataFormatException thrown = assertThrows(
                InvalidBusinessDataFormatException.class,
                () -> {
                    orderRepository.updateOrder(otc.INVALID_BDF_ORDER_ENTITY);
                }
        );

        assertEquals(3, thrown.getMessages().size());
        assertTrue(thrown.getMessages().contains(otc.BDF_CV_BARBERSHOP_HOURS_ERR_MSG));
        assertTrue(thrown.getMessages().contains(otc.BDF_CV_CUSTOMER_ID_BARBER_ID_EQ_ERR_MSG));
        assertTrue(thrown.getMessages().contains(otc.BDF_CV_TIME_FORMAT_ERR_MSG));
    }


}
