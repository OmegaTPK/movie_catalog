package com.example.moviecatalog.service;

import com.example.moviecatalog.converter.CredentialsConverter;
import com.example.moviecatalog.dao.CredentialsDao;
import com.example.moviecatalog.dto.CredentialsDto;
import com.example.moviecatalog.entity.CredentialsEntity;
import com.example.moviecatalog.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CredentialsService {

    private CredentialsDao credentialsDao;
    private CredentialsConverter credentialsConverter;

    public CredentialsEntity getCredentials(UserEntity user, CredentialsDto dto) {
        CredentialsEntity result;

        if (user.getCredentials() != null) {
            result = user.getCredentials();
            credentialsConverter.fillFromDto(dto, result);
        } else {
            result = credentialsConverter.newCredentials(dto);
        }

        return credentialsDao.save(result);
    }
}
