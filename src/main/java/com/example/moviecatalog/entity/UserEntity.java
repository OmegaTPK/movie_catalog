package com.example.moviecatalog.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usr")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long Id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastname;

    private String description;

    @Column(nullable = false)
    private Boolean active = false;

    @ManyToMany
    @JoinTable(name = "usr_roles",
            joinColumns = @JoinColumn(name = "usr_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<RoleEntity> roles = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "auth", unique = true)
    private AuthenticationInfoEntity authInfo;

    public void addRole(RoleEntity role) {
        roles.add(role);
    }
}
