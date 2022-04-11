package com.app.barbershopweb.user;

import com.app.barbershopweb.aws.s3.S3Service;
import com.app.barbershopweb.exception.NotFoundException;
import com.app.barbershopweb.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final S3Service s3Service;

    public UserService(UserRepository userRepository, S3Service s3Service) {
        this.userRepository = userRepository;
        this.s3Service = s3Service;
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
        s3Service.deleteFile(key);
        s3Service.uploadFile(key, profileAvatar);
    }

    public MultipartFile downloadProfileAvatar(Long userId) {
        if (!userRepository.userExistsById(userId)) {
            throw new NotFoundException(
                    List.of("Profile avatar download: User with id " + userId + " wasn't found")
            );
        }

        String key = "profile_avatar_" + userId;
        return s3Service.downloadFile(key);
    }
}
