package com.example.moviecatalog.service;

import com.example.moviecatalog.converter.MovieConverter;
import com.example.moviecatalog.dao.MovieDao;
import com.example.moviecatalog.dto.MovieDto;
import com.example.moviecatalog.entity.MovieEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieService {

    private MovieDao movieDao;
    private MovieConverter movieConverter;

    @Autowired
    public MovieService(MovieDao movieDao, MovieConverter movieConverter) {
        this.movieDao = movieDao;
        this.movieConverter = movieConverter;
    }

    public List<MovieDto> getMovies() {
        List<MovieEntity> entities = (List<MovieEntity>) movieDao.findAll();
        return movieConverter.convertEntities(entities);
    }

    public MovieDto addMovie(@NonNull MovieDto movieDto) {
        MovieEntity movieEntity = movieConverter.convert(movieDto);
        movieEntity.setId(null);
        movieEntity = movieDao.save(movieEntity);
        return movieConverter.convert(movieEntity);
    }

    public void deleteMovie(@NonNull Long id) {
        //TODO redo with exception usage throw if can't delete
        if (movieExistById(id)) {
            movieDao.deleteById(id);
        }
    }

    public MovieDto updateMovie(MovieDto dto) {
        //TODO redo with exception usage throw if movie not exist
        MovieEntity entity = movieConverter.convert(dto);
        MovieDto result = null;
        if (movieExistById(entity.getId())) {
            result = movieConverter.convert(movieDao.save(entity));
        }
        return result;
    }

    private Boolean movieExistById(Long movieId) {
        return movieId != null && movieDao.existsById(movieId);
    }
}
