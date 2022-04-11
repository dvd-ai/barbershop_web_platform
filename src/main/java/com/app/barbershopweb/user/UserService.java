package com.app.barbershopweb.user;

import com.app.barbershopweb.aws.s3.S3Service;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final String bucketName;

    public UserService(UserRepository userRepository, S3Service s3Service,
                       @Value("${AWS_S3_BUCKET_NAME}")String bucketName) {
        this.userRepository = userRepository;
        this.s3Service = s3Service;
        this.bucketName = bucketName;
    }

    public Long addUser(Users users) {
        return userRepository.addUser(users);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteUserById(id);
    }

    public Optional<Users> updateUser(Users users) {
        return userRepository.updateUser(users);
    }

    public Optional<Users> findUserById(Long id) {
        return userRepository.findUserById(id);
    }

    public List<Users> getUsers() {
        return userRepository.getUsers();
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
