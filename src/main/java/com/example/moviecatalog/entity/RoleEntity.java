package com.example.moviecatalog.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "role_entity")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany
    @JoinTable(name = "usr_roles",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "usr_id", referencedColumnName = "id"))
    private Set<UserEntity> users = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleEntity that = (RoleEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(users, that.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
