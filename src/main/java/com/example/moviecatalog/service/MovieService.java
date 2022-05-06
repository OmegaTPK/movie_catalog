package com.example.moviecatalog.service;

import com.example.moviecatalog.converter.MovieConverter;
import com.example.moviecatalog.dao.ActorDao;
import com.example.moviecatalog.dao.MovieDao;
import com.example.moviecatalog.dto.MovieDto;
import com.example.moviecatalog.entity.ActorEntity;
import com.example.moviecatalog.entity.MovieEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieService {

    private MovieDao movieDao;
    private MovieConverter movieConverter;
    private ActorDao actorDao;

    @Autowired
    public MovieService(MovieDao movieDao, MovieConverter movieConverter, ActorDao actorDao) {
        this.movieDao = movieDao;
        this.movieConverter = movieConverter;
        this.actorDao = actorDao;
    }

    public List<MovieDto> getMovies() {
        List<MovieEntity> entities = movieDao.findAll();
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

    public MovieDto updateMovie(MovieDto dto, Long id) {
        //TODO redo with exception usage throw if movie not exist
        MovieDto result;
        MovieEntity entity = movieDao.getById(id);

        movieConverter.fillEntityFromDto(dto, entity);
        result = movieConverter.convert(movieDao.save(entity));

        return result;
    }

    public MovieDto addActorToMovie(Long movieId, Long actor_id) {
        //TODO redo exception
        MovieDto result;
        MovieEntity movieEntity = movieDao.getById(movieId);
        ActorEntity actorEntity = actorDao.getById(actor_id);

        movieEntity.addActor(actorEntity);
        movieEntity = movieDao.save(movieEntity);

        result = movieConverter.convert(movieEntity);

        return result;
    }

    private Boolean movieExistById(Long movieId) {
        return movieId != null && movieDao.existsById(movieId);
    }
}
