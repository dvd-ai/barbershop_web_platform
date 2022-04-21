package com.app.barbershopweb.user.avatar;

import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.minio.MinioService;
import com.app.barbershopweb.user.crud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UserAvatarService {

    private final UserRepository userRepository;
    private final MinioService minioService;
    private final String bucketName;

    public UserAvatarService(UserRepository userRepository, MinioService minioService, @Value("${minio.bucket.name}") String bucketName) {
        this.userRepository = userRepository;
        this.minioService = minioService;
        this.bucketName = bucketName;
    }

    public void uploadProfileAvatar(Long userId, MultipartFile profileAvatar) {

        String objectKey = getObjectKeyIfUserExists(userId);
        minioService.deleteFile(bucketName, objectKey);
        minioService.uploadFile(bucketName, objectKey, profileAvatar);
    }

    public ByteArrayResource downloadProfileAvatar(Long userId) {
        return new ByteArrayResource(minioService.downloadFile(bucketName, getObjectKeyIfUserExists(userId)));
    }

    public void deleteProfileAvatar(Long userId) {
        minioService.deleteFile(bucketName, getObjectKeyIfUserExists(userId));
    }

    private String getObjectKeyIfUserExists(Long userId) {
        if (!userRepository.userExistsById(userId)) {
            throw new NotFoundException(
                    List.of("User with id " + userId + " not found")
            );
        }
        return "profile_avatar_" + userId;
    }
}
