package com.company.platform.app.config.swagger;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(
        name = "app.security.swagger-enabled",
        havingValue = "true",
        matchIfMissing = false
)
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI enterpriseCommerceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Enterprise Commerce Platform API")
                        .description("API documentation for the Enterprise Commerce Platform modular monolith.")
                        .version("v1")
                        .contact(new Contact()
                                .name("Enterprise Commerce Platform")
                                .email("no-reply@local.dev"))
                        .license(new License()
                                .name("Internal Use")))
                .externalDocs(new ExternalDocumentation()
                        .description("Project documentation")
                        .url("/swagger-ui.html"));
    }

}
