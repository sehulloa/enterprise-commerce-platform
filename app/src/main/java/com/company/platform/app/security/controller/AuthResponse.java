package com.company.platform.app.security.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "Authentication response")
@Getter
@Builder
public class AuthResponse {

    @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;

    @Schema(description = "Authorization type", example = "admin")
    private String type;

    @Schema(description = "Username", example = "admin")
    private String username;
}
