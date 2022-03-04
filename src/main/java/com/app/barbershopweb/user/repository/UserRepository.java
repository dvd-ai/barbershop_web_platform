package com.app.barbershopweb.user.repository;

import com.app.barbershopweb.user.Users;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository {
    Long addUser(Users barbershop);
    Optional<Users> findUserById(Long id);
    Optional<Users>updateUser(Users barbershop);
    List<Users> getUsers();
    void deleteUserById(Long id);
    boolean userExistsById(Long id);
}
