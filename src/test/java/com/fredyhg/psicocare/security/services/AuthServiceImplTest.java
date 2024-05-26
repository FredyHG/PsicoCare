package com.fredyhg.psicocare.security.services;

import com.fredyhg.psicocare.security.models.AuthenticationResponse;
import com.fredyhg.psicocare.security.models.UserModel;
import com.fredyhg.psicocare.security.models.dto.AuthenticationDTO;
import com.fredyhg.psicocare.security.repositories.UserModelRepository;
import com.fredyhg.psicocare.security.repositories.UserTokenRepository;
import com.fredyhg.psicocare.security.services.impl.AuthServiceImpl;
import com.fredyhg.psicocare.security.services.impl.JwtServiceImpl;
import com.fredyhg.psicocare.security.services.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserModelRepository userModelRepository;

    @Mock
    private UserServiceImpl userServiceImpl;

    @Mock
    private UserTokenRepository userTokenRepository;

    @Mock
    private JwtServiceImpl jwtServiceImpl;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    @Test
    void authenticateSuccess() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO("user@example.com", "password");
        UserModel user = new UserModel();
        when(userModelRepository.findByUsername(authenticationDTO.getEmail())).thenReturn(Optional.of(user));
        when(jwtServiceImpl.generateToken(user)).thenReturn("jwtToken");
        when(jwtServiceImpl.generateRefreshTokenToken(user)).thenReturn("refreshToken");

        AuthenticationResponse response = authServiceImpl.authenticate(authenticationDTO);

        assertNotNull(response);
        assertEquals("jwtToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        verify(userServiceImpl).saveUserToken(user, "jwtToken");
    }

    @Test
    void refreshTokenSuccess() {

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer validRefreshToken");
        UserModel user = new UserModel();

        when(jwtServiceImpl.extractUsername("validRefreshToken")).thenReturn("user@example.com");
        when(userServiceImpl.findByUsername("user@example.com")).thenReturn(user);
        when(jwtServiceImpl.isTokenValid("validRefreshToken", user)).thenReturn(true);
        when(jwtServiceImpl.generateToken(user)).thenReturn("newAccessToken");


        AuthenticationResponse authenticationResponse = authServiceImpl.refreshToken(request, response);

        assertEquals("validRefreshToken", authenticationResponse.getRefreshToken() );

    }
}