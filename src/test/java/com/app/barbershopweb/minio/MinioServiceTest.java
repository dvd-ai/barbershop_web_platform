package com.app.barbershopweb.minio;

import com.app.barbershopweb.exception.MinioClientException;
import io.minio.*;
import io.minio.errors.ServerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.InvalidKeyException;

import static com.app.barbershopweb.minio.constants.MinioService_Metadata__TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MinioServiceTest {

    MinioClient minioClient = Mockito.mock(MinioClient.class);

    MinioService minioService = new MinioService(minioClient, MINIO_SERVICE_BUCKET_NAME);

    @Test
    void deleteFile() throws Exception {
        minioService.deleteFile(MINIO_SERVICE_BUCKET_NAME, MINIO_SERVICE_OBJECT_KEY);

        RemoveObjectArgs args = RemoveObjectArgs.builder()
                .bucket(MINIO_SERVICE_BUCKET_NAME)
                .object(MINIO_SERVICE_OBJECT_KEY)
                .build();

        verify(minioClient, times(1)).removeObject(args);
    }

    @Test
    void uploadFile() throws Exception {
        minioService.uploadFile(
                MINIO_SERVICE_BUCKET_NAME, MINIO_SERVICE_OBJECT_KEY,
                MINIO_SERVICE_MULTIPART_FILE_MOCK
        );
        ArgumentCaptor<PutObjectArgs> captor = ArgumentCaptor.forClass(PutObjectArgs.class);
        verify(minioClient).putObject(
                captor.capture()
        );

        PutObjectArgs value = captor.getValue();
        assertEquals(MINIO_SERVICE_MULTIPART_FILE_MOCK.getContentType(), value.contentType());
        assertEquals(MINIO_SERVICE_BUCKET_NAME, value.bucket());
        assertEquals(MINIO_SERVICE_OBJECT_KEY, value.object());
        assertArrayEquals(
                MINIO_SERVICE_MULTIPART_FILE_MOCK.getBytes(),
                value.stream().readAllBytes()
        );
    }

    @Test
    void downloadFile() throws Exception {
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(MINIO_SERVICE_BUCKET_NAME)
                .object(MINIO_SERVICE_OBJECT_KEY)
                .build();

        when(minioClient.getObject(args)).thenReturn(
                new GetObjectResponse(
                        null, MINIO_SERVICE_BUCKET_NAME,
                        null, MINIO_SERVICE_OBJECT_KEY,
                        MINIO_SERVICE_MULTIPART_FILE_MOCK.getInputStream())
        );

        byte[] bytes = minioService.downloadFile(MINIO_SERVICE_BUCKET_NAME, MINIO_SERVICE_OBJECT_KEY);

        assertArrayEquals(
                MINIO_SERVICE_MULTIPART_FILE_MOCK.getBytes(),
                bytes
        );
    }


    @Test
    @DisplayName("when sth went wrong during download - throws MinioClientException")
    void downloadFile__Exception() throws Exception {
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(MINIO_SERVICE_BUCKET_NAME)
                .object(MINIO_SERVICE_OBJECT_KEY)
                .build();

        when(minioClient.getObject(args))
                .thenThrow(new InvalidKeyException(""));

        assertThrows(MinioClientException.class,
                () -> minioService.downloadFile(MINIO_SERVICE_BUCKET_NAME, MINIO_SERVICE_OBJECT_KEY)
        );
    }

    @Test
    @DisplayName("when there is no object found in minio, returns empty byte array")
    void downloadFile__NoObjectFound() throws Exception {
        ServerException e = new ServerException("The specified key does not exist.", null);

        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(MINIO_SERVICE_BUCKET_NAME)
                .object(MINIO_SERVICE_OBJECT_KEY)
                .build();

        when(minioClient.getObject(args))
                .thenThrow(e);

        assertArrayEquals(
                new byte[0],
                minioService.downloadFile(MINIO_SERVICE_BUCKET_NAME, MINIO_SERVICE_OBJECT_KEY)
        );
    }

}