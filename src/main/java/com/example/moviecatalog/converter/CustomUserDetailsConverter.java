package com.example.moviecatalog.converter;

import com.example.moviecatalog.config.CustomUserDetails;
import com.example.moviecatalog.entity.CredentialsEntity;
import com.example.moviecatalog.entity.UserEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CustomUserDetailsConverter {
    public CustomUserDetails fromUserEntityToCustomUserDetails(UserEntity userEntity) {
        CredentialsEntity credentials = userEntity.getCredentials();
        CustomUserDetails c = CustomUserDetails.builder()
                .login(credentials.getLogin())
                .password(credentials.getPassword())
                .grantedAuthorities(userEntity
                        .getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toSet()))
                .build();
        return c;
    }
}
