package com.example.moviecatalog.dto;

import java.io.Serializable;
import java.time.Instant;

public class MovieDto implements Serializable {
    private final Long id;
    private final String name;
    private final Instant year;
    private final String description;
    private final Double rate;

    public MovieDto(Long id, String name, String description, Double rate, Instant year) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.description = description;
        this.rate = rate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Instant getYear() {
        return year;
    }

    public String getDescription() {
        return description;
    }

    public Double getRate() {
        return rate;
    }
}
