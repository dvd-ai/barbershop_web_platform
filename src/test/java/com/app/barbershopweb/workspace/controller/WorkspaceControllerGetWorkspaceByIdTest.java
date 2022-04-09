package com.app.barbershopweb.workspace.controller;

import com.app.barbershopweb.workspace.WorkspaceController;
import com.app.barbershopweb.workspace.WorkspaceConverter;
import com.app.barbershopweb.workspace.WorkspaceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.app.barbershopweb.workspace.constants.WorkspaceDto__TestConstants.WORKSPACE_VALID_DTO;
import static com.app.barbershopweb.workspace.constants.WorkspaceEntity__TestConstants.WORKSPACE_VALID_ENTITY;
import static com.app.barbershopweb.workspace.constants.WorkspaceMetadata__TestConstants.*;
import static com.app.barbershopweb.workspace.constants.error.WorkspaceErrorMessage_PathVar__TestConstants.WORKSPACE_ERR_INVALID_PATH_VAR_WORKSPACE_ID;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkspaceController.class)
@DisplayName("Testing GET: " + WORKSPACES_URL + "/" + "{workspaceId}")
@ExtendWith(MockitoExtension.class)
class WorkspaceControllerGetWorkspaceByIdTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WorkspaceService workspaceService;

    @MockBean
    WorkspaceConverter workspaceConverter;


    @DisplayName("When path variable input 'workspaceId' isn't valid" +
            " returns status code 400 (BAD_REQUEST) & error dto")
    @Test
    void whenWorkspaceIdNotValid() throws Exception {
        mockMvc
                .perform(get(WORKSPACES_URL + "/" + WORKSPACE_INVALID_WORKSPACE_ID))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(WORKSPACE_ERR_INVALID_PATH_VAR_WORKSPACE_ID)));
    }

    @DisplayName("when there's no workspace with id 'workspaceId' " +
            "returns status code 404 & error dto")
    @Test
    void whenNotExistedWorkspaceId() throws Exception {
        mockMvc
                .perform(get(WORKSPACES_URL + "/" + WORKSPACE_NOT_EXISTED_WORKSPACE_ID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(
                        "Workspace with id '" + WORKSPACE_NOT_EXISTED_WORKSPACE_ID + "' not found.")
                ));
    }

    @DisplayName("returns corresponding workspace dto")
    @Test
    void shouldReturnWorkspace() throws Exception {
        when(workspaceService.findWorkspaceById(WORKSPACE_VALID_WORKSPACE_ID)).thenReturn(
                Optional.of(WORKSPACE_VALID_ENTITY)
        );
        when(workspaceConverter.mapToDto(WORKSPACE_VALID_ENTITY)).thenReturn(
                WORKSPACE_VALID_DTO
        );

        mockMvc
                .perform(get(WORKSPACES_URL + "/" + WORKSPACE_VALID_WORKSPACE_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.barbershopId", is(WORKSPACE_VALID_DTO.barbershopId().intValue())))
                .andExpect(jsonPath("$.workspaceId", is(WORKSPACE_VALID_DTO.workspaceId().intValue())))
                .andExpect(jsonPath("$.userId", is(WORKSPACE_VALID_DTO.userId().intValue())))
                .andExpect(jsonPath("$.active", is(WORKSPACE_VALID_DTO.active())))

                .andExpect(jsonPath("$", aMapWithSize(WORKSPACE_FIELD_AMOUNT)));
    }
}
