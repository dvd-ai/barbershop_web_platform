package com.app.barbershopweb.integrationtests;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

public abstract class AbstractAwsIT extends AbstractIT{

    private static final DockerImageName localstackImage = DockerImageName.parse("localstack/localstack:0.11.3");
    public static LocalStackContainer localstack = new LocalStackContainer(localstackImage)
            .withServices(S3);

    private static AmazonS3 amazonS3;
    private static final String bucketName = "barbershop--web";

    static {
        localstack.start();
        setAmazonS3();
    }

    private static void setAmazonS3() {
        AbstractAwsIT.amazonS3 = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(localstack.getEndpointConfiguration(S3))
                .withCredentials(localstack.getDefaultCredentialsProvider())
                .build();
    }

    public static String getBucketName() {
        return bucketName;
    }

    public static AmazonS3 getAmazonS3() {
        return amazonS3;
    }
}
