package com.fredyhg.psicocare.controllers;

import com.fredyhg.psicocare.controllers.interfaces.AuthController;
import com.fredyhg.psicocare.exceptions.utils.ResponseMessage;
import com.fredyhg.psicocare.models.dtos.auth.ChangePasswordPostRequest;
import com.fredyhg.psicocare.security.models.AuthenticationResponse;
import com.fredyhg.psicocare.security.models.dto.AuthenticationDTO;
import com.fredyhg.psicocare.security.services.AuthService;
import com.fredyhg.psicocare.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {
    private final AuthService authService;

    @PostMapping("/authenticate")
    @Override
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationDTO authenticationDto) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.authenticate(authenticationDto));
    }

    @PostMapping("/refresh-token")
    @Override
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.status(HttpStatus.OK).body(authService.refreshToken(request, response));
    }

    @PostMapping("/check-login")
    public ResponseEntity<Boolean> checkLogin(){
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @PostMapping("/change-password")
    public ResponseEntity<ResponseMessage> changePassword(@RequestBody ChangePasswordPostRequest changePasswordPostRequest, HttpServletRequest httpServletRequest){

        authService.changePassword(changePasswordPostRequest, httpServletRequest);

        return ResponseEntity.status(HttpStatus.OK).body(Utils.createResponseMessage(
                200,
                "Password changed successfully"
        ));
    }

}
