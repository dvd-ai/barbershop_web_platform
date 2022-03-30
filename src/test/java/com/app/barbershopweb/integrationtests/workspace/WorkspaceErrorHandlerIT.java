package com.app.barbershopweb.integrationtests.workspace;

import com.app.barbershopweb.barbershop.BarbershopTestConstants;
import com.app.barbershopweb.barbershop.repository.JdbcBarbershopRepository;
import com.app.barbershopweb.error.ErrorDto;
import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.user.UserTestConstants;
import com.app.barbershopweb.user.repository.JdbcUsersRepository;
import com.app.barbershopweb.workspace.WorkspaceDto;
import com.app.barbershopweb.workspace.WorkspaceTestConstants;
import com.app.barbershopweb.workspace.repository.JdbcWorkspaceRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Objects;

import static com.app.barbershopweb.barbershop.BarbershopTestConstants.BARBERSHOPS_URL;
import static com.app.barbershopweb.user.UserTestConstants.USERS_URL;
import static com.app.barbershopweb.workspace.WorkspaceTestConstants.WORKSPACES_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("workspace error handling IT")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WorkspaceErrorHandlerIT extends AbstractIT {
    @Autowired
    private TestRestTemplate restTemplate;

    private final WorkspaceTestConstants wtc = new WorkspaceTestConstants();
    private final BarbershopTestConstants btc = new BarbershopTestConstants();
    private final UserTestConstants utc = new UserTestConstants();

    @Autowired
    private JdbcBarbershopRepository barbershopRepository;
    @Autowired
    private JdbcUsersRepository usersRepository;
    @Autowired
    private JdbcWorkspaceRepository workspaceRepository;

    void initFk() {
         restTemplate.postForEntity(BARBERSHOPS_URL, btc.VALID_BARBERSHOP_DTO, Long.class);
         restTemplate.postForEntity(USERS_URL, utc.VALID_USER_DTO, Long.class);
    }

    @DisplayName("POST: " + WORKSPACES_URL +
            " when workspace dto isn't valid " +
            "returns status code 400 & error dto")
    @Test
    @Order(1)
    void whenWorkspaceDtoNotValidPost() {
        final ResponseEntity<ErrorDto> response = restTemplate.postForEntity(WORKSPACES_URL, wtc.INVALID_WORKSPACE_DTO, ErrorDto.class);
        ErrorDto body = response.getBody();
        
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(3, Objects.requireNonNull(body).errors().size());

        assertTrue(body.errors().contains(wtc.DTO_CV_BARBERSHOP_ID_ERR_MSG));
        assertTrue(body.errors().contains(wtc.DTO_CV_WORKSPACE_ID_ERR_MSG));
        assertTrue(body.errors().contains(wtc.DTO_CV_USER_ID_ERR_MSG));
    }

    @DisplayName("GET: " + WORKSPACES_URL + "/{workspaceId} " +
            "When path variable input 'workspaceId' isn't valid" +
            " returns status code 400 (BAD_REQUEST) & error dto")
    @Test
    @Order(2)
    void whenWorkspaceIdNotValidGet() {
        ResponseEntity<ErrorDto> response = restTemplate.getForEntity(
                WORKSPACES_URL + "/" + wtc.INVALID_WORKSPACE_ID,
                ErrorDto.class
        );

        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(wtc.PV_WORKSPACE_ID_ERR_MSG, Objects.requireNonNull(response.getBody()).errors().get(0));
        assertEquals(1, response.getBody().errors().size());
    }

    @DisplayName("GET: " + WORKSPACES_URL + "/{workspaceId} " +
            "when there's no workspace with id 'workspaceId' " +
            "returns status code 404 & error dto")
    @Test
    @Order(3)
    void whenNotExistedWorkspaceId() {
        ResponseEntity<ErrorDto> response = restTemplate.getForEntity(WORKSPACES_URL + "/" + wtc.NOT_EXISTED_WORKSPACE_ID, ErrorDto.class);
        
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Workspace with id '" + wtc.NOT_EXISTED_WORKSPACE_ID + "' not found.",
                Objects.requireNonNull(response.getBody()).errors().get(0));
        assertEquals(1, response.getBody().errors().size());
    }

    @DisplayName("PUT: " + WORKSPACES_URL +
            " when workspace dto isn't valid " +
            "returns status code 400 & error dto")
    @Test
    @Order(4)
    void whenWorkspaceDtoNotValidPut() {
        HttpEntity<WorkspaceDto> requestEntity = new HttpEntity<>(wtc.INVALID_WORKSPACE_DTO);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(WORKSPACES_URL, HttpMethod.PUT, requestEntity, ErrorDto.class);
        ErrorDto body = response.getBody();
        
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(3, Objects.requireNonNull(body).errors().size());

        assertTrue(body.errors().contains(wtc.DTO_CV_USER_ID_ERR_MSG));
        assertTrue(body.errors().contains(wtc.DTO_CV_WORKSPACE_ID_ERR_MSG));
        assertTrue(body.errors().contains(wtc.DTO_CV_BARBERSHOP_ID_ERR_MSG));
    }

    @DisplayName("DELETE: " + WORKSPACES_URL + "/{workspaceId}" +
            " When path variable input 'workspaceId' isn't valid" +
            " returns status code 400 (BAD_REQUEST) & error dto")
    @Test
    @Order(5)
    void whenWorkspaceIdNotValidDelete() {
        ResponseEntity<ErrorDto> response = restTemplate.exchange(WORKSPACES_URL + "/" + wtc.INVALID_WORKSPACE_ID, HttpMethod.DELETE, null, ErrorDto.class);
        ErrorDto body = response.getBody();


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(body).errors().size());
        assertEquals(wtc.PV_WORKSPACE_ID_ERR_MSG, body.errors().get(0));
    }

    @DisplayName("POST: " + WORKSPACES_URL +
            " when workspace dto violates db fk constraints " +
            "returns status code 404 & error dto")
    @Test
    @Order(6)
    void whenWorkspaceDtoViolatesDbFkConstraintsPost() {

        final ResponseEntity<ErrorDto> response = restTemplate.postForEntity(WORKSPACES_URL, wtc.VALID_WORKSPACE_DTO, ErrorDto.class);
        ErrorDto body = response.getBody();


        assertEquals(2, Objects.requireNonNull(body).errors().size());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        assertTrue(body.errors().contains(wtc.FK_CV_BARBERSHOP_ID_ERR_MSG));
        assertTrue(body.errors().contains(wtc.FK_CV_USER_ID_ERR_MSG));

    }

    @DisplayName("PUT: " + WORKSPACES_URL +
            " when workspace dto violates db fk constraints " +
            "returns status code 404 & error dto")
    @Test
    @Order(7)
    void whenWorkspaceDtoViolatesDbFkConstraintsPut() {
        HttpEntity<WorkspaceDto> requestEntity = new HttpEntity<>(wtc.VALID_WORKSPACE_DTO);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(WORKSPACES_URL, HttpMethod.PUT, requestEntity, ErrorDto.class);
        ErrorDto body = response.getBody();

        assertEquals(2, Objects.requireNonNull(body).errors().size());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        assertTrue(body.errors().contains(wtc.FK_CV_BARBERSHOP_ID_ERR_MSG));
        assertTrue(body.errors().contains(wtc.FK_CV_USER_ID_ERR_MSG));
    }

    @Test
    @DisplayName("PUT: " + WORKSPACES_URL +
            " when entity with 'id' in workspaceDto doesn't exist " +
            "returns status code 404 and error dto")
    @Order(8)
    void whenNotFoundWorkspaceId() {
        initFk();

        HttpEntity<WorkspaceDto> requestEntity = new HttpEntity<>(wtc.WORKSPACE_DTO_NOT_EXISTED_ID);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(WORKSPACES_URL, HttpMethod.PUT, requestEntity, ErrorDto.class);
        ErrorDto body = response.getBody();
        System.out.println(body);

        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(body).errors().size());
        assertEquals(
                "Workspace with id '" + wtc.WORKSPACE_DTO_NOT_EXISTED_ID.workspaceId() + "' not found.",
                Objects.requireNonNull(Objects.requireNonNull(body).errors().get(0))
        );

    }

    @DisplayName("POST: " + WORKSPACES_URL +
            " when workspace dto violates db uk constraints " +
            "returns status code 400 & error dto")
    @Test
    @Order(9)
    void whenWorkspaceDtoViolatesDbUkConstraintsPost() {
        addWorkspaceWithUnusedUk();

        final ResponseEntity<ErrorDto> response = restTemplate.postForEntity(WORKSPACES_URL, wtc.VALID_WORKSPACE_DTO, ErrorDto.class);
        ErrorDto body = response.getBody();


        assertEquals(1, Objects.requireNonNull(body).errors().size());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertTrue(body.errors().contains(wtc.UK_CV_ERR_MSG));

    }

    @DisplayName("PUT: " + WORKSPACES_URL +
            " when workspace dto violates db uk constraints " +
            "returns status code 400 & error dto")
    @Test
    @Order(10)
    void whenWorkspaceDtoViolatesDbUkConstraintsPut() {
        HttpEntity<WorkspaceDto> requestEntity = new HttpEntity<>(wtc.VALID_WORKSPACE_DTO);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(WORKSPACES_URL, HttpMethod.PUT, requestEntity, ErrorDto.class);
        ErrorDto body = response.getBody();

        System.out.println(body);

        assertEquals(1, Objects.requireNonNull(body).errors().size());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertTrue(body.errors().contains(wtc.UK_CV_ERR_MSG));
    }

    void addWorkspaceWithUnusedUk() {
        restTemplate.postForEntity(WORKSPACES_URL, wtc.VALID_WORKSPACE_DTO, Long.class);
    }

    @AfterAll
    void cleanUpDb() {
        barbershopRepository.truncateAndRestartSequence();
        usersRepository.truncateAndRestartSequence();
        workspaceRepository.truncateAndRestartSequence();
    }
}
