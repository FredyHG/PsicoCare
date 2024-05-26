package com.fredyhg.psicocare.security.services;

import com.fredyhg.psicocare.models.PsychologistModel;
import com.fredyhg.psicocare.security.models.UserModel;

public interface UserService {
    void createUser(PsychologistModel psychologist);

    void saveUserToken(UserModel user, String jwtToken);

    UserModel findByUsername(String accountUsername);

    UserModel ensureUserExistsByUsername(String username);

    String passwordGenerator();
}
