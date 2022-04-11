package com.app.barbershopweb.aws.s3.constants;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public final class S3Service_Metadata__TestConstants {
    @Value("$(AWS_S3_BUCKET_NAME)")
    public static String S3_SERVICE_BUCKET_NAME; //= "barbershop-web";
    public final static String S3_SERVICE_OBJECT_KEY = "key";
    public final static MultipartFile S3_SERVICE_MULTIPART_FILE_MOCK = new MockMultipartFile(
            "image.png",
            "image.png",
            "image/png",
            "image file content".getBytes()
    );

    public final static S3Object S3_SERVICE_OBJECT_MOCK = setS3Object();

    private static S3Object setS3Object() {
        S3Object obj = new S3Object();
        try {
            obj.setObjectContent(
                    new S3ObjectInputStream(
                            new ByteArrayInputStream(S3_SERVICE_MULTIPART_FILE_MOCK.getBytes()),
                            null
                    )
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
