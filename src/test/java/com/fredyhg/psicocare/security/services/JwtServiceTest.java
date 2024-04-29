package com.fredyhg.psicocare.security.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;


    @BeforeEach
    void setUp() {
        long refreshExpiration = 604800000;
        long jwtExpiration = 86400000;
        String secretKey = "25a5356a4956aac7944e164e531d5bd4c145d4057aafabe921648ce4b433a19e";

        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", jwtExpiration);
        ReflectionTestUtils.setField(jwtService, "refreshExpiration", refreshExpiration);
    }

    @Test
    void extractUsernameTest() {
        String username = "testUser";
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(username);

        String token = jwtService.generateToken(userDetails);
        assertEquals(username, jwtService.extractUsername(token));
    }

    @Test
    void isTokenValidTest() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");

        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void generateTokenTest() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");

        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
        assertFalse(jwtService.isTokenExpired(token));
    }

    @Test
    void generateRefreshTokenTest() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");

        String refreshToken = jwtService.generateRefreshTokenToken(userDetails);
        assertNotNull(refreshToken);
        assertFalse(jwtService.isTokenExpired(refreshToken));
    }

}