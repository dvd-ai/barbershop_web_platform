package com.app.barbershopweb.integrationtests.config.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.app.barbershopweb.integrationtests.AbstractAwsIT;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class S3TestConfig {

    @Bean
    @Primary
    public AmazonS3 s3client() {
        return AbstractAwsIT.getAmazonS3();
    }
}
