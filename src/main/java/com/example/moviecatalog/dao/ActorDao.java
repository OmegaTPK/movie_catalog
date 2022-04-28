package com.example.moviecatalog.dao;

import com.example.moviecatalog.entity.ActorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorDao extends JpaRepository<ActorEntity, Long> {
}