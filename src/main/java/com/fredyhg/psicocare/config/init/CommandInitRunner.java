package com.fredyhg.psicocare.config.init;

import com.fredyhg.psicocare.security.enums.Role;
import com.fredyhg.psicocare.security.models.UserModel;
import com.fredyhg.psicocare.security.repositories.UserModelRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class CommandInitRunner implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;

    private final UserModelRepository userModelRepository;

    @Override
    public void run(String... args) throws Exception {

        String passwordEncrypt = passwordEncoder.encode("admin");

        UserModel admin = UserModel.builder()
                .username("admin")
                .role(Role.ROLE_ROOT)
                .psychologistModel(null)
                .password(passwordEncrypt)
                .build();

        Optional<UserModel> userExists = userModelRepository.findByUsername(admin.getUsername());

        if(userExists.isPresent()){
            return;
        }

        userModelRepository.save(admin);
    }
}
