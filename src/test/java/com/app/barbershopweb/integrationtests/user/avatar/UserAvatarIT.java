package com.app.barbershopweb.integrationtests.user.avatar;

import com.amazonaws.services.s3.AmazonS3;
import com.app.barbershopweb.integrationtests.AbstractAwsIT;
import com.app.barbershopweb.user.crud.repository.JdbcUsersRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.app.barbershopweb.user.avatar.constants.UserAvatar_Metadata__TestConstants.*;
import static com.app.barbershopweb.user.crud.constants.UserEntity__TestConstants.USERS_VALID_ENTITY;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_VALID_USER_ID;
import static com.app.barbershopweb.util.MultipartFileUtil.convertMultipartFileToFile;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserAvatarIT extends AbstractAwsIT {

    @Autowired
    private JdbcUsersRepository userRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AmazonS3 s3;

    private static File file;

    @BeforeAll
    void createBucket() {
        s3.createBucket(getBucketName());
    }

    @BeforeEach
    void addUser() {
        userRepository.addUser(USERS_VALID_ENTITY);
    }

    @AfterEach
    void cleanUpDb() {
        userRepository.truncateAndRestartSequence();
        s3.deleteObject(getBucketName(), USER_AVATAR_OBJECT_KEY);
    }

    @Test
    void uploadsAvatar() throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("file", createTempFileResource(USERS_AVATAR_IMAGE_MOCK.getBytes()));

        ResponseEntity<Object> response = restTemplate.postForEntity(
                USER_AVATARS_URL + "/" + USERS_VALID_USER_ID,
                new HttpEntity<>(requestMap, headers), Object.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(s3.doesObjectExist(getBucketName(), USER_AVATAR_OBJECT_KEY));
        assertEquals(
                new ByteArrayResource(s3.getObject(getBucketName(), USER_AVATAR_OBJECT_KEY).getObjectContent().readAllBytes()),
                new ByteArrayResource(USERS_AVATAR_IMAGE_MOCK.getBytes())
        );
    }

    @Test
    void downloadAvatar() throws IOException {
        file = convertMultipartFileToFile(USERS_AVATAR_IMAGE_MOCK);
        s3.putObject(
                getBucketName(),
                USER_AVATAR_OBJECT_KEY,
                file
        );

        ResponseEntity<ByteArrayResource> response = restTemplate.getForEntity(
                USER_AVATARS_URL + "/" + USERS_VALID_USER_ID,
                ByteArrayResource.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new ByteArrayResource(USERS_AVATAR_IMAGE_MOCK.getBytes()), response.getBody());
    }

    @Test
    void removeAvatar() {
        file = convertMultipartFileToFile(USERS_AVATAR_IMAGE_MOCK);
        s3.putObject(
                getBucketName(),
                USER_AVATAR_OBJECT_KEY,
                file
        );

        ResponseEntity<Object> response = restTemplate.exchange(
                USER_AVATARS_URL + "/" + USERS_VALID_USER_ID,
                HttpMethod.DELETE,
                null,
                Object.class
        );

        assertFalse(s3.doesObjectExist(getBucketName(), USER_AVATAR_OBJECT_KEY));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    static Resource createTempFileResource(byte [] content) throws IOException {
        Path tempFile = Files.createTempFile("avatar", ".png");
        Files.write(tempFile, content);
        return new FileSystemResource(tempFile.toFile());
    }

    @AfterAll
    static void removeFile() {
        file.delete();
    }
}
