package com.fredyhg.psicocare.config.init;

import com.fredyhg.psicocare.security.enums.Role;
import com.fredyhg.psicocare.security.models.UserModel;
import com.fredyhg.psicocare.security.repositories.UserModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommandInitRunner implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;

    private final UserModelRepository userModelRepository;

    @Value("${application.security.default-user}")
    private String defaultUsername;

    @Value("${application.security.default-password}")
    private String defaultPassword;

    @Override
    public void run(String... args) {

        String passwordEncrypted = passwordEncoder.encode(defaultPassword);

        UserModel admin = UserModel.builder()
                .username(defaultUsername)
                .role(Role.ROLE_ROOT)
                .psychologistModel(null)
                .password(passwordEncrypted)
                .build();

        Optional<UserModel> userExists = userModelRepository.findByUsername(admin.getUsername());


        if(userExists.isPresent()){
            return;
        }

        userModelRepository.save(admin);
    }
}
