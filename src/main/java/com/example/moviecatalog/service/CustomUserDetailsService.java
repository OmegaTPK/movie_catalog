package com.example.moviecatalog.service;

import com.example.moviecatalog.config.CustomUserDetails;
import com.example.moviecatalog.dao.CredentialsDao;
import com.example.moviecatalog.dao.UserDao;
import com.example.moviecatalog.entity.CredentialsEntity;
import com.example.moviecatalog.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private UserDao userDao;
    private CredentialsDao credentialsDao;

    @Override
    public CustomUserDetails loadUserByUsername(String userName) {
        UserEntity userEntity;
        CredentialsEntity credentials;

        credentials = credentialsDao.findByLogin(userName);
        userEntity = userDao.findByCredentials(credentials);

        return CustomUserDetails.fromUserEntityToCustomUserDetails(userEntity);
    }
}
