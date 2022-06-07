package com.example.moviecatalog.dao;

import com.example.moviecatalog.entity.CredentialsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialsDao extends JpaRepository<CredentialsEntity, Long> {
    CredentialsEntity findByLogin(String login);
}
