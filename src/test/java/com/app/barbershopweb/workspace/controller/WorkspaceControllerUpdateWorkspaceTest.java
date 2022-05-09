package com.app.barbershopweb.workspace.controller;

import com.app.barbershopweb.exception.DbUniqueConstraintsViolationException;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.workspace.WorkspaceController;
import com.app.barbershopweb.workspace.WorkspaceConverter;
import com.app.barbershopweb.workspace.WorkspaceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static com.app.barbershopweb.workspace.constants.WorkspaceDto__TestConstants.WORKSPACE_NOT_EXISTED_ID_DTO;
import static com.app.barbershopweb.workspace.constants.WorkspaceDto__TestConstants.WORKSPACE_VALID_DTO;
import static com.app.barbershopweb.workspace.constants.WorkspaceEntity__TestConstants.WORKSPACE_NOT_EXISTED_ID_ENTITY;
import static com.app.barbershopweb.workspace.constants.WorkspaceEntity__TestConstants.WORKSPACE_VALID_ENTITY;
import static com.app.barbershopweb.workspace.constants.WorkspaceMetadata__TestConstants.WORKSPACES_URL;
import static com.app.barbershopweb.workspace.constants.WorkspaceMetadata__TestConstants.WORKSPACE_NOT_EXISTED_WORKSPACE_ID;
import static com.app.barbershopweb.workspace.constants.error.WorkspaceErrorMessage_Fk__TestConstants.WORKSPACE_ERR_FK_BARBERSHOP_ID;
import static com.app.barbershopweb.workspace.constants.error.WorkspaceErrorMessage_Fk__TestConstants.WORKSPACE_ERR_FK_USER_ID;
import static com.app.barbershopweb.workspace.constants.error.WorkspaceErrorMessage_Uk__TestConstants.WORKSPACE_ERR_UK_USER_ID__BARBERSHOP_ID;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkspaceController.class)
@DisplayName("Testing PUT: " + WORKSPACES_URL)
@ExtendWith(MockitoExtension.class)
@MockBean(AuthenticationProvider.class)
class WorkspaceControllerUpdateWorkspaceTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    WorkspaceConverter workspaceConverter;

    @MockBean
    WorkspaceService workspaceService;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @DisplayName("when entity with 'id' in workspaceDto doesn't exist " +
            "returns status code 404 and error dto")
    void whenNotFoundWorkspaceId() throws Exception {
        String json = objectMapper.writeValueAsString(
                WORKSPACE_NOT_EXISTED_ID_DTO
        );

        when(workspaceConverter.mapToEntity(WORKSPACE_NOT_EXISTED_ID_DTO)).thenReturn(
                WORKSPACE_NOT_EXISTED_ID_ENTITY
        );
        when(workspaceService.updateWorkspace(WORKSPACE_NOT_EXISTED_ID_ENTITY)).thenReturn(Optional.empty());

        mockMvc
                .perform(put(WORKSPACES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(
                        "Workspace with id '" + WORKSPACE_NOT_EXISTED_WORKSPACE_ID + "' not found."
                )))
                .andExpect(jsonPath("$", aMapWithSize(1)));
    }

    @DisplayName("when workspace dto violates db fk constraints " +
            "returns status code 404 & error dto")
    @Test
    void whenWorkspaceDtoViolatesDbFkConstraints() throws Exception {
        String json = objectMapper.writeValueAsString(
                WORKSPACE_VALID_DTO
        );

        when(workspaceConverter.mapToEntity(WORKSPACE_VALID_DTO)).thenReturn(
                WORKSPACE_VALID_ENTITY);

        when(workspaceService.updateWorkspace(WORKSPACE_VALID_ENTITY)).thenThrow(
                new NotFoundException(
                        List.of(
                                WORKSPACE_ERR_FK_USER_ID,
                                WORKSPACE_ERR_FK_BARBERSHOP_ID
                        )
                )
        );

        mockMvc
                .perform(put(WORKSPACES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors", hasItem(WORKSPACE_ERR_FK_USER_ID)))
                .andExpect(jsonPath("$.errors", hasItem(WORKSPACE_ERR_FK_BARBERSHOP_ID)));
    }

    @DisplayName("when workspace dto violates db uk constraints " +
            "returns status code 400 & error dto")
    @Test
    void whenWorkspaceDtoViolatesDbUkConstraints() throws Exception {
        String json = objectMapper.writeValueAsString(
                WORKSPACE_VALID_DTO
        );

        when(workspaceConverter.mapToEntity(WORKSPACE_VALID_DTO)).thenReturn(
                WORKSPACE_VALID_ENTITY);

        when(workspaceService.updateWorkspace(WORKSPACE_VALID_ENTITY)).thenThrow(
                new DbUniqueConstraintsViolationException(
                        List.of(WORKSPACE_ERR_UK_USER_ID__BARBERSHOP_ID)
                )
        );

        mockMvc
                .perform(put(WORKSPACES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(WORKSPACE_ERR_UK_USER_ID__BARBERSHOP_ID)));
    }

    @Test
    @DisplayName("should return updated workspace (dto)")
    void shouldReturnUpdatedWorkspace() throws Exception {
        String json = objectMapper.writeValueAsString(
                WORKSPACE_VALID_DTO
        );

        when(workspaceConverter.mapToEntity(WORKSPACE_VALID_DTO)).thenReturn(
                WORKSPACE_VALID_ENTITY
        );
        when(workspaceService.updateWorkspace(WORKSPACE_VALID_ENTITY)).thenReturn(
                Optional.of(WORKSPACE_VALID_ENTITY)
        );
        when(workspaceConverter.mapToDto(WORKSPACE_VALID_ENTITY)).thenReturn(
                WORKSPACE_VALID_DTO
        );

        mockMvc
                .perform(put(WORKSPACES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.barbershopId", is(WORKSPACE_VALID_DTO.barbershopId().intValue())))
                .andExpect(jsonPath("$.userId", is(WORKSPACE_VALID_DTO.userId().intValue())))
                .andExpect(jsonPath("$.workspaceId", is(WORKSPACE_VALID_DTO.workspaceId().intValue())))
                .andExpect(jsonPath("$.active", is(WORKSPACE_VALID_DTO.active())));
    }
}
