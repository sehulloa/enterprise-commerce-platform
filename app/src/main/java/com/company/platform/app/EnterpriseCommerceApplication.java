package com.company.platform.app;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import static org.springframework.boot.SpringApplication.*;

@SpringBootApplication(scanBasePackages = "com.company.platform")
public class EnterpriseCommerceApplication {

    public static void main(String[] args) {
        run(EnterpriseCommerceApplication.class, args);
    }
}
