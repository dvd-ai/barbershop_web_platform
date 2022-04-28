package com.app.barbershopweb.integrationtests;

import com.app.barbershopweb.integrationtests.container.MailHogContainer;
import com.app.barbershopweb.integrationtests.container.MinioContainer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class AbstractIT {
    private static final String flywayTestMigrationLocation = "classpath:db/migration";

    private static final String bucketName = "barbershop--web";
    private static final String accessKey = "minioadmin";
    private static final String secretKey = accessKey;

    public static MinioContainer minioContainer =
            new MinioContainer(accessKey, secretKey);
    public static PostgreSQLContainer<?> postgreDBContainer = new PostgreSQLContainer<>("postgres:10");
    public static MailHogContainer mailHogContainer = new MailHogContainer();

    static {
        postgreDBContainer.start();
        minioContainer.start();
        mailHogContainer.start();
    }

    @DynamicPropertySource
    static void testProperties(DynamicPropertyRegistry registry) {
        dbTestProperties(registry);
        minioTestProperties(registry);
        mailHogTestProperties(registry);
    }

    private static void dbTestProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url=", postgreDBContainer::getJdbcUrl);
        registry.add("spring.datasource.username=", postgreDBContainer::getUsername);
        registry.add("spring.datasource.password=", postgreDBContainer::getPassword);
        registry.add("spring.flyway.locations=", AbstractIT::getFlywayTestMigrationLocation);
    }

    private static void minioTestProperties(DynamicPropertyRegistry registry) {
        registry.add("minio.bucket.name=", AbstractIT::getBucketName);
        registry.add("minio.url=", minioContainer::getHttpHostAddress);
        registry.add("minio.accessKey=", AbstractIT::getAccessKey);
        registry.add("minio.secretKey=", AbstractIT::getSecretKey);
    }

    private static void mailHogTestProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.mail.host=", mailHogContainer::getSmtpHost);
        registry.add("spring.mail.port=", mailHogContainer::getSmtpPort);
    }

    private static String getFlywayTestMigrationLocation() {
        return flywayTestMigrationLocation;
    }

    public static String getBucketName() {
        return bucketName;
    }

    private static String getAccessKey() {
        return accessKey;
    }

    private static String getSecretKey() {
        return secretKey;
    }
}