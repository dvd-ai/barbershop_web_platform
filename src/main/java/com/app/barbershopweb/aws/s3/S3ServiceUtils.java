package com.app.barbershopweb.aws.s3;

import com.amazonaws.AmazonClientException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.app.barbershopweb.exception.FileException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class S3ServiceUtils {

    public byte[] toByteArray(S3ObjectInputStream inputStream) {
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new FileException(List.of(e.getMessage()));
        }
    }

    public void handleNotFoundBucketObject(SdkClientException e) {
        if (!e.getMessage().contains("The specified key does not exist."))
            throw new AmazonClientException(e.getMessage());
    }

}
