package com.app.barbershopweb.integrationtests.config.minio;

import com.app.barbershopweb.integrationtests.AbstractMinioIT;
import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


@Configuration
public class MinioTestConfig {

    @Bean
    @Primary
    public MinioClient minioClient() {
        return AbstractMinioIT.getMinioClient();
    }
}
