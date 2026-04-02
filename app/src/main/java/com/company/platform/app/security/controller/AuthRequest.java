package com.company.platform.app.security.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Authentication request")
@Getter
@Setter
public class AuthRequest {

    @Schema(description = "Username", example = "admin")
    private String username;

    @Schema(description = "User password", example = "Admin123*")
    private String password;
}
