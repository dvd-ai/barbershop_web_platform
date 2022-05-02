package com.app.barbershopweb.user.registration;

import com.app.barbershopweb.exception.DbUniqueConstraintsViolationException;
import com.app.barbershopweb.user.credentials.UserCredentials;
import com.app.barbershopweb.user.credentials.UserCredentialsRepository;
import com.app.barbershopweb.user.crud.User;
import com.app.barbershopweb.user.crud.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.app.barbershopweb.user.credentials.UserCredentials_Entities__TestConstants.USER_CREDENTIALS_VALID_ENTITY;
import static com.app.barbershopweb.user.credentials.error.UserCredentialsErrorMessage_Uk__TestConstants.USER_CREDENTIALS_ERR_UK_USERNAME;
import static com.app.barbershopweb.user.crud.constants.UserMetadata__TestConstants.USERS_VALID_USER_ID;
import static com.app.barbershopweb.user.registration.constants.UserRegistration_Dto__TestConstants.USER_REGISTRATION_VALID_DTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserCredentialsRepository userCredentialsRepository;

    @InjectMocks
    UserRegistrationService userRegistrationService;

    @Test
    void register() {
        when(
                userCredentialsRepository.credentialsExistByUsername(
                    USER_CREDENTIALS_VALID_ENTITY.getUsername()
                )
        ).thenReturn(false);

        when(
                userRepository.addUser(any(User.class))
        ).thenReturn(USERS_VALID_USER_ID);

        when(
                userCredentialsRepository.addUserCredentials(any(UserCredentials.class))
        ).thenReturn(USERS_VALID_USER_ID);

        assertEquals(USERS_VALID_USER_ID, userRegistrationService.register(USER_REGISTRATION_VALID_DTO));
    }

    @Test
    void register_usernameCv() {
        when(
                userCredentialsRepository.credentialsExistByUsername(
                        USER_CREDENTIALS_VALID_ENTITY.getUsername()
                )
        ).thenReturn(true);

        List<String> messages = assertThrows(
                DbUniqueConstraintsViolationException.class, () ->
                        userRegistrationService.register(USER_REGISTRATION_VALID_DTO)
        ).getMessages();

        assertEquals(1, messages.size());
        assertTrue(messages.contains(USER_CREDENTIALS_ERR_UK_USERNAME));
    }
}