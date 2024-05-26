package com.fredyhg.psicocare.security.services;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String extractUsername(String token);

    Date extractExpiration(String token);

    Claims extractAllClaims(String token);

    String generateToken(UserDetails userDetails);

    String generateToken(Map<String, Object> extraClaims,
                         UserDetails userDetails);

    String generateRefreshTokenToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);

    boolean isTokenExpired(String token);

    String buildToken(Map<String, Object> extraClaims,
                      UserDetails userDetails,
                      long expiration
    );

    public Key getSignInKey();
}
