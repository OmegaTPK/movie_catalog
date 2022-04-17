package com.example.moviecatalog.dao;

import com.example.moviecatalog.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieDao extends JpaRepository<MovieEntity, Long> {
}
