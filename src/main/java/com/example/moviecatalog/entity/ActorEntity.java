package com.example.moviecatalog.entity;

import com.example.moviecatalog.enums.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "actor_entity")
@Data
@NoArgsConstructor
public class ActorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false, length = 300)
    private String name;
    @Column(nullable = false, length = 300)
    private String surname;
    @Column(length = 300)
    private String middleName;
    @Enumerated
    @Column(nullable = false)
    private Gender gender;
    @Column(nullable = false)
    private Instant birthDate;
    @Column(nullable = false)
    private Instant activeStartDate;
    @Column(nullable = false, length = 500)
    private String birthPlace;
    @Column(nullable = false, length = 2000)
    private String description;
    @ManyToMany
    @JoinTable(name = "actor_movie_link",
            joinColumns = @JoinColumn(name = "actor_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"))
    private Set<MovieEntity> movies = new HashSet<>();

    public ActorEntity(Long id,
                       String name,
                       String surname,
                       String middleName,
                       Gender gender,
                       Instant birthDate,
                       Instant activeStartDate,
                       String birthPlace,
                       String description,
                       Set<MovieEntity> movies) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.middleName = middleName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.activeStartDate = activeStartDate;
        this.birthPlace = birthPlace;
        this.description = description;
        this.movies = movies;
    }

    public void addMovie(MovieEntity movie) {
        this.movies.add(movie);
    }
}