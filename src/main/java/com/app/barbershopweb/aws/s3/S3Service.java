package com.app.barbershopweb.aws.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.app.barbershopweb.exception.FileException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.app.barbershopweb.util.MultipartFileUtil.convertMultipartFileToFile;


@Service
public class S3Service {
    private final AmazonS3 amazonS3;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public void deleteFile(String bucketName, String key) {
        amazonS3.deleteObject(bucketName, key);
    }

    public void uploadFile(String bucketName, String key, MultipartFile multipartFile) {
        File file = convertMultipartFileToFile(multipartFile);
        amazonS3.putObject(bucketName, key, file);
        file.delete();
    }

    public byte[] downloadFile(String bucketName, String key) {
        byte[] bytes = new byte[0];

        try {
            S3Object s3Object = amazonS3.getObject(bucketName, key);
            S3ObjectInputStream inputStream = s3Object.getObjectContent();
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new FileException(List.of(e.getMessage()));

        } catch (SdkClientException e) {
            if (e.getMessage().contains("The specified key does not exist."))
                return bytes;
            else throw new AmazonServiceException(e.getMessage());
        }
    }
}
