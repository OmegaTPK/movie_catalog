package com.example.moviecatalog.service;

import com.example.moviecatalog.converter.MovieConverter;
import com.example.moviecatalog.dao.MovieDao;
import com.example.moviecatalog.dto.MovieDto;
import com.example.moviecatalog.entity.MovieEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieService {
    @Autowired
    private MovieDao movieDao;
    @Autowired
    private MovieConverter movieConverter;

    public List<MovieDto> getMovies() {
        List<MovieEntity> entities = (List<MovieEntity>) movieDao.findAll();
        return movieConverter.convert(entities);
    }
}
