package com.app.barbershopweb.workspace;

import com.app.barbershopweb.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkspaceService {
    
    public final WorkspaceRepository workspaceRepository;

    public WorkspaceService(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    public Long addWorkspace(Workspace workspace) {
        return workspaceRepository.addWorkspace(workspace);
    }

    public void deleteWorkspaceById(Long id) {
        workspaceRepository.deleteWorkspaceById(id);
    }

    public Optional<Workspace> updateWorkspace(Workspace workspace) {
        return workspaceRepository.updateWorkspace(workspace);
    }

    public Optional<Workspace> findWorkspaceById(Long id) {
        return workspaceRepository.findWorkspaceById(id);
    }

    public List<Workspace> getWorkspaces() {
        return workspaceRepository.getWorkspaces();
    }
}
