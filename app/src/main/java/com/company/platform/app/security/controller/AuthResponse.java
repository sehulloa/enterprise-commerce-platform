package com.company.platform.app.security.controller;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {

    private String token;
    private String type;
    private String username;
}
