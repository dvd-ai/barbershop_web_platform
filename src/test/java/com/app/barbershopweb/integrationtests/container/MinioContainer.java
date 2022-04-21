package com.app.barbershopweb.integrationtests.container;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class MinioContainer extends GenericContainer<MinioContainer> {
    private static final String IMAGE = "minio/minio:RELEASE.2020-10-18T21-54-12Z";
    private static final Integer DEFAULT_PORT = 9000;
    private static final String MINIO_ACCESS_KEY = "MINIO_ACCESS_KEY";
    private static final String MINIO_SECRET_KEY = "MINIO_SECRET_KEY";
    private static final String SERVER_COMMAND = "server";
    private static final String DEFAULT_STORAGE_DIRECTORY = "/data";

    public MinioContainer(String accessKey, String secretKey) {
        this(IMAGE, accessKey, secretKey);
    }

    private MinioContainer(String dockerImageName, String accessKey, String secretKey) {
        super(dockerImageName);
        addExposedPort(DEFAULT_PORT);
        withEnv(MINIO_ACCESS_KEY, accessKey);
        withEnv(MINIO_SECRET_KEY, secretKey);
        withCommand(SERVER_COMMAND, DEFAULT_STORAGE_DIRECTORY);
        setWaitStrategy(Wait.forListeningPort());
    }

    public String getHttpHostAddress() {
        return "http://" + getContainerIpAddress() + ":" + getMappedPort(DEFAULT_PORT);
    }
}
