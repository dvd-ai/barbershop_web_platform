package com.app.barbershopweb.user.crud;

import com.app.barbershopweb.user.crud.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long addUser(User users) {
        return userRepository.addUser(users);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteUserById(id);
    }

    public Optional<User> updateUser(User users) {
        return userRepository.updateUser(users);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findUserById(id);
    }

    public List<User> getUsers() {
        return userRepository.getUsers();
    }


}
