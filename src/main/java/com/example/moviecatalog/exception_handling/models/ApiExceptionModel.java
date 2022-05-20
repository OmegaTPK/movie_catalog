package com.example.moviecatalog.exception_handling.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ApiExceptionModel {

    private final String message;
    private final Instant timeStamp;
    private final HttpStatus status;

}
