package com.app.barbershopweb.aws.s3;

import com.amazonaws.AmazonClientException;
import com.amazonaws.SdkClientException;
import com.amazonaws.util.IOUtils;
import com.app.barbershopweb.exception.FileException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;

import static com.app.barbershopweb.aws.s3.constants.S3Service_Metadata__TestConstants.S3_SERVICE_MULTIPART_FILE_MOCK;
import static com.app.barbershopweb.aws.s3.constants.S3Service_Metadata__TestConstants.S3_SERVICE_OBJECT_MOCK;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class S3ServiceUtilsTest {

    S3ServiceUtils utils = new S3ServiceUtils();

    @Test
    @DisplayName("should throw new FileException instead of IOException")
    void toByteArray__IOException() throws IOException {
        MockedStatic<IOUtils> mockedStatic = mockStatic(IOUtils.class);

        when(IOUtils.toByteArray(S3_SERVICE_OBJECT_MOCK.getObjectContent()))
                .thenThrow(new IOException(""));

        assertThrows(FileException.class,
                () -> utils.toByteArray(
                        S3_SERVICE_OBJECT_MOCK.getObjectContent()
                )
        );
        mockedStatic.close();
    }

    @Test
    @DisplayName("should return byte array from S3ObjectInputStream")
    void toByteArray() throws IOException {
        assertEquals(
                new ByteArrayResource(S3_SERVICE_MULTIPART_FILE_MOCK.getBytes()),
                new ByteArrayResource(utils.toByteArray(S3_SERVICE_OBJECT_MOCK.getObjectContent()))
        );
    }


    @Test
    @DisplayName("when the except. message doesn't correspond to expected," +
            " throws AmazonClientException")
    void handleNotFoundBucketObject__WrongMessage() {
        SdkClientException exception = new SdkClientException("message");

        assertThrows(AmazonClientException.class,
                () -> utils.handleNotFoundBucketObject(exception)
        );
    }

    @Test
    @DisplayName("doesn't throw any exceptions if the except. message as expected")
    void handleNotFoundBucketObject() {
        SdkClientException exception = new SdkClientException("The specified key does not exist.");
        assertDoesNotThrow(() -> utils.handleNotFoundBucketObject(exception));
    }
}