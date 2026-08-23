package com.martinatanasov.restapi.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.jspecify.annotations.NullMarked;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@NullMarked
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. @Valid body errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((first, second) -> first + "; " + second)
                .orElse("Validation failed");

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                request);
    }

    // 2. @Validated query/path parameter errors
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            WebRequest request) {

        String message = ex.getConstraintViolations()
                .stream()
                .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                .reduce((first, second) -> first + "; " + second)
                .orElse("Validation failed");

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                request);
    }

    // 3. Entity not found
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(
            EntityNotFoundException ex,
            WebRequest request) {

        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request);
    }

    // 4. Resource already exists
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExists(
            ResourceAlreadyExistsException ex,
            WebRequest request) {

        return buildErrorResponse(
                HttpStatus.CONFLICT,
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request);
    }

    // 5. Malformed JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMalformedJson(
            HttpMessageNotReadableException ex,
            WebRequest request) {

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Malformed JSON request",
                request);
    }

    // 6. Method not supported
    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(
            org.springframework.web.HttpRequestMethodNotSupportedException ex,
            WebRequest request) {

        return buildErrorResponse(
                HttpStatus.METHOD_NOT_ALLOWED,
                HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(),
                "HTTP method not supported",
                request);
    }

    // 7. Data integrity violation
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            WebRequest request) {

        return buildErrorResponse(
                HttpStatus.CONFLICT,
                HttpStatus.CONFLICT.getReasonPhrase(),
                "Database constraint violation",
                request);
    }

    // 8. Database query timeout
    @ExceptionHandler(org.springframework.dao.QueryTimeoutException.class)
    public ResponseEntity<ErrorResponse> handleQueryTimeout(
            org.springframework.dao.QueryTimeoutException ex,
            WebRequest request) {

        return buildErrorResponse(
                HttpStatus.REQUEST_TIMEOUT,
                HttpStatus.REQUEST_TIMEOUT.getReasonPhrase(),
                "Database query timed out",
                request);
    }

    // 9. Hibernate/JDBC connection error
    @ExceptionHandler(org.hibernate.exception.JDBCConnectionException.class)
    public ResponseEntity<ErrorResponse> handleJDBCConnectionException(
            org.hibernate.exception.JDBCConnectionException ex,
            WebRequest request) {

        return buildErrorResponse(
                HttpStatus.GATEWAY_TIMEOUT,
                HttpStatus.GATEWAY_TIMEOUT.getReasonPhrase(),
                "Database connection error",
                request);
    }

    // 10. Controller async timeout
    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public ResponseEntity<ErrorResponse> handleAsyncTimeout(
            AsyncRequestTimeoutException ex,
            WebRequest request) {

        return buildErrorResponse(
                HttpStatus.REQUEST_TIMEOUT,
                HttpStatus.REQUEST_TIMEOUT.getReasonPhrase(),
                "Request processing timeout",
                request);
    }

    // 11. Request parameter type mismatch
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            WebRequest request) {

        final String param = ex.getName();

        final String requiredType = ex.getRequiredType() != null
                ? ex.getRequiredType().getSimpleName()
                : "unknown";

        final String message = String.format(
                "Invalid value for '%s': must be of type %s",
                param,
                requiredType);

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                request);
    }

    // 12. Fallback for unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex,
            WebRequest request) {

        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Unexpected server error",
                request);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpStatus status,
            String error,
            String message,
            WebRequest request) {

        String path = request.getDescription(false)
                .replace("uri=", "");

        ErrorResponse response = new ErrorResponse(
                status.value(),
                error,
                message,
                path,
                LocalDateTime.now()
        );

        return ResponseEntity.status(status)
                .body(response);
    }

}
