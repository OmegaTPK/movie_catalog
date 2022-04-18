package com.example.moviecatalog.service;

import com.example.moviecatalog.converter.MovieConverter;
import com.example.moviecatalog.dao.MovieDao;
import com.example.moviecatalog.dto.MovieDto;
import com.example.moviecatalog.entity.MovieEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<List<MovieDto>> getMovies() {
        List<MovieEntity> entities = (List<MovieEntity>) movieDao.findAll();
        return new ResponseEntity<List<MovieDto>>(movieConverter.convertEntities(entities), HttpStatus.OK);
    }

    public ResponseEntity<MovieDto> addMovie(@NonNull MovieDto movieDto) {

        ResponseEntity<MovieDto> response;
        MovieEntity movieEntity = movieConverter.convert(movieDto);
        Boolean movieExist = movieExistById(movieEntity.getId());

        if (movieExist) {
            response = new ResponseEntity<MovieDto>(HttpStatus.CONFLICT);
        } else {
            movieEntity = saveMovie(movieEntity);

            if (movieEntity == null) {
                response = new ResponseEntity<MovieDto>(HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                response = new ResponseEntity<MovieDto>(movieConverter.convert(movieEntity), HttpStatus.OK);
            }
        }
        return response;
    }

    public ResponseEntity deleteMovie(@NonNull Long id) {

        ResponseEntity response;

        if (movieExistById(id)) {
            movieDao.deleteById(id);
            response = new ResponseEntity(HttpStatus.OK);
        } else {
            response = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    private MovieEntity saveMovie(MovieEntity movieEntity) {
        return movieDao.save(movieEntity);
    }

    private Boolean movieExistById(Long movieId) {
        return movieId != null && movieDao.existsById(movieId);
    }
}
