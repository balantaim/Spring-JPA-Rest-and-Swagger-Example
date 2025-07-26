package com.martinatanasov.restapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
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
//				,license = @License(
//					name = "no license",
//					url = "http://"
//				)
        )
)
@Configuration
public class OpenApiUserControllerConfig {

    public static final String SECURITY_SCHEME_BEARER_TOKEN = "bearerAuth";

    @Bean
    public OpenAPI customizeOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_BEARER_TOKEN))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_BEARER_TOKEN, new SecurityScheme()
                                .name(SECURITY_SCHEME_BEARER_TOKEN)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .description(
                                        "Provide the JWT token. JWT token can be obtained from the Authentication API.")
                                .bearerFormat("JWT")));
    }

    @Bean
    public GroupedOpenApi userControllerApi() {
        return GroupedOpenApi.builder()
                .group("employees") // Appears as tab in Swagger UI
                .pathsToMatch(BASE_PATH + "/employees/**") // Match only employees-related endpoints
                .build();
    }

}
