package com.app.barbershopweb.database.order.crud;

import com.app.barbershopweb.barbershop.repository.JdbcBarbershopRepository;
import com.app.barbershopweb.exception.DbUniqueConstraintsViolationException;
import com.app.barbershopweb.exception.InvalidBusinessDataFormatException;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.order.crud.repository.JdbcOrderRepository;
import com.app.barbershopweb.user.crud.repository.JdbcUsersRepository;
import com.app.barbershopweb.workspace.repository.JdbcWorkspaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.app.barbershopweb.barbershop.constants.BarbershopEntity__TestConstants.BARBERSHOP_VALID_ENTITY;
import static com.app.barbershopweb.order.crud.constants.OrderEntity__TestConstants.ORDER_INVALID_BUSINESS_ENTITY;
import static com.app.barbershopweb.order.crud.constants.OrderEntity__TestConstants.ORDER_VALID_ENTITY;
import static com.app.barbershopweb.order.crud.constants.error.OrderErrorMessage_Business_TestConstants.*;
import static com.app.barbershopweb.order.crud.constants.error.OrderErrorMessage_Fk__TestConstants.ORDER_ERR_FK_BARBER_ID;
import static com.app.barbershopweb.order.crud.constants.error.OrderErrorMessage_Fk__TestConstants.ORDER_ERR_FK_CUSTOMER_ID;
import static com.app.barbershopweb.order.crud.constants.error.OrderErrorMessage_Uk__TestConstants.ORDER_ERR_UK_BARBER_ID__ORDER_DATE;
import static com.app.barbershopweb.order.crud.constants.error.OrderErrorMessage_Uk__TestConstants.ORDER_ERR_UK_CUSTOMER_ID__ORDER_DATE;
import static com.app.barbershopweb.user.crud.constants.UserEntity__TestConstants.USERS_VALID_ENTITY;
import static com.app.barbershopweb.workspace.constants.WorkspaceEntity__TestConstants.WORKSPACE_VALID_ENTITY;
import static com.app.barbershopweb.workspace.constants.WorkspaceList__TestConstants.WORKSPACE_VALID_ENTITY_LIST;
import static org.junit.jupiter.api.Assertions.*;

class JdbcOrderRepositoryEhTest extends AbstractIT {
    @Autowired
    JdbcWorkspaceRepository workspaceRepository;
    @Autowired
    JdbcBarbershopRepository barbershopRepository;
    @Autowired
    JdbcUsersRepository usersRepository;
    @Autowired
    JdbcOrderRepository orderRepository;


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
                    orderRepository.addOrder(ORDER_VALID_ENTITY);
                }
        );

        assertEquals(2, thrown.getMessages().size());
        assertTrue(thrown.getMessages().contains(ORDER_ERR_FK_BARBER_ID));
        assertTrue(thrown.getMessages().contains(ORDER_ERR_FK_CUSTOMER_ID));
    }

    @Test
    @DisplayName(
            """
                    when trying to add order with existing uk-s 
                    should throw DbUniqueConstraintsViolationException with uk error message
                    """
    )
    void addOrderUkViolation() {
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        usersRepository.addUser(USERS_VALID_ENTITY);
        usersRepository.addUser(USERS_VALID_ENTITY);
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY);
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY_LIST.get(1));

        orderRepository.addOrder(ORDER_VALID_ENTITY);

        DbUniqueConstraintsViolationException thrown =
                assertThrows(DbUniqueConstraintsViolationException.class,
                        () -> {
                            orderRepository.addOrder(ORDER_VALID_ENTITY);
                        }
                );

        assertEquals(2, thrown.getMessages().size());
        assertTrue(thrown.getMessages().contains(ORDER_ERR_UK_BARBER_ID__ORDER_DATE));
        assertTrue(thrown.getMessages().contains(ORDER_ERR_UK_CUSTOMER_ID__ORDER_DATE));
    }

    @Test
    @DisplayName(
            """
                    when trying to add order with wrong business data format 
                    should throw InvalidBusinessDataFormatException with bdf error message
                    """
    )
    void addOrderBdfViolation() {
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        usersRepository.addUser(USERS_VALID_ENTITY);
        usersRepository.addUser(USERS_VALID_ENTITY);
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY);
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY_LIST.get(1));

        InvalidBusinessDataFormatException thrown = assertThrows(
                InvalidBusinessDataFormatException.class,
                () -> {
                    orderRepository.addOrder(ORDER_INVALID_BUSINESS_ENTITY);
                }
        );

        assertEquals(3, thrown.getMessages().size());
        assertTrue(thrown.getMessages().contains(ORDER_ERR_BUSINESS_BARBERSHOP_HOURS));
        assertTrue(thrown.getMessages().contains(ORDER_ERR_BUSINESS_CUSTOMER_ID_BARBER_ID));
        assertTrue(thrown.getMessages().contains(ORDER_ERR_BUSINESS_TIME_FORMAT));
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
                    orderRepository.updateOrder(ORDER_VALID_ENTITY);
                }
        );

        assertEquals(2, thrown.getMessages().size());
        assertTrue(thrown.getMessages().contains(ORDER_ERR_FK_BARBER_ID));
        assertTrue(thrown.getMessages().contains(ORDER_ERR_FK_CUSTOMER_ID));
    }

    @Test
    @DisplayName(
            """
                    when trying to update order with existing uk-s 
                    should throw DbUniqueConstraintsViolationException with uk error message
                    """
    )
    void updateOrderUkViolation() {
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        usersRepository.addUser(USERS_VALID_ENTITY);
        usersRepository.addUser(USERS_VALID_ENTITY);
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY);
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY_LIST.get(1));

        orderRepository.addOrder(ORDER_VALID_ENTITY);

        DbUniqueConstraintsViolationException thrown =
                assertThrows(DbUniqueConstraintsViolationException.class,
                        () -> {
                            orderRepository.updateOrder(ORDER_VALID_ENTITY);
                        }
                );

        assertEquals(2, thrown.getMessages().size());
        assertTrue(thrown.getMessages().contains(ORDER_ERR_UK_BARBER_ID__ORDER_DATE));
        assertTrue(thrown.getMessages().contains(ORDER_ERR_UK_CUSTOMER_ID__ORDER_DATE));
    }

    @Test
    @DisplayName(
            """
                    when trying to update order with wrong business data format 
                    should throw InvalidBusinessDataFormatException with bdf error message
                    """
    )
    void updateOrderBdfViolation() {
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        usersRepository.addUser(USERS_VALID_ENTITY);
        usersRepository.addUser(USERS_VALID_ENTITY);
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY);
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY_LIST.get(1));

        orderRepository.addOrder(ORDER_VALID_ENTITY);

        InvalidBusinessDataFormatException thrown = assertThrows(
                InvalidBusinessDataFormatException.class,
                () -> {
                    orderRepository.updateOrder(ORDER_INVALID_BUSINESS_ENTITY);
                }
        );

        assertEquals(3, thrown.getMessages().size());
        assertTrue(thrown.getMessages().contains(ORDER_ERR_BUSINESS_BARBERSHOP_HOURS));
        assertTrue(thrown.getMessages().contains(ORDER_ERR_BUSINESS_CUSTOMER_ID_BARBER_ID));
        assertTrue(thrown.getMessages().contains(ORDER_ERR_BUSINESS_TIME_FORMAT));
    }


}
