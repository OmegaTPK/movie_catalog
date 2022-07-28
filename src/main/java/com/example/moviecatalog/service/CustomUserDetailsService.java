package com.example.moviecatalog.service;

import com.example.moviecatalog.config.CustomUserDetails;
import com.example.moviecatalog.converter.CustomUserDetailsConverter;
import com.example.moviecatalog.dao.CredentialsDao;
import com.example.moviecatalog.dao.UserDao;
import com.example.moviecatalog.entity.CredentialsEntity;
import com.example.moviecatalog.entity.UserEntity;
import com.example.moviecatalog.exception.CredentialsValidationException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private UserDao userDao;
    private CredentialsDao credentialsDao;
    private CustomUserDetailsConverter customUserDetailsConverter;

    @Override
    public CustomUserDetails loadUserByUsername(String userName) {
        UserEntity userEntity;
        CredentialsEntity credentials;

        credentials = credentialsDao.findByLogin(userName);
        if (credentials == null) {
            throw new CredentialsValidationException("Authentication failed");
        }

        userEntity = userDao.findByCredentials(credentials);
        if (userEntity == null || !userEntity.getActive()) {
            throw new CredentialsValidationException("Authentication failed");
        }

        return customUserDetailsConverter.fromUserEntityToCustomUserDetails(userEntity);
    }
}
