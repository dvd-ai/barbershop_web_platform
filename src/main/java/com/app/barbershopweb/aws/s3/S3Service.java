package com.app.barbershopweb.aws.s3;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static com.app.barbershopweb.util.MultipartFileUtil.convertMultipartFileToFile;


@Service
public class S3Service {
    private final AmazonS3 amazonS3;
    private final S3ServiceUtils s3ServiceUtils;

    public S3Service(AmazonS3 amazonS3, S3ServiceUtils s3ServiceUtils) {
        this.amazonS3 = amazonS3;
        this.s3ServiceUtils = s3ServiceUtils;
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
            return s3ServiceUtils.toByteArray(inputStream);

        } catch (SdkClientException e) {
            s3ServiceUtils.handleNotFoundBucketObject(e);
        }
        return bytes;
    }
}
