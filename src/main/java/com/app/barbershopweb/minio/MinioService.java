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
    private final MinioServiceUtils minioServiceUtils;

    public MinioService(MinioClient minioClient, MinioServiceUtils minioServiceUtils,
                        @Value("${minio.bucket.name}") String bucket) {
        this.minioClient = minioClient;
        this.minioServiceUtils = minioServiceUtils;

        try {
            boolean found = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucket)
                            .build()
            );
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucket)
                        .build());
            }
        } catch (Exception e) {
            throw new MinioClientException(e.getMessage());
        }

    }

    public void deleteFile(String bucketName, String key) {

        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(bucketName).object(key).build()
            );
        } catch (Exception e) {
            throw new MinioClientException(e.getMessage());
        }
    }

    public void uploadFile(String bucketName, String key, MultipartFile multipartFile) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName).object(key).stream(
                                    multipartFile.getInputStream(), -1, PART_SIZE)
                            .contentType(multipartFile.getContentType())
                            .build()
            );
        } catch (Exception e) {
            throw new MinioClientException(e.getMessage());
        }
    }

    public byte[] downloadFile(String bucketName, String key) {
        InputStream is;

        try {
            is = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(key)
                            .build());
            return is.readAllBytes();
        } catch (Exception e) {
            minioServiceUtils.handleNotFoundBucketObject(e);
        }
        return new byte[0];
    }
}
