package com.martinatanasov.restapi.controllers;


import com.martinatanasov.restapi.services.EmployeeService;
import com.martinatanasov.restapi.services.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.martinatanasov.restapi.controllers.EmployeeController.BASE_PATH;


@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final TokenService tokenService;
    private final EmployeeService employeeService;

    @PostMapping(BASE_PATH + "/auth/token")
    public ResponseEntity<Map<String, String>> getToken(Authentication authentication) {
        log.debug("Get token for user: {}", authentication.getName());

        return employeeService.getEmployeeByEmail(authentication.getName())
                .map(employee -> {
                    String token = tokenService.generateToken(authentication);
                    log.info("Token granted for user {}: {}", authentication.getName(), token);
                    return ResponseEntity.ok(Map.of("token", token));
                })
                .orElseGet(() -> {
                    log.warn("Token request denied for user {}", authentication.getName());
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                });
    }

}

