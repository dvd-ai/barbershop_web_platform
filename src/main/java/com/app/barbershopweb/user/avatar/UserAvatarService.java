package com.app.barbershopweb.user.avatar;

import com.app.barbershopweb.aws.s3.S3Service;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.user.crud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UserAvatarService {

    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final String bucketName;

    public UserAvatarService(UserRepository userRepository, S3Service s3Service, @Value("${AWS_S3_BUCKET_NAME}") String bucketName) {
        this.userRepository = userRepository;
        this.s3Service = s3Service;
        this.bucketName = bucketName;
    }

    public void uploadProfileAvatar(Long userId, MultipartFile profileAvatar) {
        if (!userRepository.userExistsById(userId)) {
            throw new NotFoundException(
                    List.of("Profile avatar upload: User with id " + userId + " wasn't found")
            );
        }

        String key = "profile_avatar_" + userId;
        s3Service.deleteFile(bucketName, key);
        s3Service.uploadFile(bucketName, key, profileAvatar);
    }

    public ByteArrayResource downloadProfileAvatar(Long userId) {
        if (!userRepository.userExistsById(userId)) {
            throw new NotFoundException(
                    List.of("Profile avatar download: User with id " + userId + " wasn't found")
            );
        }

        String key = "profile_avatar_" + userId;
        return new ByteArrayResource(s3Service.downloadFile(bucketName, key));
    }
}
