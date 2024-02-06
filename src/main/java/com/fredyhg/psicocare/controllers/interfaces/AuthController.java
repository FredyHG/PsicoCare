package com.fredyhg.psicocare.controllers.interfaces;

import com.fredyhg.psicocare.security.models.AuthenticationResponse;
import com.fredyhg.psicocare.security.models.dto.AuthenticationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthController {

    @Operation(summary = "Authentication user", description = "Anyone can make this request", tags = "AUTH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully"),
            @ApiResponse(responseCode = "400", description = "User credentials error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request")
    })
    ResponseEntity<AuthenticationResponse> authenticate(AuthenticationDTO authenticationDto);

    @Operation(summary = "Authentication user", description = "Anyone can make this request", tags = "AUTH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully"),
            @ApiResponse(responseCode = "400", description = "User credentials error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized request")
    })
    void refreshToken(HttpServletRequest request, HttpServletResponse response);

}