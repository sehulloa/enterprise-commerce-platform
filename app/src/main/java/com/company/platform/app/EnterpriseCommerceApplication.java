package com.company.platform.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.company.platform")
@EntityScan(basePackages = "com.company.platform")
@EnableJpaRepositories(basePackages = "com.company.platform")
public class EnterpriseCommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnterpriseCommerceApplication.class, args);    }
}
