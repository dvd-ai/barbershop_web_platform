package com.app.barbershopweb.workspace.repository;

import com.app.barbershopweb.workspace.Workspace;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkspaceRepository {
    Long addWorkspace(Workspace workspace);
    Optional<Workspace> findWorkspaceById(Long id);
    boolean workspaceExistsByBarbershopIdAndUserId(Long barbershopId, Long userId);
    boolean workspaceIsActiveByBarbershopIdAndUserId(Long barbershopId, Long userId);
    Optional<Workspace>updateWorkspace(Workspace workspace);
    List<Workspace> getWorkspaces();
    void deleteWorkspaceById(Long id);
    void truncateAndRestartSequences();

}
