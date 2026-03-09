package com.company.platform.app.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

    public static void main(String[] args) {
        String rawPassword = "Admin123*";
        String encoded = new BCryptPasswordEncoder().encode(rawPassword);
        System.out.println(encoded);
    }}
