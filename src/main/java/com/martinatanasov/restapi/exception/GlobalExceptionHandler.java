package com.martinatanasov.restapi.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.QueryTimeoutException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. @Valid body errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // 2. @Validated query/path param errors
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(cv ->
                errors.put(cv.getPropertyPath().toString(), cv.getMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // 3. Entity not found
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // 4. Resource already exists
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<String> handleResourceAlreadyExists(ResourceAlreadyExistsException ex) {
        log.error("Error: {}", String.valueOf(ex));
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    // 5. Malformed JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleMalformedJson(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>("Malformed JSON request", HttpStatus.BAD_REQUEST);
    }

    // 6. Method not supported
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return new ResponseEntity<>("HTTP method not supported", HttpStatus.METHOD_NOT_ALLOWED);
    }

    // 7. Data integrity (e.g. constraint violations, duplicates)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return new ResponseEntity<>("Database constraint violation: " + Objects.requireNonNull(ex.getRootCause()).getMessage(), HttpStatus.CONFLICT);
    }

    // 8. Database query timeout
    @ExceptionHandler(QueryTimeoutException.class)
    public ResponseEntity<String> handleQueryTimeout(QueryTimeoutException ex) {
        return new ResponseEntity<>("Database query timed out", HttpStatus.REQUEST_TIMEOUT);
    }

    // 9. Hibernate connection timeout
    @ExceptionHandler(JDBCConnectionException.class)
    public ResponseEntity<String> handleJDBCConnectionTimeout(JDBCConnectionException ex) {
        return new ResponseEntity<>("Database connection timeout", HttpStatus.GATEWAY_TIMEOUT);
    }

    // 10. Controller (async) timeout
    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public ResponseEntity<String> handleAsyncTimeout(AsyncRequestTimeoutException ex) {
        return new ResponseEntity<>("Request processing timeout", HttpStatus.REQUEST_TIMEOUT);
    }

    // 11. Mismatch exceptions
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        final String param = ex.getName();
        final String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
        final String message = String.format("Invalid value for '%s': must be of type %s", param, requiredType);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    // 12. Fallback for any other unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        log.error("Error: {}", String.valueOf(ex));
        return new ResponseEntity<>("Unexpected server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
