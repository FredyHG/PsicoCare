package com.fredyhg.psicocare.security.services;

import com.fredyhg.psicocare.models.dtos.auth.ChangePasswordPostRequest;
import com.fredyhg.psicocare.security.models.AuthenticationResponse;
import com.fredyhg.psicocare.security.models.UserModel;
import com.fredyhg.psicocare.security.models.dto.AuthenticationDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    AuthenticationResponse authenticate(AuthenticationDTO authenticationDTO);

    void revokeAllUserTokens(UserModel user);

    AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response);

    void changePassword(ChangePasswordPostRequest changePasswordPostRequest, HttpServletRequest request);
}
