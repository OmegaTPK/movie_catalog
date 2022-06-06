package com.example.moviecatalog.dao;

import com.example.moviecatalog.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<UserEntity, Long> {
}
