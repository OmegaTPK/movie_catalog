package com.example.moviecatalog.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class MovieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "year", nullable = false)
    private Date year;
    @Column(name = "description", nullable = false, length = 2000)
    private String description;
    @Column(name = "rate", nullable = false)
    private Double rate;

    public MovieEntity() {

    }

    public MovieEntity(Long id, String name, Date year, String description, Double rate) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.description = description;
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public Date getYear() {
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
}
