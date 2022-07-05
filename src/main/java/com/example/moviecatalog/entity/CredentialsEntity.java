package com.example.moviecatalog.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "credentials_entity")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CredentialsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;
    @Column(unique = true)
    private String login;
    private String password;
}
