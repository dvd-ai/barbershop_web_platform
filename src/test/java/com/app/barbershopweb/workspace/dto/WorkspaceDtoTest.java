package com.app.barbershopweb.workspace.dto;

import com.app.barbershopweb.workspace.WorkspaceController;
import com.app.barbershopweb.workspace.WorkspaceConverter;
import com.app.barbershopweb.workspace.WorkspaceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.app.barbershopweb.workspace.constants.WorkspaceDto__TestConstants.WORKSPACE_INVALID_DTO;
import static com.app.barbershopweb.workspace.constants.WorkspaceMetadata__TestConstants.WORKSPACES_URL;
import static com.app.barbershopweb.workspace.constants.error.WorkspaceErrorMessage_Dto__TestConstants.*;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkspaceController.class)
@MockBean(AuthenticationProvider.class)
class WorkspaceDtoTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    WorkspaceService workspaceService;

    @MockBean
    WorkspaceConverter workspaceConverter;

    @Autowired
    ObjectMapper objectMapper;


    String json;

    @DisplayName(
            "Testing POST: " + WORKSPACES_URL +
                    " when workspace dto isn't valid " +
                    "returns status code 400 & error dto")
    @Test
    @WithMockUser(roles = "ADMIN")
    void addWorkspace__DtoNotValid() throws Exception {
        json = objectMapper.writeValueAsString(
                WORKSPACE_INVALID_DTO
        );

        mockMvc
                .perform(post(WORKSPACES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors", hasItem(WORKSPACE_ERR_INVALID_DTO_BARBERSHOP_ID)))
                .andExpect(jsonPath("$.errors", hasItem(WORKSPACE_ERR_INVALID_DTO_WORKSPACE_ID)))
                .andExpect(jsonPath("$.errors", hasItem(WORKSPACE_ERR_INVALID_DTO_USER_ID)));
    }

    @DisplayName(
            "Testing PUT: " + WORKSPACES_URL +
                    " when workspace dto isn't valid " +
                    "returns status code 400 & error dto")
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateWorkspace__whenWorkspaceDtoNotValid() throws Exception {


        String json = objectMapper.writeValueAsString(
                WORKSPACE_INVALID_DTO
        );

        mockMvc
                .perform(put(WORKSPACES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors", hasItem(WORKSPACE_ERR_INVALID_DTO_USER_ID)))
                .andExpect(jsonPath("$.errors", hasItem(WORKSPACE_ERR_INVALID_DTO_BARBERSHOP_ID)))
                .andExpect(jsonPath("$.errors", hasItem(WORKSPACE_ERR_INVALID_DTO_WORKSPACE_ID)));
    }


}