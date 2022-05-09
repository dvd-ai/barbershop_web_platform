package com.app.barbershopweb.workspace.controller;

import com.app.barbershopweb.workspace.WorkspaceController;
import com.app.barbershopweb.workspace.WorkspaceConverter;
import com.app.barbershopweb.workspace.WorkspaceService;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.app.barbershopweb.barbershop.crud.constants.BarbershopList__TestConstants.BARBERSHOP_VALID_DTO_LIST;
import static com.app.barbershopweb.workspace.constants.WorkspaceList__TestConstants.WORKSPACE_VALID_DTO_LIST;
import static com.app.barbershopweb.workspace.constants.WorkspaceList__TestConstants.WORKSPACE_VALID_ENTITY_LIST;
import static com.app.barbershopweb.workspace.constants.WorkspaceMetadata__TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkspaceController.class)
@DisplayName("Testing GET: " + WORKSPACES_URL)
@ExtendWith(MockitoExtension.class)
@MockBean(AuthenticationProvider.class)
class WorkspaceControllerGetWorkspacesTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WorkspaceService workspaceService;

    @MockBean
    WorkspaceConverter converter;

    @WithMockUser(roles = {"USER", "BARBER"})
    @Test
    void whenNotAdmin() throws Exception {
        mockMvc.
                perform(get(WORKSPACES_URL)).
                andExpect(status().isForbidden())
        ;

    }


    @DisplayName("gives empty Workspace List when they're no added workspaces yet")
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnEmptyWorkspaceList() throws Exception {

        when(workspaceService.getWorkspaces()).thenReturn(Collections.emptyList());
        when(converter.workspaceEntityListToDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

        mockMvc.
                perform(get(WORKSPACES_URL)).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(content().string("[]"));
    }

    @DisplayName("gives all workspaces at once")
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnAllWorkspaces() throws Exception {
        when(workspaceService.getWorkspaces()).thenReturn(
                WORKSPACE_VALID_ENTITY_LIST
        );
        when(converter.workspaceEntityListToDtoList(WORKSPACE_VALID_ENTITY_LIST)).thenReturn(
                WORKSPACE_VALID_DTO_LIST
        );

        MockHttpServletResponse response = mockMvc.
                perform(get(WORKSPACES_URL)).
                andDo(print()).andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        checkWorkspaceDtoJson(response.getContentAsString());
    }

    void checkWorkspaceDtoJson(String json) {
        DocumentContext context = JsonPath.parse(json);
        List<Object> object = context.read("$");

        assertEquals(WORKSPACE_VALID_DTO_LIST.size(), object.size());

        for (var i = 0; i < BARBERSHOP_VALID_DTO_LIST.size(); i++) {
            Map<String, Object> dto = context.read("$[" + i + "]");
            assertEquals(WORKSPACE_FIELD_AMOUNT, dto.size());
            assertEquals(WORKSPACE_VALID_DTO_LIST.get(i).workspaceId().intValue(), (Integer) context.read("$[" + i + "].workspaceId"));
            assertEquals(WORKSPACE_VALID_DTO_LIST.get(i).userId().intValue(), (Integer) context.read("$[" + i + "].userId"));
            assertEquals(WORKSPACE_VALID_DTO_LIST.get(i).barbershopId().intValue(), (Integer) context.read("$[" + i + "].barbershopId"));
            assertEquals(WORKSPACE_VALID_DTO_LIST.get(i).active(), context.read("$[" + i + "].active"));

        }
    }
}
