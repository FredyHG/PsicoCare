package com.fredyhg.psicocare.security.services;

import com.fredyhg.psicocare.security.services.impl.JwtServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class JwtServiceImplTest {

    @InjectMocks
    private JwtServiceImpl jwtServiceImpl;


    @BeforeEach
    void setUp() {
        long refreshExpiration = 604800000;
        long jwtExpiration = 86400000;
        String secretKey = "25a5356a4956aac7944e164e531d5bd4c145d4057aafabe921648ce4b433a19e";

        ReflectionTestUtils.setField(jwtServiceImpl, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtServiceImpl, "jwtExpiration", jwtExpiration);
        ReflectionTestUtils.setField(jwtServiceImpl, "refreshExpiration", refreshExpiration);
    }

    @Test
    void extractUsernameTest() {
        String username = "testUser";
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(username);

        String token = jwtServiceImpl.generateToken(userDetails);
        assertEquals(username, jwtServiceImpl.extractUsername(token));
    }

    @Test
    void isTokenValidTest() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");

        String token = jwtServiceImpl.generateToken(userDetails);
        assertTrue(jwtServiceImpl.isTokenValid(token, userDetails));
    }

    @Test
    void generateTokenTest() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");

        String token = jwtServiceImpl.generateToken(userDetails);
        assertNotNull(token);
        assertFalse(jwtServiceImpl.isTokenExpired(token));
    }

    @Test
    void generateRefreshTokenTest() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");

        String refreshToken = jwtServiceImpl.generateRefreshTokenToken(userDetails);
        assertNotNull(refreshToken);
        assertFalse(jwtServiceImpl.isTokenExpired(refreshToken));
    }

}