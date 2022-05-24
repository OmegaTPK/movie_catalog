package com.example.moviecatalog.exception.models;

import java.time.Instant;

public record ApiExceptionModel(String message, Instant timeStamp) {

}
