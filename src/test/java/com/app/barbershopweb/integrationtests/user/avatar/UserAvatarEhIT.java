package com.app.barbershopweb.integrationtests.user.avatar;

import com.app.barbershopweb.error.ErrorDto;
import com.app.barbershopweb.integrationtests.AbstractMinioIT;
import com.app.barbershopweb.user.crud.repository.JdbcUsersRepository;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static com.app.barbershopweb.user.avatar.constants.UserAvatar_ErrorMessage__TestConstants.*;
import static com.app.barbershopweb.user.avatar.constants.UserAvatar_Metadata__TestConstants.*;
import static com.app.barbershopweb.user.crud.constants.UserEntity__TestConstants.USERS_VALID_ENTITY;
import static com.app.barbershopweb.user.crud.constants.UserErrorMessage__TestConstants.USER_ERR_INVALID_PATH_VAR_USER_ID;
import static com.app.barbershopweb.user.crud.constants.UserErrorMessage__TestConstants.USER_ERR_NOT_EXISTING_USER_ID;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserAvatarEhIT extends AbstractMinioIT {

    /*
    downloadAvatar, uploadAvatar, deleteAvatar: FileException, AmazonServiceException, SdkClientException
    are checked only on controller level tests
    */

    @Autowired
    JdbcUsersRepository usersRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MinioClient minioClient;

    static Resource createTempFileResource(byte[] content, String suffix) throws IOException {
        Path tempFile = Files.createTempFile("avatar", suffix);
        Files.write(tempFile, content);
        return new FileSystemResource(tempFile.toFile());
    }

    @AfterEach
    void cleanUpDb() throws Exception {
        usersRepository.truncateAndRestartSequence();
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(getBucketName())
                        .object(USER_AVATAR_OBJECT_KEY)
                        .build()
        );
    }

    @Test
    @DisplayName("when user doesn't exist, returns 404 & error dto")
    void downloadAvatar__UserNotExist() {

        ResponseEntity<ErrorDto> response = restTemplate.getForEntity(
                USER_AVATARS_URL + "/" + USERS_NOT_EXISTING_USER_ID,
                ErrorDto.class
        );

        List<String> errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(1, errors.size());
        assertTrue(errors.contains(USER_ERR_NOT_EXISTING_USER_ID));

    }

    @Test
    @DisplayName("when invalid path var returns 400 & error dto")
    void downloadAvatar__InvalidPathVar() {

        ResponseEntity<ErrorDto> response = restTemplate.getForEntity(
                USER_AVATARS_URL + "/" + USERS_INVALID_USER_ID,
                ErrorDto.class
        );

        List<String> errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, errors.size());
        assertTrue(errors.contains(USER_ERR_INVALID_PATH_VAR_USER_ID));

    }

    @Test
    @DisplayName("when there's no profile avatar returns 404 & error dto")
    void downloadAvatar__NoAvatar() {
        usersRepository.addUser(USERS_VALID_ENTITY);

        ResponseEntity<ErrorDto> response = restTemplate.getForEntity(
                USER_AVATARS_URL + "/" + USERS_VALID_USER_ID,
                ErrorDto.class
        );

        List<String> errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(1, errors.size());
        assertTrue(errors.contains(USER_AVATAR_ERR_NO_AVATAR_FOUND));
    }

    @Test
    @DisplayName("when user doesn't exist, returns 404 & error dto")
    void uploadAvatar__UserNotExist() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("file", createTempFileResource(USERS_AVATAR_IMAGE_MOCK.getBytes(), ".png"));

        ResponseEntity<ErrorDto> response = restTemplate.postForEntity(
                USER_AVATARS_URL + "/" + USERS_NOT_EXISTING_USER_ID,
                new HttpEntity<>(requestMap, headers), ErrorDto.class
        );

        List<String> errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(1, errors.size());
        assertTrue(errors.contains(USER_ERR_NOT_EXISTING_USER_ID));
    }

    @Test
    @DisplayName("when invalid path var, file content type returns 400 & error dto")
    void uploadAvatar__InvalidPathVar_FileType() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("file", createTempFileResource(USERS_AVATAR_IMAGE_MOCK.getBytes(), ".txt"));

        ResponseEntity<ErrorDto> response = restTemplate.postForEntity(
                USER_AVATARS_URL + "/" + USERS_INVALID_USER_ID,
                new HttpEntity<>(requestMap, headers), ErrorDto.class
        );

        List<String> errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(2, errors.size());
        assertTrue(errors.contains(USER_ERR_INVALID_PATH_VAR_USER_ID));
        assertTrue(errors.contains(USER_AVATAR_ERR_INVALID_FILE));
    }

    @Test
    @DisplayName("when no file returns 400")
    void uploadAvatar__NoFile() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("file", null);


        ResponseEntity<ErrorDto> response = restTemplate.postForEntity(
                USER_AVATARS_URL + "/" + USERS_VALID_USER_ID, new HttpEntity<>(requestMap, headers), ErrorDto.class
        );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("when no image content, returns 400 & error dto")
    void uploadAvatar__NoImageContent() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("file", createTempFileResource(USERS_AVATAR_NO_FILE_CONTENT_MOCK.getBytes(), ".png"));


        ResponseEntity<ErrorDto> response = restTemplate.postForEntity(
                USER_AVATARS_URL + "/" + USERS_VALID_USER_ID, new HttpEntity<>(requestMap, headers), ErrorDto.class
        );
        List<String> errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, errors.size());
        assertTrue(errors.contains(USER_AVATAR_ERR_EMPTY_FILE));
    }

    @Test
    @DisplayName("when image exceeds size limit (AvatarImage validator, not global spring validation), returns 400 & error dto")
    void uploadAvatar__imageSizeLimit() throws Exception {
        usersRepository.addUser(USERS_VALID_ENTITY);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("file", createTempFileResource(USERS_AVATAR_FILE_SIZE_LIMIT_MOCK.getBytes(), ".png"));

        ResponseEntity<ErrorDto> response = restTemplate.postForEntity(
                USER_AVATARS_URL + "/" + USERS_VALID_USER_ID,
                new HttpEntity<>(requestMap, headers), ErrorDto.class
        );

        List<String> errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, errors.size());
        assertTrue(errors.contains(USER_AVATAR_ERR_FILE_SIZE));
    }


    @Test
    @DisplayName("when invalid path var, returns 400 & error dto")
    void removeAvatar() {
        ResponseEntity<ErrorDto> response = restTemplate.exchange(
                USER_AVATARS_URL + "/" + USERS_INVALID_USER_ID,
                HttpMethod.DELETE,
                null,
                ErrorDto.class
        );
        List<String> errors = Objects.requireNonNull(response.getBody()).errors();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(1, errors.size());
        assertTrue(errors.contains(USER_ERR_INVALID_PATH_VAR_USER_ID));
    }
}
