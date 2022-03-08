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

import static com.app.barbershopweb.workspace.WorkspaceTestConstants.WORKSPACES_URL;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WorkspaceController.class)
@DisplayName("Testing POST: " + WORKSPACES_URL)
class WorkspaceControllerAddWorkspaceTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WorkspaceService workspaceService;

    @MockBean
    WorkspaceConverter workspaceConverter;

    @Autowired
    ObjectMapper objectMapper;

    private final WorkspaceTestConstants wtc = new WorkspaceTestConstants();

    @DisplayName("when workspace dto isn't valid " +
            "returns status code 400 & error dto")
    @Test
    void whenWorkspaceDtoNotValid() throws Exception {
        String json = objectMapper.writeValueAsString(
                wtc.INVALID_WORKSPACE_DTO
        );

        mockMvc
                .perform(post(WORKSPACES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors", hasItem(wtc.DTO_CV_BARBERSHOP_ID_ERR_MSG)))
                .andExpect(jsonPath("$.errors", hasItem(wtc.DTO_CV_WORKSPACE_ID_ERR_MSG)))
                .andExpect(jsonPath("$.errors", hasItem(wtc.DTO_CV_USER_ID_ERR_MSG)));
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

        when(workspaceService.addWorkspace(any())).thenThrow(
                new NotFoundException(
                        List.of(
                                wtc.DTO_FK_CV_USER_ID_ERR_MSG,
                                wtc.DTO_FK_CV_BARBERSHOP_ID_ERR_MSG
                        )
                )
        );

        mockMvc
                .perform(post(WORKSPACES_URL)
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

        when(workspaceService.addWorkspace(any())).thenThrow(
                new DbUniqueConstraintsViolationException(
                        wtc.DTO_UK_CV_ERR_MSG
                )
        );

        mockMvc
                .perform(post(WORKSPACES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(wtc.DTO_UK_CV_ERR_MSG)));
    }




    @DisplayName("after saving workspace entity returns its id and status code 201")
    @Test
    void shouldAddWorkspace() throws Exception {
        String json = objectMapper.writeValueAsString(
                wtc.VALID_WORKSPACE_DTO
        );

        when(workspaceConverter.mapToEntity(any())).thenReturn(
                wtc.VALID_WORKSPACE_ENTITY
        );
        when(workspaceService.addWorkspace(any())).thenReturn(
                wtc.VALID_WORKSPACE_ID
        );

        mockMvc
                .perform(post(WORKSPACES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(String.valueOf(wtc.VALID_WORKSPACE_ID)));
    }
}
