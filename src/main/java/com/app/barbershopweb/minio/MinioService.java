package com.app.barbershopweb.minio;

import com.app.barbershopweb.exception.MinioClientException;
import io.minio.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;


@Service
public class MinioService {
    private static final long PART_SIZE = 5_000_000_00;
    private final MinioClient minioClient;

    public MinioService(MinioClient minioClient, @Value("${minio.bucket.name}") String bucket) {
        this.minioClient = minioClient;
        createBucketIfNotExist(bucket);
    }

    public void deleteFile(String bucketName, String key) {

        try {
            RemoveObjectArgs args = RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(key)
                    .build();

            minioClient.removeObject(args);
        } catch (Exception e) {
            throw new MinioClientException(e.getMessage());
        }
    }

    public void uploadFile(String bucketName, String key, MultipartFile multipartFile) {
        try {
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(key)
                    .stream(multipartFile.getInputStream(), -1, PART_SIZE)
                    .contentType(multipartFile.getContentType())
                    .build();

            minioClient.putObject(args);
        } catch (Exception e) {
            throw new MinioClientException(e.getMessage());
        }
    }

    public byte[] downloadFile(String bucketName, String key) {
        try (InputStream is =
                     minioClient.getObject(GetObjectArgs.builder()
                             .bucket(bucketName)
                             .object(key)
                             .build())
        ) {
            return is.readAllBytes();
        } catch (Exception e) {
            if (!e.getMessage().contains("The specified key does not exist."))
                throw new MinioClientException(e.getMessage());
        }
        return new byte[0];
    }

    void createBucketIfNotExist(String bucket) {

        try {
            BucketExistsArgs args = BucketExistsArgs.builder()
                    .bucket(bucket)
                    .build();

            MakeBucketArgs args1 = MakeBucketArgs.builder()
                    .bucket(bucket)
                    .build();

            boolean found = minioClient.bucketExists(args);

            if (!found) {
                minioClient.makeBucket(args1);
            }
        } catch (Exception e) {
            throw new MinioClientException(e.getMessage());
        }
    }
}
