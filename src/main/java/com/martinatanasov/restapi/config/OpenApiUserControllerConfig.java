package com.martinatanasov.restapi.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.martinatanasov.restapi.controllers.CrudController.BASE_PATH;

@Configuration
public class OpenApiUserControllerConfig {

    @Bean
    public GroupedOpenApi userControllerApi() {
        return GroupedOpenApi.builder()
                .group("employees") // Appears as tab in Swagger UI
                .pathsToMatch(BASE_PATH + "/**") // Match only employees-related endpoints
                .build();
    }

}
