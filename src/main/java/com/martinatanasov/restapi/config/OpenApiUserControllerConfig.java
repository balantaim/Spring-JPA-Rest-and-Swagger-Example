package com.martinatanasov.restapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.martinatanasov.restapi.controllers.EmployeeController.BASE_PATH;

@OpenAPIDefinition(
        info = @Info(
                title = "Simple Spring JPA Rest and Swagger Example",
                version = "1.0.0",
                description = "Spring JPA rest project with MySQL database and swagger-ui for testing purpose",
                //termsOfService = "http://",
                contact = @Contact(
                        name = "Martin Atanasov"
                        //email = .....
                )
				,license = @License(
					name = "Apache 2.0",
					url = "https://www.apache.org/licenses/LICENSE-2.0"
				)
        )
)
@Configuration
public class OpenApiUserControllerConfig {

    public static final String SECURITY_SCHEME_BEARER_TOKEN = "bearerAuth";
    public static final String SECURITY_SCHEME_BASIC_AUTH = "basicAuth";

    @Bean
    public OpenAPI customizeOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(SECURITY_SCHEME_BEARER_TOKEN)
                )
                .components(
                        new Components()
                                // Add basic auth Authorization
                                .addSecuritySchemes(
                                        SECURITY_SCHEME_BASIC_AUTH,
                                        new SecurityScheme()
                                                .name(SECURITY_SCHEME_BASIC_AUTH)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("basic")
                                                .description("Username and password authentication.")
                                )
                                // Add bearer Authorization
                                .addSecuritySchemes(
                                        SECURITY_SCHEME_BEARER_TOKEN,
                                        new SecurityScheme()
                                                .name(SECURITY_SCHEME_BEARER_TOKEN)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                                .description("Provide a JWT token.")
                                )
                );
    }

    // Create a single group for employees + auth (we can create beans for different groups)
    @Bean
    public GroupedOpenApi userControllerApi() {
        return GroupedOpenApi.builder()
                .group("employees")
                .pathsToMatch(
                        "/auth/**",
                        BASE_PATH + "/employees/**"
                )
                .build();
    }

}
