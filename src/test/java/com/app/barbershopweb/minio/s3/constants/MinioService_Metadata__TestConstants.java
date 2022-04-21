package com.app.barbershopweb.minio.s3.constants;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static io.minio.ObjectWriteArgs.MIN_MULTIPART_SIZE;


public final class MinioService_Metadata__TestConstants {
    public final static String MINIO_SERVICE_OBJECT_KEY = "key";
    public final static MultipartFile MINIO_SERVICE_MULTIPART_FILE_MOCK = new MockMultipartFile(
            "image.png",
            "image.png",
            "image/png",
            "image file content".getBytes()
    );
    public final static String MINIO_SERVICE_BUCKET_NAME = "barbershop-web";
    public final static long MINIO_ALLOWED_MIN_PART_SIZE = MIN_MULTIPART_SIZE;
}
