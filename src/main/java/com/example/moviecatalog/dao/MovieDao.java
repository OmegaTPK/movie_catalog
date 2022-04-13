package com.example.moviecatalog.dao;

import com.example.moviecatalog.entity.MovieEntity;
import org.springframework.data.repository.CrudRepository;

public interface MovieDao  extends CrudRepository<MovieEntity, Long> {
}
