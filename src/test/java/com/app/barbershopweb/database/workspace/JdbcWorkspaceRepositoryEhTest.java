package com.app.barbershopweb.database.workspace;

import com.app.barbershopweb.barbershop.repository.JdbcBarbershopRepository;
import com.app.barbershopweb.exception.DbUniqueConstraintsViolationException;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.user.crud.repository.JdbcUsersRepository;
import com.app.barbershopweb.workspace.repository.JdbcWorkspaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.app.barbershopweb.barbershop.constants.BarbershopEntity__TestConstants.BARBERSHOP_VALID_ENTITY;
import static com.app.barbershopweb.user.crud.constants.UserEntity__TestConstants.USERS_VALID_ENTITY;
import static com.app.barbershopweb.workspace.constants.WorkspaceEntity__TestConstants.WORKSPACE_VALID_ENTITY;
import static com.app.barbershopweb.workspace.constants.error.WorkspaceErrorMessage_Fk__TestConstants.WORKSPACE_ERR_FK_BARBERSHOP_ID;
import static com.app.barbershopweb.workspace.constants.error.WorkspaceErrorMessage_Fk__TestConstants.WORKSPACE_ERR_FK_USER_ID;
import static com.app.barbershopweb.workspace.constants.error.WorkspaceErrorMessage_Uk__TestConstants.WORKSPACE_ERR_UK_USER_ID__BARBERSHOP_ID;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("jdbc workspace repository error handling tests")
class JdbcWorkspaceRepositoryEhTest extends AbstractIT {
    @Autowired
    JdbcUsersRepository usersRepository;
    @Autowired
    JdbcBarbershopRepository barbershopRepository;
    @Autowired
    JdbcWorkspaceRepository workspaceRepository;


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
                    workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY);
                }
        );
        assertEquals(2, thrown.getMessages().size());
        assertTrue(thrown.getMessages().contains(WORKSPACE_ERR_FK_USER_ID));
        assertTrue(thrown.getMessages().contains(WORKSPACE_ERR_FK_BARBERSHOP_ID));
    }

    @Test
    @DisplayName(
            """
                    when trying to add workspace with existing uk-s 
                    should throw DbUniqueConstraintsViolationException with uk error message
                    """
    )
    void addWorkspaceUkViolation() {
        usersRepository.addUser(USERS_VALID_ENTITY);
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY);

        DbUniqueConstraintsViolationException thrown =
                assertThrows(
                        DbUniqueConstraintsViolationException.class,
                        () -> {
                            workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY);
                        }
                );
        assertEquals(1, thrown.getMessages().size());
        assertTrue(thrown.getMessages().contains(WORKSPACE_ERR_UK_USER_ID__BARBERSHOP_ID));

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
                    workspaceRepository.updateWorkspace(WORKSPACE_VALID_ENTITY);
                }
        );
        assertEquals(2, thrown.getMessages().size());
        assertTrue(thrown.getMessages().contains(WORKSPACE_ERR_FK_USER_ID));
        assertTrue(thrown.getMessages().contains(WORKSPACE_ERR_FK_BARBERSHOP_ID));
    }

    @Test
    @DisplayName(
            """
                    when trying to update workspace with existing uk-s 
                    should throw DbUniqueConstraintsViolationException with uk error message
                    """
    )
    void updateWorkspaceUkViolation() {
        usersRepository.addUser(USERS_VALID_ENTITY);
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY);

        DbUniqueConstraintsViolationException thrown =
                assertThrows(
                        DbUniqueConstraintsViolationException.class,
                        () -> {
                            workspaceRepository.updateWorkspace(WORKSPACE_VALID_ENTITY);
                        }
                );
        assertEquals(1, thrown.getMessages().size());
        assertTrue(thrown.getMessages().contains(WORKSPACE_ERR_UK_USER_ID__BARBERSHOP_ID));

    }

    @AfterEach
    void cleanUpDb() {
        usersRepository.truncateAndRestartSequence();
        barbershopRepository.truncateAndRestartSequence();
        workspaceRepository.truncateAndRestartSequence();
    }
}
