package com.martinatanasov.restapi.services;

import org.springframework.security.core.Authentication;

public interface TokenService {

    String generateToken(Authentication authentication);

}
