package dev.pozuelo.url_shortener.Exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Global exception handler for the entire application
@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle URLShortenerException exceptions
    @ExceptionHandler(URLShortenerException.class)
    public ResponseEntity<ErrorResponse> handleURLShortenerException(URLShortenerException ex, WebRequest request) {
        ErrorResponse errorDetails = new ErrorResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, ex.getStatusCode());
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

