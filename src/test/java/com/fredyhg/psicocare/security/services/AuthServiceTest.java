package com.fredyhg.psicocare.security.services;

import com.fredyhg.psicocare.security.models.AuthenticationResponse;
import com.fredyhg.psicocare.security.models.UserModel;
import com.fredyhg.psicocare.security.models.dto.AuthenticationDTO;
import com.fredyhg.psicocare.security.repositories.UserModelRepository;
import com.fredyhg.psicocare.security.repositories.UserTokenRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserModelRepository userModelRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserTokenRepository userTokenRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AuthService authService;


    @Test
    void authenticateSuccess() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO("user@example.com", "password");
        UserModel user = new UserModel();
        when(userModelRepository.findByUsername(authenticationDTO.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwtToken");
        when(jwtService.generateRefreshTokenToken(user)).thenReturn("refreshToken");

        AuthenticationResponse response = authService.authenticate(authenticationDTO);

        assertNotNull(response);
        assertEquals("jwtToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        verify(userService).saveUserToken(user, "jwtToken");
    }

    @Test
    void refreshTokenSuccess() throws IOException {

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer validRefreshToken");
        UserModel user = new UserModel();

        when(jwtService.extractUsername("validRefreshToken")).thenReturn("user@example.com");
        when(userService.findByUsername("user@example.com")).thenReturn(user);
        when(jwtService.isTokenValid("validRefreshToken", user)).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("newAccessToken");

        ServletOutputStream outputStream = mock(ServletOutputStream.class);
        when(response.getOutputStream()).thenReturn(outputStream);

        authService.refreshToken(request, response);

        verify(response).getOutputStream();
    }

}