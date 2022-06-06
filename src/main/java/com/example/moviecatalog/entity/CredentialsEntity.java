package com.example.moviecatalog.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "credentials_entity")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CredentialsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;
    @Column(unique = true)
    private String login;
    private String password;
}
