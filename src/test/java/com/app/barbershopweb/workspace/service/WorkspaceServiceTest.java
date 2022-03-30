package com.app.barbershopweb.workspace.service;

import com.app.barbershopweb.barbershop.Barbershop;
import com.app.barbershopweb.workspace.Workspace;
import com.app.barbershopweb.workspace.WorkspaceService;
import com.app.barbershopweb.workspace.WorkspaceTestConstants;
import com.app.barbershopweb.workspace.repository.WorkspaceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkspaceServiceTest {

    @Mock
    WorkspaceRepository workspaceRepository;
    
    @InjectMocks
    WorkspaceService workspaceService;
    
    WorkspaceTestConstants wtc = new WorkspaceTestConstants();
    
    @Test
    void addWorkspace() {
        when(workspaceRepository.addWorkspace(any()))
                .thenReturn(wtc.VALID_WORKSPACE_ID);

        Long id = workspaceService.addWorkspace(wtc.VALID_WORKSPACE_ENTITY);
        
        assertEquals(wtc.VALID_WORKSPACE_ID, id);
    }

    @Test
    void deleteWorkspaceById() {
        workspaceService.deleteWorkspaceById(wtc.VALID_WORKSPACE_ID);
        
        verify(workspaceRepository, times(1))
                .deleteWorkspaceById(any());
    }

    @Test
    void updateWorkspace() {
        when(workspaceRepository.updateWorkspace(any()))
                .thenReturn(Optional.of(wtc.VALID_UPDATED_WORKSPACE_ENTITY));

        Optional<Workspace> workspaceUpdOptional = workspaceService
                .updateWorkspace(wtc.VALID_UPDATED_WORKSPACE_ENTITY);

        assertTrue(workspaceUpdOptional.isPresent());
        assertEquals(wtc.VALID_UPDATED_WORKSPACE_ENTITY, workspaceUpdOptional.get());
        
    }

    @Test
    void findWorkspaceById() {
        when(workspaceRepository.findWorkspaceById((any())))
                .thenReturn(Optional.of(wtc.VALID_WORKSPACE_ENTITY));

        Optional<Workspace> foundWorkspaceOpt = workspaceService
                .findWorkspaceById(wtc.VALID_WORKSPACE_ID);

        assertTrue(foundWorkspaceOpt.isPresent());
        assertEquals(wtc.VALID_WORKSPACE_ENTITY, foundWorkspaceOpt.get());

    }

    @Test
    void getWorkspaces() {

        when(workspaceRepository.getWorkspaces())
                .thenReturn(wtc.VALID_WORKSPACE_ENTITY_LIST);

        List<Workspace> workspaces = workspaceService.getWorkspaces();

        assertEquals(wtc.VALID_WORKSPACE_ENTITY_LIST.size(), workspaces.size());
        assertEquals(wtc.VALID_WORKSPACE_ENTITY_LIST, workspaces);
    }
}