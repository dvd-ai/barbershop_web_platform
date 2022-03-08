package com.app.barbershopweb.workspace.controller;

import com.app.barbershopweb.workspace.WorkspaceController;
import com.app.barbershopweb.workspace.WorkspaceConverter;
import com.app.barbershopweb.workspace.WorkspaceService;
import com.app.barbershopweb.workspace.WorkspaceTestConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.app.barbershopweb.workspace.WorkspaceTestConstants.WORKSPACES_URL;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkspaceController.class)
@DisplayName("Testing GET: " + WORKSPACES_URL + "/" + "{workspaceId}")
class WorkspaceControllerGetWorkspaceByIdTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WorkspaceService workspaceService;

    @MockBean
    WorkspaceConverter workspaceConverter;

    WorkspaceTestConstants wtc = new WorkspaceTestConstants();

    @DisplayName("When path variable input 'workspaceId' isn't valid" +
            " returns status code 400 (BAD_REQUEST) & error dto")
    @Test
    void whenWorkspaceIdNotValid() throws Exception {
        mockMvc
                .perform(get(WORKSPACES_URL + "/" + wtc.INVALID_WORKSPACE_ID))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(wtc.PV_WORKSPACE_ID_ERR_MSG)));
    }

    @DisplayName("when there's no workspace with id 'workspaceId' " +
            "returns status code 404 & error dto")
    @Test
    void whenNotExistedWorkspaceId() throws Exception {
        mockMvc
                .perform(get(WORKSPACES_URL + "/" + wtc.NOT_EXISTED_WORKSPACE_ID))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(
                        "Workspace with id '" + wtc.NOT_EXISTED_WORKSPACE_ID + "' not found.")
                ));
    }

    @DisplayName("returns corresponding workspace dto")
    @Test
    void shouldReturnWorkspace() throws Exception {
        when(workspaceService.findWorkspaceById(any())).thenReturn(
                Optional.of(wtc.VALID_WORKSPACE_ENTITY)
        );
        when(workspaceConverter.mapToDto(any())).thenReturn(
                wtc.VALID_WORKSPACE_DTO
        );

        mockMvc
                .perform(get(WORKSPACES_URL+ "/" + wtc.VALID_WORKSPACE_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.barbershopId",  is(wtc.VALID_WORKSPACE_DTO.barbershopId().intValue())))
                .andExpect(jsonPath("$.workspaceId", is(wtc.VALID_WORKSPACE_DTO.workspaceId().intValue())))
                .andExpect(jsonPath("$.userId", is(wtc.VALID_WORKSPACE_DTO.userId().intValue())))
                .andExpect(jsonPath("$.active", is(wtc.VALID_WORKSPACE_DTO.active())))

                .andExpect(jsonPath("$", aMapWithSize(wtc.WORKSPACE_FIELD_AMOUNT)));
    }
}
