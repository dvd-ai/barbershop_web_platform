package com.app.barbershopweb.user.registration;

import com.app.barbershopweb.exception.DbUniqueConstraintsViolationException;
import com.app.barbershopweb.user.credentials.UserCredentialsRepository;
import com.app.barbershopweb.user.credentials.UserCredentials;
import com.app.barbershopweb.user.crud.User;
import com.app.barbershopweb.user.crud.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final UserCredentialsRepository userCredentialsRepository;

    public UserRegistrationService(UserRepository userRepository, UserCredentialsRepository userCredentialsRepository) {
        this.userRepository = userRepository;
        this.userCredentialsRepository = userCredentialsRepository;
    }

    public Long register(UserRegistrationDto userRegistrationDto) {
        Long userId = setAndAddUser(userRegistrationDto);
        return setAndAddUserCredentials(userId, userRegistrationDto);
    }

    private Long setAndAddUser(UserRegistrationDto userRegistrationDto) {
        User user = new User();
        user.setFirstName(userRegistrationDto.firstName());
        user.setLastName(userRegistrationDto.lastName());
        user.setRegistrationDate(userRegistrationDto.registrationDate());
        user.setRole("user");
        user.setPhoneNumber(userRegistrationDto.phoneNumber());
        user.setEmail(userRegistrationDto.email());

        return userRepository.addUser(user);
    }

    private Long setAndAddUserCredentials(Long userId, UserRegistrationDto userRegistrationDto) {
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUserId(userId);
        userCredentials.setUsername(userRegistrationDto.username());
        userCredentials.setPassword(new BCryptPasswordEncoder().encode(userRegistrationDto.password()));
        userCredentials.setEnabled(true);

        try {
            return userCredentialsRepository.addUserCredentials(userCredentials);
        } catch (DbUniqueConstraintsViolationException e) {
            userRepository.deleteUserById(userId);
            throw new DbUniqueConstraintsViolationException(e.getMessages());
        }
    }
}
