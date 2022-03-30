package com.app.barbershopweb.database.workspace;

import com.app.barbershopweb.barbershop.BarbershopTestConstants;
import com.app.barbershopweb.barbershop.repository.JdbcBarbershopRepository;
import com.app.barbershopweb.database.AbstractJdbcRepositoryTest;
import com.app.barbershopweb.user.UserTestConstants;
import com.app.barbershopweb.user.repository.JdbcUsersRepository;
import com.app.barbershopweb.workspace.Workspace;
import com.app.barbershopweb.workspace.WorkspaceTestConstants;
import com.app.barbershopweb.workspace.repository.JdbcWorkspaceRepository;
import org.junit.jupiter.api.*;

import javax.sql.DataSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("jdbc workspace repository tests without error handling")
class JdbcWorkspaceRepositoryTest extends AbstractJdbcRepositoryTest {

    static JdbcWorkspaceRepository workspaceRepository;
    static JdbcBarbershopRepository barbershopRepository;
    static JdbcUsersRepository usersRepository;

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

    @BeforeEach
    void initFks() {
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY);
        usersRepository.addUser(utc.VALID_USER_ENTITY);
    }

    @Test
    void addWorkspace() {
        Long workspaceId = workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY);

        assertTrue(workspaceId > 0);
    }

    @Test
    void findWorkspaceById() {
        Long id = workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY);
        Optional<Workspace> workspaceOptional = workspaceRepository.findWorkspaceById(id);

        assertTrue(workspaceOptional.isPresent());
        assertEquals(wtc.VALID_WORKSPACE_ENTITY, workspaceOptional.get());
    }

    @Test
    void findWorkspaceByNotExistingId() {
        Optional<Workspace> workspaceOptional = workspaceRepository.findWorkspaceById(
                wtc.NOT_EXISTED_WORKSPACE_ID
        );

        assertTrue(workspaceOptional.isEmpty());
    }

    @Test
    void workspaceExistsByBarbershopIdAndUserId() {

        assertFalse(workspaceRepository.workspaceExistsByBarbershopIdAndUserId(
                btc.VALID_BARBERSHOP_ID, utc.VALID_USER_ID
                )
        );

        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY);

        assertTrue(workspaceRepository.workspaceExistsByBarbershopIdAndUserId(
                btc.VALID_BARBERSHOP_ID, utc.VALID_USER_ID
                )
        );
    }

    @Test
    void workspaceIsActiveByBarbershopIdAndUserId() {
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY);

        assertTrue(workspaceRepository.workspaceIsActiveByBarbershopIdAndUserId(
                btc.VALID_BARBERSHOP_ID, utc.VALID_USER_ID
                )
        );
    }

    @Test
    void updateNotExistingWorkspace() {
        Optional<Workspace> notFoundWorkspace = workspaceRepository.updateWorkspace(wtc.VALID_WORKSPACE_ENTITY);

        assertTrue(notFoundWorkspace.isEmpty());
    }

    @Test
    void updateExistingWorkspace() {

        usersRepository.addUser(utc.VALID_USER_ENTITY_LIST.get(1));
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY);
        Optional<Workspace> updWorkspaceOptional = workspaceRepository.updateWorkspace(
                wtc.VALID_UPDATED_WORKSPACE_ENTITY
        );

        assertTrue(updWorkspaceOptional.isPresent());
        assertEquals(wtc.VALID_UPDATED_WORKSPACE_ENTITY, updWorkspaceOptional.get());
    }

    @Test
    void getWorkspaces() {
        assertTrue(workspaceRepository.getWorkspaces().isEmpty());

        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY_LIST.get(1));
        barbershopRepository.addBarbershop(btc.VALID_BARBERSHOP_ENTITY_LIST.get(2));
        usersRepository.addUser(utc.VALID_USER_ENTITY_LIST.get(1));
        usersRepository.addUser(utc.VALID_USER_ENTITY_LIST.get(2));

        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY_LIST.get(0));
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY_LIST.get(1));
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY_LIST.get(2));

        assertEquals(
                wtc.VALID_WORKSPACE_ENTITY_LIST.size(),
                workspaceRepository.getWorkspaces().size()
        );
        assertEquals(wtc.VALID_WORKSPACE_ENTITY_LIST, workspaceRepository.getWorkspaces());
    }

    @Test
    void deleteWorkspaceById() {
        Long id = workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY);
        workspaceRepository.deleteWorkspaceById(id);

        assertTrue(workspaceRepository.getWorkspaces().isEmpty());
    }

    @Test
    void truncateAndRestartSequences() {
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY);
        workspaceRepository.truncateAndRestartSequence();

        assertTrue(workspaceRepository.getWorkspaces().isEmpty());

        Long id = workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY);

        assertEquals(1, id);
    }

    @AfterEach
    void cleanUpDb() {
        usersRepository.truncateAndRestartSequence();
        barbershopRepository.truncateAndRestartSequence();
        workspaceRepository.truncateAndRestartSequence();
    }
}