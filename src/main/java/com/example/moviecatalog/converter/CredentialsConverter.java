package com.example.moviecatalog.converter;

import com.example.moviecatalog.dto.CredentialsDto;
import com.example.moviecatalog.entity.CredentialsEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CredentialsConverter {


    public CredentialsEntity newCredentials(CredentialsDto dto) {

        return new CredentialsEntity(null,
                dto.getLogin(),
                dto.getPassword());
    }

    public void fillFromDto(CredentialsDto dto, CredentialsEntity entity) {
        entity.setLogin(dto.getLogin());
        entity.setPassword(dto.getPassword());
    }
}
