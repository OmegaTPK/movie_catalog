package com.example.moviecatalog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ConflictException extends RuntimeException {
    private final String message;
}
