package com.app.barbershopweb.integrationtests.workspace;

import com.app.barbershopweb.barbershop.BarbershopTestConstants;
import com.app.barbershopweb.barbershop.repository.JdbcBarbershopRepository;
import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.user.UserTestConstants;
import com.app.barbershopweb.user.repository.JdbcUsersRepository;
import com.app.barbershopweb.workspace.WorkspaceDto;
import com.app.barbershopweb.workspace.WorkspaceTestConstants;
import com.app.barbershopweb.workspace.repository.JdbcWorkspaceRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.app.barbershopweb.barbershop.BarbershopTestConstants.BARBERSHOPS_URL;
import static com.app.barbershopweb.user.UserTestConstants.USERS_URL;
import static com.app.barbershopweb.workspace.WorkspaceTestConstants.WORKSPACES_URL;
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


    private final WorkspaceTestConstants wtc = new WorkspaceTestConstants();
    private static final BarbershopTestConstants btc = new BarbershopTestConstants();
    private static final UserTestConstants utc = new UserTestConstants();



    //add barbershop and user rows into db once for workspace tests (post, put requests)
    void init() {
        restTemplate.postForEntity(BARBERSHOPS_URL, btc.VALID_BARBERSHOP_DTO_LIST.get(0), Long.class);
        restTemplate.postForEntity(BARBERSHOPS_URL, btc.VALID_BARBERSHOP_DTO_LIST.get(1), Long.class);
        restTemplate.postForEntity(BARBERSHOPS_URL, btc.VALID_BARBERSHOP_DTO_LIST.get(2), Long.class);

        restTemplate.postForEntity(USERS_URL, utc.VALID_USER_DTO_LIST.get(0), Long.class);
        restTemplate.postForEntity(USERS_URL, utc.VALID_USER_DTO_LIST.get(1), Long.class);
        restTemplate.postForEntity(USERS_URL, utc.VALID_USER_DTO_LIST.get(2), Long.class);
    }


    @DisplayName("GET: " + WORKSPACES_URL +
            " gives empty Workspace List(array) when they're no added workspaces yet")
    @Test
    @Order(1)
    void shouldReturnEmptyWorkspaceList() {

        ResponseEntity<WorkspaceDto[]> response = restTemplate.getForEntity(WORKSPACES_URL, WorkspaceDto[].class);
        System.out.println(Arrays.toString(response.getBody()));
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
                WORKSPACES_URL, wtc.VALID_WORKSPACE_DTO, Long.class
        );

        assertEquals(wtc.VALID_WORKSPACE_ID, response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @DisplayName("GET: " + WORKSPACES_URL + "/{workspaceId}" +
            " returns corresponding workspace dto")
    @Test
    @Order(3)
    void shouldReturnWorkspace() {
        ResponseEntity<WorkspaceDto> response = restTemplate.getForEntity(
                WORKSPACES_URL + "/" + wtc.VALID_WORKSPACE_ID, WorkspaceDto.class
        );
        WorkspaceDto body = response.getBody();
        assertEquals(wtc.VALID_WORKSPACE_ID, Objects.requireNonNull(body).workspaceId());
        assertEquals(wtc.VALID_WORKSPACE_DTO.barbershopId(), Objects.requireNonNull(body).barbershopId());
        assertEquals(wtc.VALID_WORKSPACE_DTO.userId(), Objects.requireNonNull(body).userId());
        assertEquals(wtc.VALID_WORKSPACE_DTO.active(), Objects.requireNonNull(body).active());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(wtc.WORKSPACE_FIELD_AMOUNT, WorkspaceDto.class.getDeclaredFields().length);
    }

    @Test
    @DisplayName("PUT: " + WORKSPACES_URL +
            " should return updated workspace (dto)")
    @Order(4)
    void shouldReturnUpdatedWorkspace() {
        HttpEntity<WorkspaceDto> entity = new HttpEntity<>(wtc.VALID_UPDATED_WORKSPACE_DTO);
        ResponseEntity<WorkspaceDto> response = restTemplate.exchange(WORKSPACES_URL, HttpMethod.PUT, entity, WorkspaceDto.class);
        WorkspaceDto body = response.getBody();


        assertEquals(wtc.VALID_UPDATED_WORKSPACE_DTO.workspaceId(), Objects.requireNonNull(body).workspaceId());
        assertEquals(wtc.VALID_UPDATED_WORKSPACE_DTO.barbershopId(), Objects.requireNonNull(body).barbershopId());
        assertEquals(wtc.VALID_UPDATED_WORKSPACE_DTO.userId(), Objects.requireNonNull(body).userId());
        assertEquals(wtc.VALID_UPDATED_WORKSPACE_DTO.active(), Objects.requireNonNull(body).active());
        assertEquals(wtc.WORKSPACE_FIELD_AMOUNT, WorkspaceDto.class.getDeclaredFields().length);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @DisplayName("GET: " + WORKSPACES_URL +
            " gives all workspaces at once")
    @Test
    @Order(5)
    void shouldReturnAllWorkspace() {
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY_LIST.get(1));
        workspaceRepository.addWorkspace(wtc.VALID_WORKSPACE_ENTITY_LIST.get(2));

        ResponseEntity<WorkspaceDto[]> response = restTemplate.getForEntity(WORKSPACES_URL, WorkspaceDto[].class);
        List<WorkspaceDto> body = List.of(Objects.requireNonNull(response.getBody()));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(wtc.VALID_WORKSPACE_DTO_LIST.size(), Objects.requireNonNull(body).size());

        assertTrue(body.contains(wtc.VALID_WORKSPACE_DTO_LIST.get(1)));
        assertTrue(body.contains(wtc.VALID_WORKSPACE_DTO_LIST.get(2)));
        assertTrue(body.contains(wtc.VALID_UPDATED_WORKSPACE_DTO));

    }

    @DisplayName("DELETE: " + WORKSPACES_URL + "{workspaceId}" +
            " returns empty body, status code 200, " +
            "when: workspace with existing / not existing id was deleted")
    @Test
    @Order(6)
    void shouldDeleteWorkspaceById() {
        ResponseEntity<Object> response = restTemplate.exchange(
                WORKSPACES_URL + "/" + wtc.VALID_WORKSPACE_ID,
                HttpMethod.DELETE, null, Object.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());

        assertTrue(workspaceRepository.findWorkspaceById(wtc.VALID_WORKSPACE_ID).isEmpty());
    }

    @AfterAll
    void cleanUpDb() {
        usersRepository.truncateAndRestartSequence();
        barbershopRepository.truncateAndRestartSequence();
        workspaceRepository.truncateAndRestartSequence();
    }

}
