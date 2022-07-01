package com.example.moviecatalog.service;

import com.example.moviecatalog.config.jwt.JwtProvider;
import com.example.moviecatalog.converter.CredentialsConverter;
import com.example.moviecatalog.converter.TokenConverter;
import com.example.moviecatalog.dao.CredentialsDao;
import com.example.moviecatalog.dao.UserDao;
import com.example.moviecatalog.dto.CredentialsDto;
import com.example.moviecatalog.dto.TokenDto;
import com.example.moviecatalog.entity.CredentialsEntity;
import com.example.moviecatalog.entity.UserEntity;
import com.example.moviecatalog.exception.CredentialsValidationException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CredentialsService {

    private final CredentialsDao credentialsDao;
    private final CredentialsConverter credentialsConverter;
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final TokenConverter tokenConverter;

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

    public TokenDto getTokenFromCredentials(CredentialsDto credentialsDto) {
        String token;

        validateCredentials(credentialsDto);

        token = jwtProvider.generateToken(credentialsDto.getLogin());
        return tokenConverter.convert(token);
    }

    private void validateCredentials(CredentialsDto credentialsDto) {
        UserEntity user;

        user = findUserByCredentials(credentialsDto);
        if (!user.getActive()) {
            throw new CredentialsValidationException("Authentication failed");
        }
    }

    private UserEntity findUserByCredentials(CredentialsDto credentialsDto) {
        CredentialsEntity credentials;
        UserEntity user;

        credentials = credentialsDao.findByLogin(credentialsDto.getLogin());
        if (credentials == null) {
            throw new CredentialsValidationException("Authentication failed");
        }

        user = userDao.findByCredentials(credentials);
        if (user == null
                || !passwordEncoder.matches(credentialsDto.getPassword(), credentials.getPassword())) {
            throw new CredentialsValidationException("Authentication failed");
        }

        return user;
    }
}
