package com.example.moviecatalog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class MovieDto implements Serializable {
    private Long id;
    private String name;
    private Instant year;
    private String description;
    private Double rate;
}
