package com.app.barbershopweb.aws.s3;

import com.amazonaws.AmazonClientException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.app.barbershopweb.exception.FileException;
import org.junit.Ignore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.app.barbershopweb.aws.s3.constants.S3Service_Metadata__TestConstants.*;
import static com.app.barbershopweb.util.MultipartFileUtil.convertMultipartFileToFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {
    @Mock
    AmazonS3 amazonS3;

    @Mock
    S3ServiceUtils utils;

    @InjectMocks
    S3Service s3Service;

    @Test
    void deleteFile() {
        s3Service.deleteFile(S3_SERVICE_BUCKET_NAME, S3_SERVICE_OBJECT_KEY);
        verify(amazonS3, times(1)).deleteObject(
                S3_SERVICE_BUCKET_NAME, S3_SERVICE_OBJECT_KEY
        );
    }

    @Test
    void uploadFile() {
        File file = convertMultipartFileToFile(S3_SERVICE_MULTIPART_FILE_MOCK);

        s3Service.uploadFile(
                S3_SERVICE_BUCKET_NAME, S3_SERVICE_OBJECT_KEY,
                S3_SERVICE_MULTIPART_FILE_MOCK
        );
        verify(amazonS3).putObject(
                S3_SERVICE_BUCKET_NAME,
                S3_SERVICE_OBJECT_KEY,
                file
        );
    }

    @Test
    void downloadFile() throws IOException {
        when(amazonS3.getObject(S3_SERVICE_BUCKET_NAME, S3_SERVICE_OBJECT_KEY))
                .thenReturn(S3_SERVICE_OBJECT_MOCK);
        when(utils.toByteArray(S3_SERVICE_OBJECT_MOCK.getObjectContent()))
                .thenReturn(S3_SERVICE_MULTIPART_FILE_MOCK.getBytes());

        byte[] bytes = s3Service.downloadFile(S3_SERVICE_BUCKET_NAME, S3_SERVICE_OBJECT_KEY);

        assertEquals(S3_SERVICE_MULTIPART_FILE_MOCK.getBytes(), bytes);
    }

    @Test
    @DisplayName("wraps IOException into FileException, when it occurs")
    void downloadFileIOException() {

        when(amazonS3.getObject(S3_SERVICE_BUCKET_NAME, S3_SERVICE_OBJECT_KEY))
                .thenReturn(S3_SERVICE_OBJECT_MOCK);

        when(utils.toByteArray(S3_SERVICE_OBJECT_MOCK.getObjectContent()))
                .thenThrow(new FileException(List.of("")));

        assertThrows(FileException.class,
                () -> s3Service.downloadFile(S3_SERVICE_BUCKET_NAME, S3_SERVICE_OBJECT_KEY)
        );
    }

    @Test
    @DisplayName("when sth went wrong in aws sdk client")
    void downloadFile__SdkClientException() {
        SdkClientException e = new SdkClientException("");
        when(amazonS3.getObject(S3_SERVICE_BUCKET_NAME, S3_SERVICE_OBJECT_KEY))
                .thenThrow(e);
        doThrow(new AmazonClientException("")).when(utils).handleNotFoundBucketObject(e);

        assertThrows(AmazonClientException.class,
                () -> s3Service.downloadFile(S3_SERVICE_BUCKET_NAME, S3_SERVICE_OBJECT_KEY)
        );
    }

    @Test
    @DisplayName("when there is no object found in aws s3, returns empty byte array")
    void downloadFile__NoObjectFound() {
        SdkClientException e = new SdkClientException("The specified key does not exist.");

        when(amazonS3.getObject(S3_SERVICE_BUCKET_NAME, S3_SERVICE_OBJECT_KEY))
                .thenThrow(e);

        assertEquals(
                new ByteArrayResource(new byte[0]),
                new ByteArrayResource(
                        s3Service.downloadFile(S3_SERVICE_BUCKET_NAME, S3_SERVICE_OBJECT_KEY)
                )
        );
    }



}