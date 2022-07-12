package com.example.moviecatalog.entity;

import com.example.moviecatalog.enums.Gender;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "actor_entity")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
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

    public void addMovie(MovieEntity movie) {
        this.movies.add(movie);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActorEntity that = (ActorEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(surname, that.surname) && Objects.equals(middleName, that.middleName) && gender == that.gender && Objects.equals(birthDate, that.birthDate) && Objects.equals(activeStartDate, that.activeStartDate) && Objects.equals(birthPlace, that.birthPlace) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, middleName, gender, birthDate, activeStartDate, birthPlace, description);
    }


}
