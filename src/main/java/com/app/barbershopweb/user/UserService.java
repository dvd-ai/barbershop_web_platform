package com.app.barbershopweb.user;

import com.app.barbershopweb.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
