package com.example.moviecatalog.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
public class MovieDto implements Serializable {
    private Long id;
    private String name;
    private Instant year;
    private String description;
    private Double rate;

    public MovieDto(Long id, String name, Instant year, String description, Double rate) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.description = description;
        this.rate = rate;
    }
}
