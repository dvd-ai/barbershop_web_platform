package com.app.barbershopweb.user.avatar;

import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.minio.MinioService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UserAvatarService {

    private final MinioService minioService;
    private final String bucketName;

    public UserAvatarService(MinioService minioService, @Value("${minio.bucket.name}") String bucketName) {
        this.minioService = minioService;
        this.bucketName = bucketName;
    }

    public void uploadProfileAvatar(Long userId, MultipartFile profileAvatar) {

        String objectKey = getObjectKeyIfUserExists(userId);
        minioService.deleteFile(bucketName, objectKey);
        minioService.uploadFile(bucketName, objectKey, profileAvatar);
    }

    public byte[] downloadProfileAvatar(Long userId) {
        byte[] avatar = minioService.downloadFile(bucketName, getObjectKeyIfUserExists(userId));

        if (avatar.length == 0) {
            throw new NotFoundException(
                    List.of(
                            "No profile avatar for user with id " + userId
                    )
            );
        }

        return avatar;
    }

    public void deleteProfileAvatar(Long userId) {
        minioService.deleteFile(bucketName, getObjectKeyIfUserExists(userId));
    }

    private String getObjectKeyIfUserExists(Long userId) {
        return "profile_avatar_" + userId;
    }
}
