package com.app.barbershopweb.minio.s3;

import com.app.barbershopweb.exception.MinioClientException;
import com.app.barbershopweb.minio.MinioServiceUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class MinioServiceUtilsTest {

    MinioServiceUtils utils = new MinioServiceUtils();

    @Test
    @DisplayName("when the except. message doesn't correspond to expected," +
            " throws MinioClientException")
    void handleNotFoundBucketObject__WrongMessage() {
        Exception exception = new Exception("message");

        assertThrows(MinioClientException.class,
                () -> utils.handleNotFoundBucketObject(exception)
        );
    }

    @Test
    @DisplayName("doesn't throw any exceptions if the except. message as expected")
    void handleNotFoundBucketObject() {
        Exception exception = new Exception("The specified key does not exist.");
        assertDoesNotThrow(() -> utils.handleNotFoundBucketObject(exception));
    }
}