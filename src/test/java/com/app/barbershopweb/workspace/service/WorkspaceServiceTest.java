package com.app.barbershopweb.workspace.service;

import com.app.barbershopweb.workspace.Workspace;
import com.app.barbershopweb.workspace.WorkspaceService;
import com.app.barbershopweb.workspace.repository.WorkspaceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.app.barbershopweb.workspace.constants.WorkspaceEntity__TestConstants.WORKSPACE_VALID_ENTITY;
import static com.app.barbershopweb.workspace.constants.WorkspaceEntity__TestConstants.WORKSPACE_VALID_UPDATED_ENTITY;
import static com.app.barbershopweb.workspace.constants.WorkspaceList__TestConstants.WORKSPACE_VALID_ENTITY_LIST;
import static com.app.barbershopweb.workspace.constants.WorkspaceMetadata__TestConstants.WORKSPACE_VALID_WORKSPACE_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkspaceServiceTest {

    @Mock
    WorkspaceRepository workspaceRepository;

    @InjectMocks
    WorkspaceService workspaceService;


    @Test
    void addWorkspace() {
        when(workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY))
                .thenReturn(WORKSPACE_VALID_WORKSPACE_ID);

        Long id = workspaceService.addWorkspace(WORKSPACE_VALID_ENTITY);

        assertEquals(WORKSPACE_VALID_WORKSPACE_ID, id);
    }

    @Test
    void deleteWorkspaceById() {
        workspaceService.deleteWorkspaceById(WORKSPACE_VALID_WORKSPACE_ID);

        verify(workspaceRepository, times(1))
                .deleteWorkspaceById(WORKSPACE_VALID_WORKSPACE_ID);
    }

    @Test
    void updateWorkspace() {
        when(workspaceRepository.updateWorkspace(WORKSPACE_VALID_UPDATED_ENTITY))
                .thenReturn(Optional.of(WORKSPACE_VALID_UPDATED_ENTITY));

        Optional<Workspace> workspaceUpdOptional = workspaceService
                .updateWorkspace(WORKSPACE_VALID_UPDATED_ENTITY);

        assertTrue(workspaceUpdOptional.isPresent());
        assertEquals(WORKSPACE_VALID_UPDATED_ENTITY, workspaceUpdOptional.get());

    }

    @Test
    void findWorkspaceById() {
        when(workspaceRepository.findWorkspaceById((WORKSPACE_VALID_WORKSPACE_ID)))
                .thenReturn(Optional.of(WORKSPACE_VALID_ENTITY));

        Optional<Workspace> foundWorkspaceOpt = workspaceService
                .findWorkspaceById(WORKSPACE_VALID_WORKSPACE_ID);

        assertTrue(foundWorkspaceOpt.isPresent());
        assertEquals(WORKSPACE_VALID_ENTITY, foundWorkspaceOpt.get());

    }

    @Test
    void getWorkspaces() {

        when(workspaceRepository.getWorkspaces())
                .thenReturn(WORKSPACE_VALID_ENTITY_LIST);

        List<Workspace> workspaces = workspaceService.getWorkspaces();

        assertEquals(WORKSPACE_VALID_ENTITY_LIST.size(), workspaces.size());
        assertEquals(WORKSPACE_VALID_ENTITY_LIST, workspaces);
    }
}