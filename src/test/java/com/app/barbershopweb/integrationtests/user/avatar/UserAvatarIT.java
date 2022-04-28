package com.app.barbershopweb.integrationtests.user.avatar;

import com.app.barbershopweb.integrationtests.AbstractIT;
import com.app.barbershopweb.user.crud.repository.JdbcUsersRepository;
import io.minio.*;
import io.minio.messages.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.ArrayList;
import java.util.List;

import static com.app.barbershopweb.minio.constants.MinioService_Metadata__TestConstants.MINIO_ALLOWED_MIN_PART_SIZE;
import static com.app.barbershopweb.user.avatar.constants.UserAvatar_Metadata__TestConstants.*;
import static com.app.barbershopweb.user.crud.constants.UserEntity__TestConstants.USERS_VALID_ENTITY;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_VALID_USER_ID;
import static org.junit.jupiter.api.Assertions.*;

class UserAvatarIT extends AbstractIT {

    @Autowired
    private JdbcUsersRepository userRepository;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private MinioClient minioClient;

    static Resource createTempFileResource(byte[] content) throws IOException {
        Path tempFile = Files.createTempFile("avatar", ".png");
        Files.write(tempFile, content);
        return new FileSystemResource(tempFile.toFile());
    }

    @BeforeEach
    void addUser() {
        userRepository.addUser(USERS_VALID_ENTITY);
    }

    @AfterEach
    void cleanUpDb() throws Exception {
        userRepository.truncateAndRestartSequence();
        RemoveObjectArgs args = RemoveObjectArgs.builder()
                .bucket(getBucketName())
                .object(USER_AVATAR_OBJECT_KEY)
                .build();

        minioClient.removeObject(args);
    }

    @Test
    void uploadsAvatar() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("file", createTempFileResource(USERS_AVATAR_IMAGE_MOCK.getBytes()));

        ResponseEntity<Object> response = restTemplate.postForEntity(
                USER_AVATARS_URL + "/" + USERS_VALID_USER_ID,
                new HttpEntity<>(requestMap, headers), Object.class
        );

        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(getBucketName())
                .object(USER_AVATAR_OBJECT_KEY)
                .build();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(
                minioClient.getObject(args).readAllBytes(),
                USERS_AVATAR_IMAGE_MOCK.getBytes()
        );
    }

    @Test
    void downloadAvatar() throws Exception {
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(getBucketName())
                .object(USER_AVATAR_OBJECT_KEY)
                .contentType(USERS_AVATAR_IMAGE_MOCK.getContentType())
                .stream(USERS_AVATAR_IMAGE_MOCK.getInputStream(), -1, MINIO_ALLOWED_MIN_PART_SIZE)
                .build();

        minioClient.putObject(args);

        ResponseEntity<byte[]> response = restTemplate.getForEntity(
                USER_AVATARS_URL + "/" + USERS_VALID_USER_ID,
                byte[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(USERS_AVATAR_IMAGE_MOCK.getBytes(), response.getBody());
    }

    @Test
    void removeAvatar() throws Exception {
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(getBucketName())
                .object(USER_AVATAR_OBJECT_KEY)
                .contentType(USERS_AVATAR_IMAGE_MOCK.getContentType())
                .stream(USERS_AVATAR_IMAGE_MOCK.getInputStream(), -1, MINIO_ALLOWED_MIN_PART_SIZE)
                .build();

        minioClient.putObject(args);

        ResponseEntity<Object> response = restTemplate.exchange(
                USER_AVATARS_URL + "/" + USERS_VALID_USER_ID,
                HttpMethod.DELETE,
                null,
                Object.class
        );

        Iterable<Result<Item>> objects = minioClient.listObjects(ListObjectsArgs.builder().bucket(getBucketName()).build());
        List<Result<Item>> objectList = new ArrayList<>();
        objects.forEach(objectList::add);

        assertTrue(objectList.isEmpty());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
