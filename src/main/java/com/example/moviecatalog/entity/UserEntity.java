package com.example.moviecatalog.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "usr")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastname;

    private String description;

    @Column(nullable = false)
    private Boolean active = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usr_roles",
            joinColumns = @JoinColumn(name = "usr_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<RoleEntity> roles = new java.util.LinkedHashSet<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "credentials_id", referencedColumnName = "id")

    private CredentialsEntity credentials;

    public void addRole(RoleEntity role) {
        roles.add(role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity user = (UserEntity) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(lastname, user.lastname) && Objects.equals(description, user.description) && Objects.equals(active, user.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastname, description, active, credentials);
    }
}
