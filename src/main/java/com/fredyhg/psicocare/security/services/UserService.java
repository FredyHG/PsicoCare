package com.fredyhg.psicocare.security.services;

import com.fredyhg.psicocare.exceptions.security.UserException;
import com.fredyhg.psicocare.models.PsychologistModel;
import com.fredyhg.psicocare.security.enums.Role;
import com.fredyhg.psicocare.security.enums.TokenType;
import com.fredyhg.psicocare.security.models.UserModel;
import com.fredyhg.psicocare.security.models.UserToken;
import com.fredyhg.psicocare.security.repositories.UserModelRepository;
import com.fredyhg.psicocare.security.repositories.UserTokenRepository;
import com.fredyhg.psicocare.services.impl.EmailSenderServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserModelRepository userRepository;

    private final UserTokenRepository tokenRepository;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final EmailSenderServiceImpl emailSenderServiceImpl;

    @Transactional
    public void createUser(PsychologistModel psychologist){

        String passwordGen = passwordGenerator();

        UserModel user = UserModel.builder()
                .username(psychologist.getEmail())
                .password(passwordEncoder.encode(passwordGen))
                .role(Role.ROLE_PSYCHOLOGIST)
                .psychologistModel(psychologist)
                .build();

        UserModel savedUser = userRepository.save(user);

        String jwtToken = jwtService.generateToken(savedUser);
        jwtService.generateRefreshTokenToken(savedUser);

        saveUserToken(savedUser, jwtToken);

        emailSenderServiceImpl.sendAccessDetailsEmail(psychologist, passwordGen);
    }

    @Transactional
    public void saveUserToken(UserModel user, String jwtToken) {
        var token = UserToken.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public UserModel findByUsername(String accountUsername) {
        return this.ensureUserExistsByUsername(accountUsername);
    }

    public UserModel ensureUserExistsByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new UserException("User not found"));
    }

    public String passwordGenerator(){
        SecureRandom random = new SecureRandom();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        return IntStream.range(0, 8)
                .map(i -> chars.charAt(random.nextInt(chars.length())))
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.joining());
    }
}
