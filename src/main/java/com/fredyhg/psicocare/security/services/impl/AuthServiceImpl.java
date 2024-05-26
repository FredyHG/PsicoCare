package com.fredyhg.psicocare.security.services.impl;

import com.fredyhg.psicocare.exceptions.security.InvalidTokenException;
import com.fredyhg.psicocare.exceptions.security.PasswordInvalidException;
import com.fredyhg.psicocare.exceptions.security.PasswordMismatchException;
import com.fredyhg.psicocare.exceptions.security.UserException;
import com.fredyhg.psicocare.models.dtos.auth.ChangePasswordPostRequest;
import com.fredyhg.psicocare.security.models.AuthenticationResponse;
import com.fredyhg.psicocare.security.models.UserModel;
import com.fredyhg.psicocare.security.models.UserToken;
import com.fredyhg.psicocare.security.models.dto.AuthenticationDTO;
import com.fredyhg.psicocare.security.repositories.UserModelRepository;
import com.fredyhg.psicocare.security.repositories.UserTokenRepository;
import com.fredyhg.psicocare.security.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserModelRepository userModelRepository;

    private final UserServiceImpl userServiceImpl;

    private final UserTokenRepository userTokenRepository;

    private final JwtServiceImpl jwtServiceImpl;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse authenticate(AuthenticationDTO authenticationDTO){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationDTO.getEmail(),
                        authenticationDTO.getPassword()
                )
        );

        UserModel user = userModelRepository.findByUsername(authenticationDTO.getEmail())
                .orElseThrow();

        String jwtToken = jwtServiceImpl.generateToken(user);
        String refreshToken = jwtServiceImpl.generateRefreshTokenToken(user);
        revokeAllUserTokens(user);

        userServiceImpl.saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void revokeAllUserTokens(UserModel user){
        List<UserToken> validAccountTokens = userTokenRepository.findAllValidTokenByUser(user.getId());

        if(validAccountTokens.isEmpty()) return;

        validAccountTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        userTokenRepository.saveAll(validAccountTokens);
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response){
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String accountUsername;


        if(authHeader == null || !authHeader.startsWith("Bearer ")) return null;

        refreshToken = authHeader.substring(7);
        accountUsername = jwtServiceImpl.extractUsername(refreshToken);

        if(accountUsername == null) throw new UserException("Error to parse username from token");

        UserModel account = this.userServiceImpl.findByUsername(accountUsername);

        if(jwtServiceImpl.isTokenValid(refreshToken, account)){
            String accessToken = jwtServiceImpl.generateToken(account);
           revokeAllUserTokens(account);
           this.userServiceImpl.saveUserToken(account, accessToken);

           return AuthenticationResponse
                   .builder()
                   .accessToken(accessToken)
                   .refreshToken(refreshToken)
                   .build();

        }

        throw new InternalAuthenticationServiceException("Error to parse token");
    }

    public void changePassword(ChangePasswordPostRequest changePasswordPostRequest, HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String token;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new InvalidTokenException("Invalid token");
        }

        token = authHeader.substring(7);

        String username = jwtServiceImpl.extractUsername(token);

        UserModel user = userModelRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User name not found"));

        if(!passwordEncoder.matches(changePasswordPostRequest.getOldPassword(), user.getPassword())){
            throw new PasswordMismatchException("Password mismatch");
        }


        String newPassword = changePasswordPostRequest.getNewPassword();

        if(newPassword.isBlank() || newPassword.length() < 8){
            throw new PasswordInvalidException("Password invalid format");
        }

        user.setPassword(passwordEncoder.encode(newPassword));

        userModelRepository.save(user);
    }
}
