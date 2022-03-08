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

import static com.app.barbershopweb.workspace.WorkspaceTestConstants.WORKSPACES_URL;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkspaceController.class)
@DisplayName("Testing DELETE: " + WORKSPACES_URL + "{workspaceId}")
class WorkspaceControllerDeleteByIdTest {

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
                .perform(delete(WORKSPACES_URL + "/" + wtc.INVALID_WORKSPACE_ID))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(wtc.PV_WORKSPACE_ID_ERR_MSG)));
    }

    @DisplayName("returns empty body, status code 200, " +
            "when: workspace with existing / not existing id was deleted")
    @Test
    void shouldDeleteWorkspaceById() throws Exception {
        mockMvc
                .perform(delete(WORKSPACES_URL + "/" + wtc.VALID_WORKSPACE_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }

}
