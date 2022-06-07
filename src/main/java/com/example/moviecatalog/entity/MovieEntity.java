package com.example.moviecatalog.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

    @ManyToMany
    @JoinTable(name = "actor_movie_link",
            joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id", referencedColumnName = "id"))
    private Set<ActorEntity> actors = new HashSet<>();

    public void addActor(ActorEntity actorEntity) {
        this.actors.add(actorEntity);
    }
}
