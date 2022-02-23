package com.app.barbershopweb.integrationtests;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class AbstractIT {

    public static PostgreSQLContainer<?> postgreDBContainer = new PostgreSQLContainer<>("postgres:9.6.12");

    static {
        postgreDBContainer.start();
    }

    @DynamicPropertySource
    static void testProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url=", postgreDBContainer::getJdbcUrl);
        registry.add("spring.datasource.username=", postgreDBContainer::getUsername);
        registry.add("spring.datasource.password=", postgreDBContainer::getPassword);
    }
}