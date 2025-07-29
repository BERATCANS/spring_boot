package com.beratcan.first_steps_on_kron.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ErrorDetails handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ErrorDetails.builder()
                .message(ex.getMessage())
                .details("The requested resource was not found.")
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .status(404)
                .error("Not Found")
                .build();
    }
    @ExceptionHandler(Exception.class)
    public ErrorDetails handleGlobalException(Exception ex) {
        return ErrorDetails.builder()
                .message(ex.getMessage())
                .details("An unexpected error occurred.")
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .status(500)
                .error("Internal Server Error")
                .build();
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDetails> handleJsonParseError(HttpMessageNotReadableException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .message(ex.getMessage())
                .details("Please ensure that the JSON body is correctly formatted and contains valid integer values.")
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .status(400)
                .error("Bad Request")
                .build();

        return ResponseEntity.badRequest().body(errorDetails);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetails> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .message(ex.getMessage())
                .details("Validation error")
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .status(HttpStatus.BAD_REQUEST.value())  // 400 Bad Request
                .error("Bad Request")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorDetails> handleUnauthorized(UnauthorizedException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .message(ex.getMessage())
                .details("User authentication failed.")
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Unauthorized")
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
    }


}
