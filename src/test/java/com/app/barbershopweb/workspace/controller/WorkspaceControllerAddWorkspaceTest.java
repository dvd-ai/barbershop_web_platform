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

import static com.app.barbershopweb.workspace.constants.WorkspaceDto__TestConstants.WORKSPACE_VALID_DTO;
import static com.app.barbershopweb.workspace.constants.WorkspaceEntity__TestConstants.WORKSPACE_VALID_ENTITY;
import static com.app.barbershopweb.workspace.constants.WorkspaceMetadata__TestConstants.WORKSPACES_URL;
import static com.app.barbershopweb.workspace.constants.WorkspaceMetadata__TestConstants.WORKSPACE_VALID_WORKSPACE_ID;
import static com.app.barbershopweb.workspace.constants.error.WorkspaceErrorMessage_Fk__TestConstants.WORKSPACE_ERR_FK_BARBERSHOP_ID;
import static com.app.barbershopweb.workspace.constants.error.WorkspaceErrorMessage_Fk__TestConstants.WORKSPACE_ERR_FK_USER_ID;
import static com.app.barbershopweb.workspace.constants.error.WorkspaceErrorMessage_Uk__TestConstants.WORKSPACE_ERR_UK_USER_ID__BARBERSHOP_ID;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WorkspaceController.class)
@DisplayName("Testing POST: " + WORKSPACES_URL)
@ExtendWith(MockitoExtension.class)
@MockBean(AuthenticationProvider.class)
class WorkspaceControllerAddWorkspaceTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WorkspaceService workspaceService;

    @MockBean
    WorkspaceConverter workspaceConverter;

    @Autowired
    ObjectMapper objectMapper;


    @DisplayName("when workspace dto violates db fk constraints " +
            "returns status code 404 & error dto")
    @Test
    void whenWorkspaceDtoViolatesDbFkConstraints() throws Exception {
        String json = objectMapper.writeValueAsString(
                WORKSPACE_VALID_DTO
        );

        when(workspaceConverter.mapToEntity(WORKSPACE_VALID_DTO)).thenReturn(
                WORKSPACE_VALID_ENTITY);

        when(workspaceService.addWorkspace(WORKSPACE_VALID_ENTITY)).thenThrow(
                new NotFoundException(
                        List.of(
                                WORKSPACE_ERR_FK_USER_ID,
                                WORKSPACE_ERR_FK_BARBERSHOP_ID
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

        when(workspaceService.addWorkspace(WORKSPACE_VALID_ENTITY)).thenThrow(
                new DbUniqueConstraintsViolationException(
                        List.of(WORKSPACE_ERR_UK_USER_ID__BARBERSHOP_ID)
                )
        );

        mockMvc
                .perform(post(WORKSPACES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem(WORKSPACE_ERR_UK_USER_ID__BARBERSHOP_ID)));
    }


    @DisplayName("after saving workspace entity returns its id and status code 201")
    @Test
    void shouldAddWorkspace() throws Exception {
        String json = objectMapper.writeValueAsString(
                WORKSPACE_VALID_DTO
        );

        when(workspaceConverter.mapToEntity(WORKSPACE_VALID_DTO)).thenReturn(
                WORKSPACE_VALID_ENTITY
        );
        when(workspaceService.addWorkspace(WORKSPACE_VALID_ENTITY)).thenReturn(
                WORKSPACE_VALID_WORKSPACE_ID
        );

        mockMvc
                .perform(post(WORKSPACES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(String.valueOf(WORKSPACE_VALID_WORKSPACE_ID)));
    }
}
