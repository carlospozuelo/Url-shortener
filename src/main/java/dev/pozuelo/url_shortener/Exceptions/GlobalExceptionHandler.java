package dev.pozuelo.url_shortener.Exceptions;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Global exception handler for the entire application
@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle URLShortenerException exceptions
    @ExceptionHandler(URLShortenerException.class)
    public ResponseEntity<ErrorResponse> handleURLShortenerException(URLShortenerException ex, WebRequest request) {
        ErrorResponse errorDetails = new ErrorResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, ex.getStatusCode());
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(HandlerMethodValidationException ex) {
        StringBuilder errors = new StringBuilder();
        for (MessageSourceResolvable error : ex.getAllErrors()) {
            errors.append(error.getDefaultMessage()).append("\n");
        }

        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), "Validation errors in the request.", errors.toString());

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        ErrorResponse errorDetails = new ErrorResponse(LocalDateTime.now(), "Request body is missing or malformed", request.getDescription(false));
        return ResponseEntity.badRequest().body(errorDetails);
    }


    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoResourceFoundException ex, WebRequest request) {
        ErrorResponse errorDetails = new ErrorResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }

    // Handle generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        ErrorResponse errorDetails = new ErrorResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class ErrorResponse {
        private LocalDateTime timestamp;
        private String message;
        private String details;
    }
}

