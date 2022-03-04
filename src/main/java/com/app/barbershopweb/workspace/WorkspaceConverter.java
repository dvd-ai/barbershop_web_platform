package com.app.barbershopweb.workspace;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkspaceConverter {

    public List<Workspace> workspaceDtoListToEntityList(List<WorkspaceDto> dtos) {
        return dtos.stream()
                .map(this::mapToEntity)
                .toList();
    }

    public List<WorkspaceDto> workspaceEntityListToDtoList(List<Workspace> entities) {
        return entities.stream()
                .map(this::mapToDto)
                .toList();
    }

    public Workspace mapToEntity(WorkspaceDto dto) {
        return new Workspace(
                dto.workspaceId(),
                dto.userId(),
                dto.barbershopId(),
                dto.active()
        );
    }

    public WorkspaceDto mapToDto(Workspace entity) {
        return new WorkspaceDto(
                entity.getWorkspaceId(),
                entity.getUserId(),
                entity.getBarbershopId(),
                entity.getActive()
        );
    }
}
