package com.app.barbershopweb.integrationtests.workspace;

import com.app.barbershopweb.barbershop.repository.JdbcBarbershopRepository;
import com.app.barbershopweb.error.ErrorDto;
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

import java.util.Objects;

import static com.app.barbershopweb.barbershop.constants.BarbershopDto__TestConstants.BARBERSHOP_VALID_DTO;
import static com.app.barbershopweb.barbershop.constants.BarbershopMetadata__TestConstants.BARBERSHOPS_URL;
import static com.app.barbershopweb.user.crud.constants.UserDto__TestConstants.USERS_VALID_USER_DTO;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_URL;
import static com.app.barbershopweb.workspace.constants.WorkspaceDto__TestConstants.*;
import static com.app.barbershopweb.workspace.constants.WorkspaceMetadata__TestConstants.*;
import static com.app.barbershopweb.workspace.constants.error.WorkspaceErrorMessage_Dto__TestConstants.*;
import static com.app.barbershopweb.workspace.constants.error.WorkspaceErrorMessage_Fk__TestConstants.WORKSPACE_ERR_FK_BARBERSHOP_ID;
import static com.app.barbershopweb.workspace.constants.error.WorkspaceErrorMessage_Fk__TestConstants.WORKSPACE_ERR_FK_USER_ID;
import static com.app.barbershopweb.workspace.constants.error.WorkspaceErrorMessage_PathVar__TestConstants.WORKSPACE_ERR_INVALID_PATH_VAR_WORKSPACE_ID;
import static com.app.barbershopweb.workspace.constants.error.WorkspaceErrorMessage_Uk__TestConstants.WORKSPACE_ERR_UK_USER_ID__BARBERSHOP_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("workspace error handling IT")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WorkspaceErrorHandlerIT extends AbstractIT {
    @Autowired
    private TestRestTemplate restTemplate;


    @Autowired
    private JdbcBarbershopRepository barbershopRepository;
    @Autowired
    private JdbcUsersRepository usersRepository;
    @Autowired
    private JdbcWorkspaceRepository workspaceRepository;

    void initFk() {
        restTemplate.postForEntity(BARBERSHOPS_URL, BARBERSHOP_VALID_DTO, Long.class);
        restTemplate.postForEntity(USERS_URL, USERS_VALID_USER_DTO, Long.class);
    }

    @DisplayName("POST: " + WORKSPACES_URL +
            " when workspace dto isn't valid " +
            "returns status code 400 & error dto")
    @Test
    @Order(1)
    void whenWorkspaceDtoNotValidPost() {
        final ResponseEntity<ErrorDto> response = restTemplate.postForEntity(WORKSPACES_URL, WORKSPACE_INVALID_DTO, ErrorDto.class);
        ErrorDto body = response.getBody();


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(3, Objects.requireNonNull(body).errors().size());

        assertTrue(body.errors().contains(WORKSPACE_ERR_INVALID_DTO_BARBERSHOP_ID));
        assertTrue(body.errors().contains(WORKSPACE_ERR_INVALID_DTO_WORKSPACE_ID));
        assertTrue(body.errors().contains(WORKSPACE_ERR_INVALID_DTO_USER_ID));
    }

    @DisplayName("GET: " + WORKSPACES_URL + "/{workspaceId} " +
            "When path variable input 'workspaceId' isn't valid" +
            " returns status code 400 (BAD_REQUEST) & error dto")
    @Test
    @Order(2)
    void whenWorkspaceIdNotValidGet() {
        ResponseEntity<ErrorDto> response = restTemplate.getForEntity(
                WORKSPACES_URL + "/" + WORKSPACE_INVALID_WORKSPACE_ID,
                ErrorDto.class
        );


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(WORKSPACE_ERR_INVALID_PATH_VAR_WORKSPACE_ID, Objects.requireNonNull(response.getBody()).errors().get(0));
        assertEquals(1, response.getBody().errors().size());
    }

    @DisplayName("GET: " + WORKSPACES_URL + "/{workspaceId} " +
            "when there's no workspace with id 'workspaceId' " +
            "returns status code 404 & error dto")
    @Test
    @Order(3)
    void whenNotExistedWorkspaceId() {
        ResponseEntity<ErrorDto> response = restTemplate.getForEntity(WORKSPACES_URL + "/" + WORKSPACE_NOT_EXISTED_WORKSPACE_ID, ErrorDto.class);


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Workspace with id '" + WORKSPACE_NOT_EXISTED_WORKSPACE_ID + "' not found.",
                Objects.requireNonNull(response.getBody()).errors().get(0));
        assertEquals(1, response.getBody().errors().size());
    }

    @DisplayName("PUT: " + WORKSPACES_URL +
            " when workspace dto isn't valid " +
            "returns status code 400 & error dto")
    @Test
    @Order(4)
    void whenWorkspaceDtoNotValidPut() {
        HttpEntity<WorkspaceDto> requestEntity = new HttpEntity<>(WORKSPACE_INVALID_DTO);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(WORKSPACES_URL, HttpMethod.PUT, requestEntity, ErrorDto.class);
        ErrorDto body = response.getBody();


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(3, Objects.requireNonNull(body).errors().size());

        assertTrue(body.errors().contains(WORKSPACE_ERR_INVALID_DTO_USER_ID));
        assertTrue(body.errors().contains(WORKSPACE_ERR_INVALID_DTO_WORKSPACE_ID));
        assertTrue(body.errors().contains(WORKSPACE_ERR_INVALID_DTO_BARBERSHOP_ID));
    }

    @DisplayName("DELETE: " + WORKSPACES_URL + "/{workspaceId}" +
            " When path variable input 'workspaceId' isn't valid" +
            " returns status code 400 (BAD_REQUEST) & error dto")
    @Test
    @Order(5)
    void whenWorkspaceIdNotValidDelete() {
        ResponseEntity<ErrorDto> response = restTemplate.exchange(WORKSPACES_URL + "/" + WORKSPACE_INVALID_WORKSPACE_ID, HttpMethod.DELETE, null, ErrorDto.class);
        ErrorDto body = response.getBody();


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(body).errors().size());
        assertEquals(WORKSPACE_ERR_INVALID_PATH_VAR_WORKSPACE_ID, body.errors().get(0));
    }

    @DisplayName("POST: " + WORKSPACES_URL +
            " when workspace dto violates db fk constraints " +
            "returns status code 404 & error dto")
    @Test
    @Order(6)
    void whenWorkspaceDtoViolatesDbFkConstraintsPost() {

        final ResponseEntity<ErrorDto> response = restTemplate.postForEntity(WORKSPACES_URL, WORKSPACE_VALID_DTO, ErrorDto.class);
        ErrorDto body = response.getBody();


        assertEquals(2, Objects.requireNonNull(body).errors().size());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        assertTrue(body.errors().contains(WORKSPACE_ERR_FK_BARBERSHOP_ID));
        assertTrue(body.errors().contains(WORKSPACE_ERR_FK_USER_ID));

    }

    @DisplayName("PUT: " + WORKSPACES_URL +
            " when workspace dto violates db fk constraints " +
            "returns status code 404 & error dto")
    @Test
    @Order(7)
    void whenWorkspaceDtoViolatesDbFkConstraintsPut() {
        HttpEntity<WorkspaceDto> requestEntity = new HttpEntity<>(WORKSPACE_VALID_DTO);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(WORKSPACES_URL, HttpMethod.PUT, requestEntity, ErrorDto.class);
        ErrorDto body = response.getBody();

        assertEquals(2, Objects.requireNonNull(body).errors().size());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        assertTrue(body.errors().contains(WORKSPACE_ERR_FK_BARBERSHOP_ID));
        assertTrue(body.errors().contains(WORKSPACE_ERR_FK_USER_ID));
    }

    @Test
    @DisplayName("PUT: " + WORKSPACES_URL +
            " when entity with 'id' in workspaceDto doesn't exist " +
            "returns status code 404 and error dto")
    @Order(8)
    void whenNotFoundWorkspaceId() {
        initFk();

        HttpEntity<WorkspaceDto> requestEntity = new HttpEntity<>(WORKSPACE_NOT_EXISTED_ID_DTO);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(WORKSPACES_URL, HttpMethod.PUT, requestEntity, ErrorDto.class);
        ErrorDto body = response.getBody();
        System.out.println(body);


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(body).errors().size());
        assertEquals(
                "Workspace with id '" + WORKSPACE_NOT_EXISTED_ID_DTO.workspaceId() + "' not found.",
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

        final ResponseEntity<ErrorDto> response = restTemplate.postForEntity(WORKSPACES_URL, WORKSPACE_VALID_DTO, ErrorDto.class);
        ErrorDto body = response.getBody();


        assertEquals(1, Objects.requireNonNull(body).errors().size());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertTrue(body.errors().contains(WORKSPACE_ERR_UK_USER_ID__BARBERSHOP_ID));

    }

    @DisplayName("PUT: " + WORKSPACES_URL +
            " when workspace dto violates db uk constraints " +
            "returns status code 400 & error dto")
    @Test
    @Order(10)
    void whenWorkspaceDtoViolatesDbUkConstraintsPut() {
        HttpEntity<WorkspaceDto> requestEntity = new HttpEntity<>(WORKSPACE_VALID_DTO);
        ResponseEntity<ErrorDto> response = restTemplate.exchange(WORKSPACES_URL, HttpMethod.PUT, requestEntity, ErrorDto.class);
        ErrorDto body = response.getBody();

        System.out.println(body);

        assertEquals(1, Objects.requireNonNull(body).errors().size());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertTrue(body.errors().contains(WORKSPACE_ERR_UK_USER_ID__BARBERSHOP_ID));
    }

    void addWorkspaceWithUnusedUk() {
        restTemplate.postForEntity(WORKSPACES_URL, WORKSPACE_VALID_DTO, Long.class);
    }

    @AfterAll
    void cleanUpDb() {
        barbershopRepository.truncateAndRestartSequence();
        usersRepository.truncateAndRestartSequence();
        workspaceRepository.truncateAndRestartSequence();
    }
}
