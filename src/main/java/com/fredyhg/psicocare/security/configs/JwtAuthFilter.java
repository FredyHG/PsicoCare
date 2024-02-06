package com.fredyhg.psicocare.security.configs;

import com.fredyhg.psicocare.exceptions.security.AuthenticationException;
import com.fredyhg.psicocare.exceptions.security.WriterException;
import com.fredyhg.psicocare.security.repositories.UserTokenRepository;
import com.fredyhg.psicocare.security.services.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    private final UserTokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            if (isAuthApiRequest(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            final String authHeader = request.getHeader("Authorization");
            if (isInvalidAuthHeader(authHeader)) {
                filterChain.doFilter(request, response);
                return;
            }

            String jwt = extractJwt(authHeader);
            String username = jwtService.extractUsername(jwt);

            if (username != null && isUserNotAuthenticated()) {
                processAuthentication(request, jwt, username);
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            sendErrorResponse(response, ex);
        } catch (IOException | ServletException ex) {
            throw new AuthenticationException("Error to validate token");
        }
    }

    private boolean isAuthApiRequest(HttpServletRequest request) {
        return request.getServletPath().contains("/api/auth");
    }

    private boolean isInvalidAuthHeader(String authHeader) {
        return authHeader == null || !authHeader.startsWith("Bearer ");
    }

    private String extractJwt(String authHeader) {
        return authHeader.substring(7);
    }

    private boolean isUserNotAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private void processAuthentication(HttpServletRequest request, String jwt, String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (isTokenValid(jwt, userDetails)) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }

    private boolean isTokenValid(String jwt, UserDetails userDetails) {
        return jwtService.isTokenValid(jwt, userDetails) &&
                tokenRepository.findByToken(jwt)
                        .map(t -> !t.isExpired() && !t.isRevoked())
                        .orElse(false);
    }

    private void sendErrorResponse(HttpServletResponse response, ExpiredJwtException ex) {
        response.setContentType("application/json");
        response.setStatus(HttpStatus.CONFLICT.value());
        try {
            response.getWriter().write("{"
                    + "\"statusCode\": 409,"
                    + "\"timestamp\": \"" + new Date() + "\","
                    + "\"message\": \"Token expired\","
                    + "\"description\": \"" + ex.getMessage() + "\""
                    + "}");
        } catch (IOException e) {
            throw new WriterException(e.getMessage());
        }
    }
}
