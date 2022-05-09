package com.app.barbershopweb.workspace.controller;

import com.app.barbershopweb.workspace.WorkspaceController;
import com.app.barbershopweb.workspace.WorkspaceConverter;
import com.app.barbershopweb.workspace.WorkspaceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.web.servlet.MockMvc;

import static com.app.barbershopweb.workspace.constants.WorkspaceMetadata__TestConstants.*;
import static com.app.barbershopweb.workspace.constants.error.WorkspaceErrorMessage_PathVar__TestConstants.WORKSPACE_ERR_INVALID_PATH_VAR_WORKSPACE_ID;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkspaceController.class)
@DisplayName("Testing DELETE: " + WORKSPACES_URL + "{workspaceId}")
@MockBean(AuthenticationProvider.class)
class WorkspaceControllerDeleteByIdTest {

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
                .perform(delete(WORKSPACES_URL + "/" + WORKSPACE_INVALID_WORKSPACE_ID))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is(WORKSPACE_ERR_INVALID_PATH_VAR_WORKSPACE_ID)));
    }

    @DisplayName("returns empty body, status code 200, " +
            "when: workspace with existing / not existing id was deleted")
    @Test
    void shouldDeleteWorkspaceById() throws Exception {
        mockMvc
                .perform(delete(WORKSPACES_URL + "/" + WORKSPACE_VALID_WORKSPACE_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }

}
