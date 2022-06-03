package com.example.moviecatalog.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Entity
@Table(name = "auth_info_entity")
@NoArgsConstructor
@Getter
@Setter
public class AuthenticationInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;
    @Column(unique = true)
    private String login;
    private String password;

    @OneToOne(targetEntity = UserEntity.class)
    @JoinColumn(unique = true)
    private UserEntity user;

    public AuthenticationInfoEntity(Long id, String login, String password, UserEntity user) {
        this.id = id;
        this.login = login;
        this.password = passHash(password);
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = passHash(password);
    }

    @SneakyThrows
    private String passHash(String password) {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return new String(hash);
    }
}