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

import java.util.Collections;

import static com.app.barbershopweb.workspace.WorkspaceTestConstants.WORKSPACES_URL;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(WorkspaceController.class)
@DisplayName("Testing GET: " + WORKSPACES_URL)
class WorkspaceControllerGetWorkspacesTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WorkspaceService workspaceService;

    @MockBean
    WorkspaceConverter converter;

    private final WorkspaceTestConstants wtc = new WorkspaceTestConstants();

    @DisplayName("gives empty Workspace List when they're no added workspaces yet")
    @Test
    void shouldReturnEmptyWorkspaceList() throws Exception {

        when(workspaceService.getWorkspaces()).thenReturn(Collections.emptyList());
        when(converter.workspaceEntityListToDtoList(any())).thenReturn(Collections.emptyList());

        mockMvc.
                perform(get(WORKSPACES_URL)).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(content().string("[]"));
    }

    @DisplayName("gives all workspaces at once")
    @Test
    void shouldReturnAllWorkspaces() throws Exception {
        when(workspaceService.getWorkspaces()).thenReturn(
                wtc.VALID_WORKSPACE_ENTITY_LIST
        );
        when(converter.workspaceEntityListToDtoList(any())).thenReturn(
                wtc.VALID_WORKSPACE_DTO_LIST
        );

        mockMvc.
                perform(get(WORKSPACES_URL)).
                andDo(print()).
                andExpect(status().isOk()).

                andExpect(jsonPath("$[0].workspaceId", is(wtc.VALID_WORKSPACE_DTO_LIST.get(0).workspaceId().intValue()))).
                andExpect(jsonPath("$[0].userId", is(wtc.VALID_WORKSPACE_DTO_LIST.get(0).userId().intValue()))).
                andExpect(jsonPath("$[0].barbershopId", is(wtc.VALID_WORKSPACE_DTO_LIST.get(0).workspaceId().intValue()))).
                andExpect(jsonPath("$[0].active", is(wtc.VALID_WORKSPACE_DTO_LIST.get(0).active()))).

                andExpect(jsonPath("$[1].workspaceId", is(wtc.VALID_WORKSPACE_DTO_LIST.get(1).workspaceId().intValue()))).
                andExpect(jsonPath("$[1].userId", is(wtc.VALID_WORKSPACE_DTO_LIST.get(1).userId().intValue()))).
                andExpect(jsonPath("$[1].barbershopId", is(wtc.VALID_WORKSPACE_DTO_LIST.get(1).workspaceId().intValue()))).
                andExpect(jsonPath("$[1].active", is(wtc.VALID_WORKSPACE_DTO_LIST.get(1).active()))).

                andExpect(jsonPath("$[2].workspaceId", is(wtc.VALID_WORKSPACE_DTO_LIST.get(2).workspaceId().intValue()))).
                andExpect(jsonPath("$[2].userId", is(wtc.VALID_WORKSPACE_DTO_LIST.get(2).userId().intValue()))).
                andExpect(jsonPath("$[2].barbershopId", is(wtc.VALID_WORKSPACE_DTO_LIST.get(2).workspaceId().intValue()))).
                andExpect(jsonPath("$[2].active", is(wtc.VALID_WORKSPACE_DTO_LIST.get(2).active()))).

                andExpect(jsonPath("$", hasSize(3))).
                andExpect(jsonPath("$[0]", aMapWithSize(wtc.WORKSPACE_FIELD_AMOUNT))).
                andExpect(jsonPath("$[1]", aMapWithSize(wtc.WORKSPACE_FIELD_AMOUNT))).
                andExpect(jsonPath("$[2]", aMapWithSize(wtc.WORKSPACE_FIELD_AMOUNT)));

    }
}
