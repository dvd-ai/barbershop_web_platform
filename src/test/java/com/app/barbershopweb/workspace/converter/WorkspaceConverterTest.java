package com.app.barbershopweb.workspace.converter;

import com.app.barbershopweb.workspace.Workspace;
import com.app.barbershopweb.workspace.WorkspaceConverter;
import com.app.barbershopweb.workspace.WorkspaceDto;
import com.app.barbershopweb.workspace.WorkspaceTestConstants;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WorkspaceConverterTest {

    private final WorkspaceConverter workspaceConverter = new WorkspaceConverter();
    private final WorkspaceTestConstants wtc = new WorkspaceTestConstants();

    @Test
    void workspaceDtoListToEntityList() {

        List<Workspace> workspaces = wtc.VALID_WORKSPACE_DTO_LIST.stream()
                .map(dto -> new Workspace(
                        dto.workspaceId(),
                        dto.userId(),
                        dto.barbershopId(),
                        dto.active()
                ))
                .toList();

        assertEquals(
                workspaces,
                workspaceConverter.workspaceDtoListToEntityList(
                        wtc.VALID_WORKSPACE_DTO_LIST
                )
        );
    }

    @Test
    void workspaceEntityListToDtoList() {
        List<WorkspaceDto> dtos = wtc.VALID_WORKSPACE_ENTITY_LIST.stream()
                .map(entity -> new WorkspaceDto(
                        entity.getWorkspaceId(),
                        entity.getUserId(),
                        entity.getBarbershopId(),
                        entity.getActive()
                ))
                .toList();

        assertEquals(
                dtos,
                workspaceConverter.workspaceEntityListToDtoList(
                        wtc.VALID_WORKSPACE_ENTITY_LIST
                )
        );
    }

    @Test
    void mapToEntity() {
        assertEquals(
                wtc.VALID_WORKSPACE_ENTITY,
                workspaceConverter.mapToEntity(wtc.VALID_WORKSPACE_DTO)
        );
    }

    @Test
    void mapToDto() {
        assertEquals(
                wtc.VALID_WORKSPACE_DTO,
                workspaceConverter.mapToDto(wtc.VALID_WORKSPACE_ENTITY)
        );
    }

    @Test
    void actualFieldAmount() {
        assertEquals(Workspace.class.getDeclaredFields().length, wtc.WORKSPACE_FIELD_AMOUNT);
    }
}
