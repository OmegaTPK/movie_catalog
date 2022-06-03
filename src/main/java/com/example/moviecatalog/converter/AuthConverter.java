package com.example.moviecatalog.converter;

import com.example.moviecatalog.dto.LoginPassDto;
import com.example.moviecatalog.entity.AuthenticationInfoEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthConverter {

    public AuthenticationInfoEntity convert(LoginPassDto dto) {
        return new AuthenticationInfoEntity(null,
                dto.login(),
                dto.password(),
                null);
    }

    public void fillFromDto(LoginPassDto dto, AuthenticationInfoEntity entity) {
        entity.setLogin(dto.login());
        entity.setPassword(dto.password());
    }
}
