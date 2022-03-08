package com.app.barbershopweb.workspace.controller;

import com.app.barbershopweb.exception.DbUniqueConstraintsViolationException;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.workspace.WorkspaceController;
import com.app.barbershopweb.workspace.WorkspaceConverter;
import com.app.barbershopweb.workspace.WorkspaceService;
import com.app.barbershopweb.workspace.WorkspaceTestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static com.app.barbershopweb.workspace.WorkspaceTestConstants.WORKSPACES_URL;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkspaceController.class)
@DisplayName("Testing PUT: " + WORKSPACES_URL)
class WorkspaceControllerUpdateWorkspaceTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    WorkspaceConverter workspaceConverter;

    @MockBean
    WorkspaceService workspaceService;

    @Autowired
    ObjectMapper objectMapper;

    WorkspaceTestConstants wtc = new WorkspaceTestConstants();


    @DisplayName("when workspace dto isn't valid " +
            "returns status code 400 & error dto")
    @Test
    void whenWorkspaceDtoNotValid() throws Exception {


        String json = objectMapper.writeValueAsString(
                wtc.INVALID_WORKSPACE_DTO
        );

        mockMvc
                .perform(put(WORKSPACES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors", hasItem(wtc.DTO_CV_USER_ID_ERR_MSG)))
                .andExpect(jsonPath("$.errors", hasItem(wtc.DTO_CV_BARBERSHOP_ID_ERR_MSG)))
                .andExpect(jsonPath("$.errors", hasItem(wtc.DTO_CV_WORKSPACE_ID_ERR_MSG)));
    }


    @Test
    @DisplayName("when entity with 'id' in workspaceDto doesn't exist " +
            "returns status code 404 and error dto")
    void whenNotFoundWorkspaceId() throws Exception {
        String json = objectMapper.writeValueAsString(
                wtc.WORKSPACE_DTO_NOT_EXISTED_ID
        );

        when(workspaceConverter.mapToEntity(any())).thenReturn(
                wtc.WORKSPACE_ENTITY_NOT_EXISTED_ID
        );
        when(workspaceService.updateWorkspace(any())).thenReturn(Optional.empty());

        mockMvc
                .perform(put(WORKSPACES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(
                        "Workspace with id '" + wtc.NOT_EXISTED_WORKSPACE_ID + "' not found."
                )))
                .andExpect(jsonPath("$", aMapWithSize(1)));
    }

    @DisplayName("when workspace dto violates db fk constraints " +
            "returns status code 404 & error dto")
    @Test
    void whenWorkspaceDtoViolatesDbFkConstraints() throws Exception {
        String json = objectMapper.writeValueAsString(
                wtc.VALID_WORKSPACE_DTO
        );

        when(workspaceConverter.mapToEntity(any())).thenReturn(
                wtc.VALID_WORKSPACE_ENTITY);

        when(workspaceService.updateWorkspace(any())).thenThrow(
                new NotFoundException(
                        List.of(
                                wtc.DTO_FK_CV_USER_ID_ERR_MSG,
                                wtc.DTO_FK_CV_BARBERSHOP_ID_ERR_MSG
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
                .andExpect(jsonPath("$.errors", hasItem(wtc.DTO_FK_CV_USER_ID_ERR_MSG)))
                .andExpect(jsonPath("$.errors", hasItem(wtc.DTO_FK_CV_BARBERSHOP_ID_ERR_MSG)));
    }

    @DisplayName("when workspace dto violates db uk constraints " +
            "returns status code 400 & error dto")
    @Test
    void whenWorkspaceDtoViolatesDbUkConstraints() throws Exception {
        String json = objectMapper.writeValueAsString(
                wtc.VALID_WORKSPACE_DTO
        );

        when(workspaceConverter.mapToEntity(any())).thenReturn(
                wtc.VALID_WORKSPACE_ENTITY);

        when(workspaceService.updateWorkspace(any())).thenThrow(
                new DbUniqueConstraintsViolationException(
                        wtc.DTO_UK_CV_ERR_MSG
                )
        );

        mockMvc
                .perform(put(WORKSPACES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(wtc.DTO_UK_CV_ERR_MSG)));
    }

    @Test
    @DisplayName("should return updated workspace (dto)")
    void shouldReturnUpdatedWorkspace() throws Exception {
        String json = objectMapper.writeValueAsString(
                wtc.VALID_WORKSPACE_DTO
        );

        when(workspaceConverter.mapToEntity(any())).thenReturn(
                wtc.VALID_WORKSPACE_ENTITY
        );
        when(workspaceService.updateWorkspace(any())).thenReturn(
                Optional.of(wtc.VALID_WORKSPACE_ENTITY)
        );
        when(workspaceConverter.mapToDto(any())).thenReturn(
                wtc.VALID_WORKSPACE_DTO
        );

        mockMvc
                .perform(put(WORKSPACES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.barbershopId",  is(wtc.VALID_WORKSPACE_DTO.barbershopId().intValue())))
                .andExpect(jsonPath("$.userId", is(wtc.VALID_WORKSPACE_DTO.userId().intValue())))
                .andExpect(jsonPath("$.workspaceId", is(wtc.VALID_WORKSPACE_DTO.workspaceId().intValue())))
                .andExpect(jsonPath("$.active", is(wtc.VALID_WORKSPACE_DTO.active())));
    }
}
