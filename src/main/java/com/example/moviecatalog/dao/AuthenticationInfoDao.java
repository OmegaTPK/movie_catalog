package com.example.moviecatalog.dao;

import com.example.moviecatalog.entity.AuthenticationInfoEntity;
import com.example.moviecatalog.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationInfoDao extends JpaRepository<AuthenticationInfoEntity, Long> {
    AuthenticationInfoEntity findByLogin(String login);

    AuthenticationInfoEntity findByUser(UserEntity user);
}