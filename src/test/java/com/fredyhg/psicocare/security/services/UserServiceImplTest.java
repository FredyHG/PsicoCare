package com.fredyhg.psicocare.security.services;

import com.fredyhg.psicocare.exceptions.security.UserException;
import com.fredyhg.psicocare.models.PsychologistModel;
import com.fredyhg.psicocare.security.models.UserModel;
import com.fredyhg.psicocare.security.models.UserToken;
import com.fredyhg.psicocare.security.repositories.UserModelRepository;
import com.fredyhg.psicocare.security.repositories.UserTokenRepository;
import com.fredyhg.psicocare.security.services.impl.JwtServiceImpl;
import com.fredyhg.psicocare.security.services.impl.UserServiceImpl;
import com.fredyhg.psicocare.services.impl.EmailSenderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private UserModelRepository userRepository;

    @Mock
    private UserTokenRepository tokenRepository;

    @Mock
    private JwtServiceImpl jwtServiceImpl;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailSenderServiceImpl emailSenderServiceImpl;

    @Test
    void createUserTest() {
        PsychologistModel psychologist = new PsychologistModel();
        psychologist.setEmail("test@example.com");
        String generatedPassword = "password";
        UserModel savedUser = new UserModel();

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserModel.class))).thenReturn(savedUser);
        when(jwtServiceImpl.generateToken(savedUser)).thenReturn("jwtToken");

        userServiceImpl.createUser(psychologist);

        verify(userRepository).save(any(UserModel.class));
        verify(jwtServiceImpl).generateToken(savedUser);
        verify(emailSenderServiceImpl).sendAccessDetailsEmail(eq(psychologist), anyString());
    }

    @Test
    void saveUserTokenTest() {
        UserModel user = new UserModel();
        String jwtToken = "jwtToken";

        userServiceImpl.saveUserToken(user, jwtToken);

        verify(tokenRepository).save(any(UserToken.class));
    }

    @Test
    void findByUsernameTest() {
        String username = "user@example.com";
        UserModel user = new UserModel();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserModel result = userServiceImpl.findByUsername(username);

        assertEquals(user, result);
    }

    @Test
    void ensureUserExistsByUsernameNotFoundTest() {
        String username = "user@example.com";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> userServiceImpl.ensureUserExistsByUsername(username));
    }

    @Test
    void passwordGeneratorTest() {
        String password = userServiceImpl.passwordGenerator();

        assertEquals(8, password.length());
        assertTrue(password.matches("[A-Za-z0-9]+"));
    }

}