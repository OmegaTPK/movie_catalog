package com.example.moviecatalog.service;

import com.example.moviecatalog.converter.MovieConverter;
import com.example.moviecatalog.dao.ActorDao;
import com.example.moviecatalog.dao.MovieDao;
import com.example.moviecatalog.dto.MovieDto;
import com.example.moviecatalog.entity.ActorEntity;
import com.example.moviecatalog.entity.MovieEntity;
import com.example.moviecatalog.exception_handling.checkup.ActorMovieCheckup;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class MovieService {

    private MovieDao movieDao;
    private MovieConverter movieConverter;
    private ActorDao actorDao;
    private ActorMovieCheckup checkup;

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
        checkup.checkExistingOfMovie(id);
        movieDao.deleteById(id);
    }

    public MovieDto updateMovie(MovieDto dto, Long id) {
        MovieDto result;
        MovieEntity entity;

        checkup.checkExistingOfMovie(id);

        entity = movieDao.getById(id);

        movieConverter.fillEntityFromDto(dto, entity);
        result = movieConverter.convert(movieDao.save(entity));

        return result;
    }

    public MovieDto addActorToMovie(Long movieId, Long actorId) {
        MovieDto result;
        MovieEntity movieEntity;
        ActorEntity actorEntity;

        checkup.checkExistingActorMovieLink(actorId, movieId);

        movieEntity = movieDao.getById(movieId);
        actorEntity = actorDao.getById(actorId);

        movieEntity.addActor(actorEntity);
        movieEntity = movieDao.save(movieEntity);

        result = movieConverter.convert(movieEntity);

        return result;
    }

    public Set<MovieDto> getMoviesByActor(Long actorId) {
        Set<MovieDto> result;
        ActorEntity actor;

        checkup.checkExistingOfActor(actorId);

        actor = actorDao.getById(actorId);
        result = actor.
                getMovies().
                stream().
                map(movieConverter::convert).
                collect(Collectors.toSet());

        return result;
    }
}
