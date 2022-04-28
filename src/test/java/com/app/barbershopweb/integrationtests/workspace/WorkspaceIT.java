package com.app.barbershopweb.integrationtests.workspace;

import com.app.barbershopweb.barbershop.crud.repository.JdbcBarbershopRepository;
import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.user.crud.repository.JdbcUsersRepository;
import com.app.barbershopweb.workspace.WorkspaceDto;
import com.app.barbershopweb.workspace.repository.JdbcWorkspaceRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static com.app.barbershopweb.barbershop.crud.constants.BarbershopList__TestConstants.BARBERSHOP_VALID_DTO_LIST;
import static com.app.barbershopweb.barbershop.crud.constants.BarbershopMetadata__TestConstants.BARBERSHOPS_URL;
import static com.app.barbershopweb.user.crud.constants.UserList__TestConstants.USERS_USER_VALID_DTO_LIST;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_URL;
import static com.app.barbershopweb.workspace.constants.WorkspaceDto__TestConstants.WORKSPACE_VALID_DTO;
import static com.app.barbershopweb.workspace.constants.WorkspaceDto__TestConstants.WORKSPACE_VALID_UPDATED_DTO;
import static com.app.barbershopweb.workspace.constants.WorkspaceList__TestConstants.WORKSPACE_VALID_DTO_LIST;
import static com.app.barbershopweb.workspace.constants.WorkspaceList__TestConstants.WORKSPACE_VALID_ENTITY_LIST;
import static com.app.barbershopweb.workspace.constants.WorkspaceMetadata__TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Workspace IT without error handling")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WorkspaceIT extends AbstractIT {

    @Autowired
    private JdbcWorkspaceRepository workspaceRepository;
    @Autowired
    private JdbcUsersRepository usersRepository;
    @Autowired
    private JdbcBarbershopRepository barbershopRepository;


    @Autowired
    private TestRestTemplate restTemplate;


    //add barbershop and user rows into db once for workspace tests (post, put requests)
    void init() {
        restTemplate.postForEntity(BARBERSHOPS_URL, BARBERSHOP_VALID_DTO_LIST.get(0), Long.class);
        restTemplate.postForEntity(BARBERSHOPS_URL, BARBERSHOP_VALID_DTO_LIST.get(1), Long.class);
        restTemplate.postForEntity(BARBERSHOPS_URL, BARBERSHOP_VALID_DTO_LIST.get(2), Long.class);

        restTemplate.postForEntity(USERS_URL, USERS_USER_VALID_DTO_LIST.get(0), Long.class);
        restTemplate.postForEntity(USERS_URL, USERS_USER_VALID_DTO_LIST.get(1), Long.class);
        restTemplate.postForEntity(USERS_URL, USERS_USER_VALID_DTO_LIST.get(2), Long.class);
    }


    @DisplayName("GET: " + WORKSPACES_URL +
            " gives empty Workspace List(array) when they're no added workspaces yet")
    @Test
    @Order(1)
    void shouldReturnEmptyWorkspaceList() {

        ResponseEntity<WorkspaceDto[]> response = restTemplate.getForEntity(WORKSPACES_URL, WorkspaceDto[].class);
        assertEquals(0, Objects.requireNonNull(response.getBody()).length);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @DisplayName("POST: " + WORKSPACES_URL +
            " after saving workspace entity returns its id and status code 201")
    @Test
    @Order(2)
    void shouldAddWorkspace() {
        init();

        ResponseEntity<Long> response = restTemplate.postForEntity(
                WORKSPACES_URL, WORKSPACE_VALID_DTO, Long.class
        );

        assertEquals(WORKSPACE_VALID_WORKSPACE_ID, response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @DisplayName("GET: " + WORKSPACES_URL + "/{workspaceId}" +
            " returns corresponding workspace dto")
    @Test
    @Order(3)
    void shouldReturnWorkspace() {
        ResponseEntity<WorkspaceDto> response = restTemplate.getForEntity(
                WORKSPACES_URL + "/" + WORKSPACE_VALID_WORKSPACE_ID, WorkspaceDto.class
        );
        WorkspaceDto body = response.getBody();
        assertEquals(WORKSPACE_VALID_WORKSPACE_ID, Objects.requireNonNull(body).workspaceId());
        assertEquals(WORKSPACE_VALID_DTO.barbershopId(), Objects.requireNonNull(body).barbershopId());
        assertEquals(WORKSPACE_VALID_DTO.userId(), Objects.requireNonNull(body).userId());
        assertEquals(WORKSPACE_VALID_DTO.active(), Objects.requireNonNull(body).active());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(WORKSPACE_FIELD_AMOUNT, WorkspaceDto.class.getDeclaredFields().length);
    }

    @Test
    @DisplayName("PUT: " + WORKSPACES_URL +
            " should return updated workspace (dto)")
    @Order(4)
    void shouldReturnUpdatedWorkspace() {
        HttpEntity<WorkspaceDto> entity = new HttpEntity<>(WORKSPACE_VALID_UPDATED_DTO);
        ResponseEntity<WorkspaceDto> response = restTemplate.exchange(WORKSPACES_URL, HttpMethod.PUT, entity, WorkspaceDto.class);
        WorkspaceDto body = response.getBody();


        assertEquals(WORKSPACE_VALID_UPDATED_DTO.workspaceId(), Objects.requireNonNull(body).workspaceId());
        assertEquals(WORKSPACE_VALID_UPDATED_DTO.barbershopId(), Objects.requireNonNull(body).barbershopId());
        assertEquals(WORKSPACE_VALID_UPDATED_DTO.userId(), Objects.requireNonNull(body).userId());
        assertEquals(WORKSPACE_VALID_UPDATED_DTO.active(), Objects.requireNonNull(body).active());
        assertEquals(WORKSPACE_FIELD_AMOUNT, WorkspaceDto.class.getDeclaredFields().length);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @DisplayName("GET: " + WORKSPACES_URL +
            " gives all workspaces at once")
    @Test
    @Order(5)
    void shouldReturnAllWorkspace() {
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY_LIST.get(1));
        workspaceRepository.addWorkspace(WORKSPACE_VALID_ENTITY_LIST.get(2));

        ResponseEntity<WorkspaceDto[]> response = restTemplate.getForEntity(WORKSPACES_URL, WorkspaceDto[].class);
        List<WorkspaceDto> body = List.of(Objects.requireNonNull(response.getBody()));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(WORKSPACE_VALID_DTO_LIST.size(), Objects.requireNonNull(body).size());

        assertTrue(body.contains(WORKSPACE_VALID_DTO_LIST.get(1)));
        assertTrue(body.contains(WORKSPACE_VALID_DTO_LIST.get(2)));
        assertTrue(body.contains(WORKSPACE_VALID_UPDATED_DTO));

    }

    @DisplayName("DELETE: " + WORKSPACES_URL + "{workspaceId}" +
            " returns empty body, status code 200, " +
            "when: workspace with existing / not existing id was deleted")
    @Test
    @Order(6)
    void shouldDeleteWorkspaceById() {
        ResponseEntity<Object> response = restTemplate.exchange(
                WORKSPACES_URL + "/" + WORKSPACE_VALID_WORKSPACE_ID,
                HttpMethod.DELETE, null, Object.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        assertTrue(workspaceRepository.findWorkspaceById(WORKSPACE_VALID_WORKSPACE_ID).isEmpty());
    }

    @AfterAll
    void cleanUpDb() {
        usersRepository.truncateAndRestartSequence();
        barbershopRepository.truncateAndRestartSequence();
        workspaceRepository.truncateAndRestartSequence();
    }

}
