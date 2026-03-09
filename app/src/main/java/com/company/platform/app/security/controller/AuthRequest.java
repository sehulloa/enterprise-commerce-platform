package com.company.platform.app.security.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {

    private String username;
    private String password;
}
