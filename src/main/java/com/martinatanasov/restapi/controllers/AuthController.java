package com.martinatanasov.restapi.controllers;


import com.martinatanasov.restapi.services.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final TokenService tokenService;

    @PostMapping("/token")
    public Map<String, String> getToken(Authentication authentication) {
        log.debug("Get token for user: {}", authentication.getName());
        final String token = tokenService.generateToken(authentication);
        log.info("Token granted: {}", token);
        return Map.of("token", token);
    }

}

