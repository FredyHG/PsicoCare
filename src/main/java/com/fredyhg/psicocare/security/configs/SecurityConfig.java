package com.fredyhg.psicocare.security.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**", "/", "/status").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/authenticate", "/api/auth/refresh-token").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/patient/all", "/api/patient/filter", "/api/therapy/pending",
                                "/api/therapy/filter", "/api/psychologist/byToken", "/api/psychologist/all", "/api/therapy/all",
                                "/api/therapy/allWithStatus", "/api/psychologist/filter").hasAnyRole("PSYCHOLOGIST", "ROOT")
                        .requestMatchers(HttpMethod.DELETE, "/api/patient/delete").hasAnyRole("PSYCHOLOGIST")
                        .requestMatchers(HttpMethod.POST, "/api/auth/check-login", "/api/therapy/schedule", "/api/patient/create", "/api/psychologist/create", "/api/auth/change-password").hasAnyRole("PSYCHOLOGIST", "ROOT")
                        .requestMatchers(HttpMethod.PUT, "/api/patient/edit").hasAnyRole("PSYCHOLOGIST", "ROOT")
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("Access-Control-Allow-Headers","Access-Control-Allow-Origin","Access-Control-Request-Method", "Access-Control-Request-Headers","Origin","Cache-Control", "Content-Type", "Authorization"));
        configuration.setAllowedMethods(List.of("DELETE", "GET", "POST", "PATCH", "PUT"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
