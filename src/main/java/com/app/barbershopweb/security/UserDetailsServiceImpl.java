package com.app.barbershopweb.security;

import com.app.barbershopweb.user.credentials.UserCredentials;
import com.app.barbershopweb.user.credentials.UserCredentialsRepository;
import com.app.barbershopweb.user.crud.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserCredentialsRepository userCredentialsRepository;

    public UserDetailsServiceImpl(UserRepository userRepository, UserCredentialsRepository userCredentialsRepository) {
        this.userRepository = userRepository;
        this.userCredentialsRepository = userCredentialsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<UserCredentials> customer = userCredentialsRepository.findByUsername(username);
        if (customer.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        UserCredentials userCredentials = customer.get();
        com.app.barbershopweb.user.crud.User foundUser = userRepository.findUserById(userCredentials.getUserId()).get();
        return SecurityUser.fromUser(userCredentials, foundUser);
    }

}

