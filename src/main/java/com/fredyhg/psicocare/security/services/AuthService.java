package com.fredyhg.psicocare.security.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fredyhg.psicocare.exceptions.security.UserException;
import com.fredyhg.psicocare.security.models.AuthenticationResponse;
import com.fredyhg.psicocare.security.models.UserModel;
import com.fredyhg.psicocare.security.models.UserToken;
import com.fredyhg.psicocare.security.models.dto.AuthenticationDTO;
import com.fredyhg.psicocare.security.repositories.UserModelRepository;
import com.fredyhg.psicocare.security.repositories.UserTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserModelRepository userModelRepository;

    private final UserService userService;

    private final UserTokenRepository userTokenRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationDTO authenticationDTO){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationDTO.getEmail(),
                        authenticationDTO.getPassword()
                )
        );

        UserModel user = userModelRepository.findByUsername(authenticationDTO.getEmail())
                .orElseThrow();

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshTokenToken(user);
        revokeAllUserTokens(user);

        userService.saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void revokeAllUserTokens(UserModel user){
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
        accountUsername = jwtService.extractUsername(refreshToken);

        if(accountUsername == null) throw new UserException("Error to parse username from token");

        UserModel account = this.userService.findByUsername(accountUsername);

        if(jwtService.isTokenValid(refreshToken, account)){
            String accessToken = jwtService.generateToken(account);
           revokeAllUserTokens(account);
           this.userService.saveUserToken(account, accessToken);

            System.out.println("aq");

           return AuthenticationResponse
                   .builder()
                   .accessToken(accessToken)
                   .refreshToken(refreshToken)
                   .build();

        }

        throw new InternalAuthenticationServiceException("Error to parse token");
    }

}
