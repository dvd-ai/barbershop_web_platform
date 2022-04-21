package com.app.barbershopweb.minio;

import com.app.barbershopweb.exception.MinioClientException;
import org.springframework.stereotype.Component;


@Component
public class MinioServiceUtils {

    public void handleNotFoundBucketObject(Exception e) {
        if (!e.getMessage().contains("The specified key does not exist."))
            throw new MinioClientException(e.getMessage());
    }

}
