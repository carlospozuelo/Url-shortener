package dev.pozuelo.url_shortener.Exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

/**
 * This is the base exception to be thrown in the application.
 */
@Data
public class URLShortenerException extends RuntimeException {

    private HttpStatusCode statusCode;

    public URLShortenerException(String message, HttpStatusCode statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public URLShortenerException(String message) {
        super(message);
        statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
