package com.company.platform.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.company.platform")
public class EnterpriseCommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnterpriseCommerceApplication.class, args);    }
}
