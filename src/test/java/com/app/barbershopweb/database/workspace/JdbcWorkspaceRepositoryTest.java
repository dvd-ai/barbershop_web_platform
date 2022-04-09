package com.app.barbershopweb.database.workspace;

import com.app.barbershopweb.barbershop.repository.JdbcBarbershopRepository;
import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.user.repository.JdbcUsersRepository;
import com.app.barbershopweb.workspace.Workspace;
import com.app.barbershopweb.workspace.repository.JdbcWorkspaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.app.barbershopweb.barbershop.constants.BarbershopEntity__TestConstants.BARBERSHOP_VALID_ENTITY;
import static com.app.barbershopweb.barbershop.constants.BarbershopList__TestConstants.BARBERSHOP_VALID_ENTITY_LIST;
import static com.app.barbershopweb.barbershop.constants.BarbershopMetadata__TestConstants.BARBERSHOP_VALID_BARBERSHOP_ID;
import static com.app.barbershopweb.user.constants.UserEntity__TestConstants.USERS_VALID_ENTITY;
import static com.app.barbershopweb.user.constants.UserList__TestConstants.USERS_USER_VALID_ENTITY_LIST;
import static com.app.barbershopweb.user.constants.UserMetadata__TestConstants.USERS_VALID_USER_ID;
import static com.app.barbershopweb.workspace.constants.WorkspaceEntity__TestConstants.WORKSPACE_VALID_ENTITY;
import static com.app.barbershopweb.workspace.constants.WorkspaceEntity__TestConstants.WORKSPACE_VALID_UPDATED_ENTITY;
import static com.app.barbershopweb.workspace.constants.WorkspaceList__TestConstants.WORKSPACE_VALID_ENTITY_LIST;
import static com.app.barbershopweb.workspace.constants.WorkspaceMetadata__TestConstants.WORKSPACE_NOT_EXISTED_WORKSPACE_ID;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("jdbc workspace repository tests without error handling")
class JdbcWorkspaceRepositoryTest extends AbstractIT {

    @Autowired
    JdbcWorkspaceRepository workspaceRepository;
    @Autowired
    JdbcBarbershopRepository barbershopRepository;
    @Autowired
    JdbcUsersRepository usersRepository;


    @BeforeEach
    void initFks() {
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY);
        usersRepository.addUser(USERS_VALID_ENTITY);
    }

    @Test
    void addWorkspace() {
        Long workspaceId = workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY);

        assertTrue(workspaceId > 0);
    }

    @Test
    void findWorkspaceById() {
        Long id = workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY);
        Optional<Workspace> workspaceOptional = workspaceRepository.findWorkspaceById(id);

        assertTrue(workspaceOptional.isPresent());
        assertEquals(WORKSPACE_VALID_ENTITY, workspaceOptional.get());
    }

    @Test
    void findWorkspaceByNotExistingId() {
        Optional<Workspace> workspaceOptional = workspaceRepository.findWorkspaceById(
                WORKSPACE_NOT_EXISTED_WORKSPACE_ID
        );

        assertTrue(workspaceOptional.isEmpty());
    }

    @Test
    void workspaceExistsByBarbershopIdAndUserId() {

        assertFalse(workspaceRepository.workspaceExistsByBarbershopIdAndUserId(
                        BARBERSHOP_VALID_BARBERSHOP_ID, USERS_VALID_USER_ID
                )
        );

        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY);

        assertTrue(workspaceRepository.workspaceExistsByBarbershopIdAndUserId(
                        BARBERSHOP_VALID_BARBERSHOP_ID, USERS_VALID_USER_ID
                )
        );
    }

    @Test
    void workspaceIsActiveByBarbershopIdAndUserId() {
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY);

        assertTrue(workspaceRepository.workspaceIsActiveByBarbershopIdAndUserId(
                        BARBERSHOP_VALID_BARBERSHOP_ID, USERS_VALID_USER_ID
                )
        );
    }

    @Test
    void updateNotExistingWorkspace() {
        Optional<Workspace> notFoundWorkspace = workspaceRepository.updateWorkspace(WORKSPACE_VALID_ENTITY);

        assertTrue(notFoundWorkspace.isEmpty());
    }

    @Test
    void updateExistingWorkspace() {

        usersRepository.addUser(USERS_USER_VALID_ENTITY_LIST.get(1));
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY);
        Optional<Workspace> updWorkspaceOptional = workspaceRepository.updateWorkspace(
                WORKSPACE_VALID_UPDATED_ENTITY
        );

        assertTrue(updWorkspaceOptional.isPresent());
        assertEquals(WORKSPACE_VALID_UPDATED_ENTITY, updWorkspaceOptional.get());
    }

    @Test
    void getWorkspaces() {
        assertTrue(workspaceRepository.getWorkspaces().isEmpty());

        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY_LIST.get(1));
        barbershopRepository.addBarbershop(BARBERSHOP_VALID_ENTITY_LIST.get(2));
        usersRepository.addUser(USERS_USER_VALID_ENTITY_LIST.get(1));
        usersRepository.addUser(USERS_USER_VALID_ENTITY_LIST.get(2));

        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY_LIST.get(0));
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY_LIST.get(1));
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY_LIST.get(2));

        assertEquals(
                WORKSPACE_VALID_ENTITY_LIST.size(),
                workspaceRepository.getWorkspaces().size()
        );
        assertEquals(WORKSPACE_VALID_ENTITY_LIST, workspaceRepository.getWorkspaces());
    }

    @Test
    void deleteWorkspaceById() {
        Long id = workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY);
        workspaceRepository.deleteWorkspaceById(id);

        assertTrue(workspaceRepository.getWorkspaces().isEmpty());
    }

    @Test
    void truncateAndRestartSequences() {
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY);
        workspaceRepository.truncateAndRestartSequence();

        assertTrue(workspaceRepository.getWorkspaces().isEmpty());

        Long id = workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY);

        assertEquals(1, id);
    }

    @AfterEach
    void cleanUpDb() {
        usersRepository.truncateAndRestartSequence();
        barbershopRepository.truncateAndRestartSequence();
        workspaceRepository.truncateAndRestartSequence();
    }
}