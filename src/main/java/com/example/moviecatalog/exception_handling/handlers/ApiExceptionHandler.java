package com.example.moviecatalog.exception_handling.handlers;

import com.example.moviecatalog.exception_handling.exceptions.NotFoundException;
import com.example.moviecatalog.exception_handling.exceptions.ValidationException;
import com.example.moviecatalog.exception_handling.models.ApiExceptionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ApiExceptionHandler {

    private final HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;
    private final HttpStatus NOT_FOUND = HttpStatus.NOT_FOUND;
    private final HttpStatus INTERNAL_ERROR = HttpStatus.INTERNAL_SERVER_ERROR;

    @ExceptionHandler(value = {ValidationException.class})
    public ResponseEntity<ApiExceptionModel> validateExceptionHandler(ValidationException e) {
        ApiExceptionModel body = new ApiExceptionModel(e.getMessage(),
                Instant.now(),
                BAD_REQUEST);
        return new ResponseEntity<>(body, BAD_REQUEST);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ApiExceptionModel> notFoundExceptionHandler(NotFoundException e) {
        ApiExceptionModel body = new ApiExceptionModel(e.getMessage(),
                Instant.now(),
                NOT_FOUND);
        return new ResponseEntity<>(body, NOT_FOUND);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ApiExceptionModel> notFoundExceptionHandler(Exception e) {
        ApiExceptionModel body = new ApiExceptionModel(e.getMessage(),
                Instant.now(),
                INTERNAL_ERROR);
        return new ResponseEntity<>(body, INTERNAL_ERROR);
    }
}
