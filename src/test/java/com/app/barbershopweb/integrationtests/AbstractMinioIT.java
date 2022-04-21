package com.app.barbershopweb.integrationtests;

import com.app.barbershopweb.integrationtests.container.MinioContainer;
import io.minio.MinioClient;

public abstract class AbstractMinioIT extends AbstractIT {
    private static final String bucketName = "barbershop--web";
    private static final String accessKey = "minioadmin";
    private static final String secretKey = accessKey;

    public static MinioContainer minioContainer =
            new MinioContainer(accessKey, secretKey);

    private static MinioClient minioClient;

    static {
        minioContainer.start();
        setMinioClient();
    }

    private static void setMinioClient() {

        AbstractMinioIT.minioClient = MinioClient.builder()
                .endpoint(minioContainer.getHttpHostAddress())
                .credentials(accessKey, secretKey)
                .build();
    }

    public static String getBucketName() {
        return bucketName;
    }

    public static MinioClient getMinioClient() {
        return minioClient;
    }
}
