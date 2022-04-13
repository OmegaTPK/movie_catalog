package com.example.moviecatalog.dto;

import java.io.Serializable;
import java.util.Date;

public class MovieDto implements Serializable {
    private final Long id;
    private final String name;
    private final Date year;
    private final String description;
    private final Double rate;

    public MovieDto(Long id, String name, String description, Double rate, Date year) {
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

    public Date getYear() {
        return year;
    }

    public String getDescription() {
        return description;
    }

    public Double getRate() {
        return rate;
    }
}
