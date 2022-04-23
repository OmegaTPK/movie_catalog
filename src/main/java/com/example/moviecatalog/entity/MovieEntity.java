package com.example.moviecatalog.entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
public class MovieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Instant year;
    @Column(nullable = false, length = 2000)
    private String description;
    @Column(nullable = false)
    private Double rate;

    public MovieEntity() {

    }

    public MovieEntity(Long id, String name, Instant year, String description, Double rate) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.description = description;
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public Instant getYear() {
        return year;
    }

    public Double getRate() {
        return rate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(Instant year) {
        this.year = year;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

}
