package com.example.moviecatalog.converter;

import com.example.moviecatalog.dto.CredentialsDto;
import com.example.moviecatalog.entity.CredentialsEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CredentialsConverter {

    private PasswordEncoder passwordEncoder;

    public CredentialsEntity newCredentials(CredentialsDto dto) {

        return new CredentialsEntity(null,
                dto.getLogin(),
                passHash(dto.getPassword()));
    }

    public void fillFromDto(CredentialsDto dto, CredentialsEntity entity) {
        entity.setLogin(dto.getLogin());
        entity.setPassword(passHash(dto.getPassword()));
    }

    private String passHash(String password) {
        return passwordEncoder.encode(password);
    }
}
