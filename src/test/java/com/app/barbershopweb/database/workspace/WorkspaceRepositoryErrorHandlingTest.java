package com.app.barbershopweb.database.workspace;

import com.app.barbershopweb.barbershop.crud.repository.BarbershopRepository;
import com.app.barbershopweb.exception.DbUniqueConstraintsViolationException;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.user.crud.repository.UserRepository;
import com.app.barbershopweb.workspace.repository.WorkspaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.app.barbershopweb.barbershop.crud.constants.BarbershopEntity__TestConstants.BARBERSHOP_VALID_ENTITY;
import static com.app.barbershopweb.user.crud.constants.UserEntity__TestConstants.USER_VALID_ENTITY;
import static com.app.barbershopweb.workspace.constants.WorkspaceEntity__TestConstants.WORKSPACE_VALID_ENTITY;
import static com.app.barbershopweb.workspace.constants.error.WorkspaceErrorMessage_Fk__TestConstants.WORKSPACE_ERR_FK_BARBERSHOP_ID;
import static com.app.barbershopweb.workspace.constants.error.WorkspaceErrorMessage_Fk__TestConstants.WORKSPACE_ERR_FK_USER_ID;
import static com.app.barbershopweb.workspace.constants.error.WorkspaceErrorMessage_Uk__TestConstants.WORKSPACE_ERR_UK_USER_ID__BARBERSHOP_ID;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("jdbc workspace repository error handling tests")
class WorkspaceRepositoryErrorHandlingTest extends AbstractIT {
    @Autowired
    UserRepository usersRepository;
    @Autowired
    BarbershopRepository barbershopRepository;
    @Autowired
    WorkspaceRepository workspaceRepository;


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
        usersRepository.addUser(USER_VALID_ENTITY);
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
        usersRepository.addUser(USER_VALID_ENTITY);
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
