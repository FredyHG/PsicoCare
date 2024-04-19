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
            @ApiResponse(responseCode = "200", description = "Authentication successfully"),
            @ApiResponse(responseCode = "401", description = "Bad credentials")
    })
    ResponseEntity<AuthenticationResponse> authenticate(AuthenticationDTO authenticationDto);

    @Operation(summary = "Refresh bearer token", description = "Anyone can make this request", tags = "AUTH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Token refreshed successfully"),
            @ApiResponse(responseCode = "403", description = "Invalid token"),
    })
    ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request, HttpServletResponse response);

    @Operation(summary = "Check login", description = "Anyone can make this request", tags = "AUTH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bearer token valid"),
            @ApiResponse(responseCode = "401", description = "Invalid Token"),
    })
    ResponseEntity<Boolean> checkLogin();
}