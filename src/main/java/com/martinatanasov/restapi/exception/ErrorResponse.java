package com.martinatanasov.restapi.exception;

import org.jspecify.annotations.NullMarked;

import java.time.LocalDateTime;

@NullMarked
public record ErrorResponse(
        int status,
        String error,
        String message,
        String path,
        LocalDateTime timestamp
)  {

}