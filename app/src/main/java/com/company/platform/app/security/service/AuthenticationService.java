package com.company.platform.app.security.service;

import com.company.platform.app.security.controller.AuthRequest;
import com.company.platform.app.security.controller.AuthResponse;
import com.company.platform.app.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthResponse authenticate(AuthRequest request) {

        log.info("Authentication attempt for username={}", request.getUsername());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(request.getUsername());

        String token = jwtService.generateToken(userDetails);

        log.info("Authentication successful for username={}", userDetails.getUsername());

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .username(userDetails.getUsername())
                .build();
    }
}
