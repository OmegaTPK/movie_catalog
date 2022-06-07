package com.example.moviecatalog.converter;

import com.example.moviecatalog.dto.CredentialsDto;
import com.example.moviecatalog.entity.CredentialsEntity;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class CredentialsConverter {

    public CredentialsEntity newCredentials(CredentialsDto dto) {

        return new CredentialsEntity(null,
                dto.login(),
                passHash(dto.password()));
    }

    public void fillFromDto(CredentialsDto dto, CredentialsEntity entity) {
        entity.setLogin(passHash(dto.login()));
        entity.setPassword(dto.password());
    }

    @SneakyThrows
    private String passHash(String password) {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return new String(hash);
    }
}
