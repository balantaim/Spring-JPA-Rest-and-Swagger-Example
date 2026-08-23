package com.martinatanasov.restapi.controllers;

import com.martinatanasov.restapi.config.OpenApiUserControllerConfig;
import com.martinatanasov.restapi.model.TokenResponseDTO;
import com.martinatanasov.restapi.services.EmployeeService;
import com.martinatanasov.restapi.services.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "Auth")
@RestController
public class AuthController {

    private final TokenService tokenService;
    private final EmployeeService employeeService;

    //Required in order to log in with basic auth via Swagger ui
    @SecurityRequirement(name = OpenApiUserControllerConfig.SECURITY_SCHEME_BASIC_AUTH)
    @Operation(summary = "Get authentification", description = "Retrieve valid JWT token")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Obtain a valid token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Not authorized")
    })
    @PostMapping("/auth/token")
    public ResponseEntity<TokenResponseDTO> getToken(Authentication authentication) {
        log.debug("Get token for user: {}", authentication.getName());

        return employeeService
                .getEmployeeByEmail(authentication.getName())
                .map(employee -> {
                    String token = tokenService.generateToken(authentication);
                    log.info("Token granted for user: {}", authentication.getName());
                    return ResponseEntity.ok(new TokenResponseDTO(token));
                })
                .orElseGet(() -> {
                    log.warn("Token request denied for user: {}", authentication.getName());

                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .build();
                });
    }

}

