package com.example.moviecatalog.exception.handlers;

import com.example.moviecatalog.exception.NotFoundException;
import com.example.moviecatalog.exception.ValidationException;
import com.example.moviecatalog.exception.models.ApiExceptionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ValidationException.class})
    public ResponseEntity<ApiExceptionModel> validateExceptionHandler(ValidationException e) {
        ApiExceptionModel body = new ApiExceptionModel(e.getMessage(),
                Instant.now());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ApiExceptionModel> notFoundExceptionHandler(NotFoundException e) {
        ApiExceptionModel body = new ApiExceptionModel(e.getMessage(),
                Instant.now());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ApiExceptionModel> notFoundExceptionHandler(Exception e) {
        ApiExceptionModel body = new ApiExceptionModel(e.getMessage(),
                Instant.now());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
