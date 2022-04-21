package com.app.barbershopweb.minio.s3;

import com.app.barbershopweb.exception.MinioClientException;
import com.app.barbershopweb.minio.MinioService;
import com.app.barbershopweb.minio.MinioServiceUtils;
import io.minio.*;
import io.minio.errors.ServerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;

import java.security.InvalidKeyException;

import static com.app.barbershopweb.minio.s3.constants.MinioService_Metadata__TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MinioServiceTest {

    MinioClient minioClient = Mockito.mock(MinioClient.class);

    MinioServiceUtils utils = Mockito.mock(MinioServiceUtils.class);

    MinioService minioService = new MinioService(minioClient, utils, MINIO_SERVICE_BUCKET_NAME);

    @Test
    void deleteFile() throws Exception {
        minioService.deleteFile(MINIO_SERVICE_BUCKET_NAME, MINIO_SERVICE_OBJECT_KEY);
        verify(minioClient, times(1)).removeObject(
                RemoveObjectArgs.builder().bucket(MINIO_SERVICE_BUCKET_NAME).object(MINIO_SERVICE_OBJECT_KEY)
                        .build()
        );
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
        assertEquals(
                new ByteArrayResource(MINIO_SERVICE_MULTIPART_FILE_MOCK.getBytes()),
                new ByteArrayResource(value.stream().readAllBytes())
        );
    }

    @Test
    void downloadFile() throws Exception {
        when(minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(MINIO_SERVICE_BUCKET_NAME)
                                .object(MINIO_SERVICE_OBJECT_KEY)
                                .build()
                )
        ).thenReturn(new GetObjectResponse(
                null, MINIO_SERVICE_BUCKET_NAME,
                null, MINIO_SERVICE_OBJECT_KEY,
                MINIO_SERVICE_MULTIPART_FILE_MOCK.getInputStream()));

        byte[] bytes = minioService.downloadFile(MINIO_SERVICE_BUCKET_NAME, MINIO_SERVICE_OBJECT_KEY);

        assertEquals(
                new ByteArrayResource(MINIO_SERVICE_MULTIPART_FILE_MOCK.getBytes()),
                new ByteArrayResource(bytes)
        );
    }


    @Test
    @DisplayName("when sth went wrong during download - throws MinioClientException")
    void downloadFile__Exception() throws Exception {
        InvalidKeyException e = new InvalidKeyException();
        when(minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(MINIO_SERVICE_BUCKET_NAME)
                        .object(MINIO_SERVICE_OBJECT_KEY)
                        .build()
        ))
                .thenThrow(e);
        doThrow(new MinioClientException("")).when(utils).handleNotFoundBucketObject(e);

        assertThrows(MinioClientException.class,
                () -> minioService.downloadFile(MINIO_SERVICE_BUCKET_NAME, MINIO_SERVICE_OBJECT_KEY)
        );
    }

    @Test
    @DisplayName("when there is no object found in minio, returns empty byte array")
    void downloadFile__NoObjectFound() throws Exception {
        ServerException e = new ServerException("The specified key does not exist.", null);

        when(minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(MINIO_SERVICE_BUCKET_NAME)
                        .object(MINIO_SERVICE_OBJECT_KEY)
                        .build()
        ))
                .thenThrow(e);

        assertEquals(
                new ByteArrayResource(new byte[0]),
                new ByteArrayResource(
                        minioService.downloadFile(MINIO_SERVICE_BUCKET_NAME, MINIO_SERVICE_OBJECT_KEY)
                )
        );
    }

}