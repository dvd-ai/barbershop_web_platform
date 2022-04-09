package com.app.barbershopweb.workspace.converter;

import com.app.barbershopweb.workspace.Workspace;
import com.app.barbershopweb.workspace.WorkspaceConverter;
import com.app.barbershopweb.workspace.WorkspaceDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.app.barbershopweb.workspace.constants.WorkspaceDto__TestConstants.WORKSPACE_VALID_DTO;
import static com.app.barbershopweb.workspace.constants.WorkspaceEntity__TestConstants.WORKSPACE_VALID_ENTITY;
import static com.app.barbershopweb.workspace.constants.WorkspaceList__TestConstants.WORKSPACE_VALID_DTO_LIST;
import static com.app.barbershopweb.workspace.constants.WorkspaceList__TestConstants.WORKSPACE_VALID_ENTITY_LIST;
import static com.app.barbershopweb.workspace.constants.WorkspaceMetadata__TestConstants.WORKSPACE_FIELD_AMOUNT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WorkspaceConverterTest {

    private final WorkspaceConverter workspaceConverter = new WorkspaceConverter();


    @Test
    void workspaceDtoListToEntityList() {

        List<Workspace> workspaces = WORKSPACE_VALID_DTO_LIST.stream()
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
                        WORKSPACE_VALID_DTO_LIST
                )
        );
    }

    @Test
    void workspaceEntityListToDtoList() {
        List<WorkspaceDto> dtos = WORKSPACE_VALID_ENTITY_LIST.stream()
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
                        WORKSPACE_VALID_ENTITY_LIST
                )
        );
    }

    @Test
    void mapToEntity() {
        assertEquals(
                WORKSPACE_VALID_ENTITY,
                workspaceConverter.mapToEntity(WORKSPACE_VALID_DTO)
        );
    }

    @Test
    void mapToDto() {
        assertEquals(
                WORKSPACE_VALID_DTO,
                workspaceConverter.mapToDto(WORKSPACE_VALID_ENTITY)
        );
    }

    @Test
    void actualFieldAmount() {
        assertEquals(Workspace.class.getDeclaredFields().length, WORKSPACE_FIELD_AMOUNT);
    }
}
