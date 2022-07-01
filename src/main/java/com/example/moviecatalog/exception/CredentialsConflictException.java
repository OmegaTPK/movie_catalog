package com.example.moviecatalog.exception;

public class CredentialsConflictException extends ConflictException {
    public CredentialsConflictException(String message) {
        super(message);
    }
}
