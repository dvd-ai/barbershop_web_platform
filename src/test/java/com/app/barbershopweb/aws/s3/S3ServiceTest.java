package com.app.barbershopweb.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.app.barbershopweb.exception.FileException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;

import static com.app.barbershopweb.aws.s3.constants.S3Service_Metadata__TestConstants.*;
import static com.app.barbershopweb.util.MultipartFileUtil.convertMultipartFileToFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {
    @Mock
    AmazonS3 amazonS3;

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
        MockedStatic<IOUtils> mockStatic = mockStatic(IOUtils.class);

        when(IOUtils.toByteArray(any(S3ObjectInputStream.class)))
                .thenReturn(S3_SERVICE_MULTIPART_FILE_MOCK.getBytes());
        when(amazonS3.getObject(S3_SERVICE_BUCKET_NAME, S3_SERVICE_OBJECT_KEY))
                .thenReturn(S3_SERVICE_OBJECT_MOCK);

        byte[] bytes = s3Service.downloadFile(S3_SERVICE_BUCKET_NAME, S3_SERVICE_OBJECT_KEY);
        assertEquals(S3_SERVICE_MULTIPART_FILE_MOCK.getBytes(), bytes);

        mockStatic.close();
    }

    @Test
    @DisplayName("wraps IOException into FileException, when it occurs")
    void downloadFileIOException() throws IOException {
        MockedStatic<IOUtils> mockStatic = mockStatic(IOUtils.class);

        when(amazonS3.getObject(S3_SERVICE_BUCKET_NAME, S3_SERVICE_OBJECT_KEY))
                .thenReturn(S3_SERVICE_OBJECT_MOCK);

        when(IOUtils.toByteArray(any(S3ObjectInputStream.class)))
                .thenThrow(new IOException(""));

        assertThrows(FileException.class,
                () -> {
                    s3Service.downloadFile(S3_SERVICE_BUCKET_NAME, S3_SERVICE_OBJECT_KEY);
                }
        );

        mockStatic.close();
    }
}