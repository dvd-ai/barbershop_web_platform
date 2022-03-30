package com.app.barbershopweb.database.workspace;

import com.app.barbershopweb.barbershop.BarbershopTestConstants;
import com.app.barbershopweb.barbershop.repository.JdbcBarbershopRepository;
import com.app.barbershopweb.database.AbstractJdbcRepositoryTest;
import com.app.barbershopweb.exception.DbUniqueConstraintsViolationException;
import com.app.barbershopweb.exception.NotFoundException;
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

@DisplayName("jdbc workspace repository error handling tests")
class JdbcWorkspaceRepositoryEhTest extends AbstractJdbcRepositoryTest {
    static JdbcUsersRepository usersRepository;
    static JdbcBarbershopRepository barbershopRepository;
    static JdbcWorkspaceRepository workspaceRepository;

    WorkspaceTestConstants wtc = new WorkspaceTestConstants();
    UserTestConstants utc = new UserTestConstants();
    BarbershopTestConstants btc = new BarbershopTestConstants();

    @BeforeAll
    static void init() {
        DataSource ds = getDataSource();
        barbershopRepository = new JdbcBarbershopRepository(ds);
        usersRepository = new JdbcUsersRepository(ds);
        workspaceRepository = new JdbcWorkspaceRepository(
                ds, usersRepository, barbershopRepository
        );
    }

    @Test
    @DisplayName(
            """
            when trying to add workspace with not existing fk-s 
            should throw NotFoundException with fk error messages
            """
    )
    void addWorkspaceNotExistingFks() {

        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> {
                    workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY);
                }
        );
        assertEquals(2, thrown.getMessages().size());
        assertTrue(thrown.getMessages().contains(wtc.FK_CV_USER_ID_ERR_MSG));
        assertTrue(thrown.getMessages().contains(wtc.FK_CV_BARBERSHOP_ID_ERR_MSG));
    }

    @Test
    @DisplayName(
            """
            when trying to add workspace with existing uk-s 
            should throw DbUniqueConstraintsViolationException with uk error message
            """
    )
    void addWorkspaceUkViolation() {
        usersRepository.addUser(utc.VALID_USER_ENTITY);
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY);

        DbUniqueConstraintsViolationException thrown =
                assertThrows(
                        DbUniqueConstraintsViolationException.class,
                        () -> {
                            workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY);
                        }
                );
        assertEquals(1, thrown.getMessages().size());
        assertTrue(thrown.getMessages().contains(wtc.UK_CV_ERR_MSG));

    }

    @Test
    @DisplayName(
            """
            when trying to update workspace with not existing fk-s 
            should throw NotFoundException with fk error messages
            """
    )
    void updateWorkspaceNotExistingFks() {

        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> {
                    workspaceRepository.updateWorkspace(wtc.VALID_WORKSPACE_ENTITY);
                }
        );
        assertEquals(2, thrown.getMessages().size());
        assertTrue(thrown.getMessages().contains(wtc.FK_CV_USER_ID_ERR_MSG));
        assertTrue(thrown.getMessages().contains(wtc.FK_CV_BARBERSHOP_ID_ERR_MSG));
    }

    @Test
    @DisplayName(
            """
            when trying to update workspace with existing uk-s 
            should throw DbUniqueConstraintsViolationException with uk error message
            """
    )
    void updateWorkspaceUkViolation() {
        usersRepository.addUser(utc.VALID_USER_ENTITY);
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY);

        DbUniqueConstraintsViolationException thrown =
                assertThrows(
                        DbUniqueConstraintsViolationException.class,
                        () -> {
                            workspaceRepository.updateWorkspace(wtc.VALID_WORKSPACE_ENTITY);
                        }
                );
        assertEquals(1, thrown.getMessages().size());
        assertTrue(thrown.getMessages().contains(wtc.UK_CV_ERR_MSG));

    }

    @AfterEach
    void cleanUpDb() {
        usersRepository.truncateAndRestartSequence();
        barbershopRepository.truncateAndRestartSequence();
        workspaceRepository.truncateAndRestartSequence();
    }
}
