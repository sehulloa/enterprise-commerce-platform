package com.company.platform.app.security.service;

import com.company.platform.app.security.controller.AuthRequest;
import com.company.platform.app.security.controller.AuthResponse;
import com.company.platform.app.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthResponse authenticate(AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(request.getUsername());

        String token = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .username(userDetails.getUsername())
                .build();
    }
}
