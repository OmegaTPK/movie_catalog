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
public class MovieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    @Getter
    @Setter
    private Long id;
    @Column(nullable = false)
    @Getter
    @Setter
    private String name;
    @Column(nullable = false)
    @Getter
    @Setter
    private Instant year;
    @Column(nullable = false, length = 2000)
    @Getter
    @Setter
    private String description;
    @Column(nullable = false)
    @Getter
    @Setter
    private Double rate;
    @ManyToMany
    @JoinTable(name = "actor_movie_link",
            joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id", referencedColumnName = "id"))
    @Getter
    @Setter
    private Set<ActorEntity> actors = new HashSet<>();

    public void addActor(ActorEntity actorEntity) {
        this.actors.add(actorEntity);
    }
}
