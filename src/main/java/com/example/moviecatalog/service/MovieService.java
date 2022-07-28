package com.example.moviecatalog.service;

import com.example.moviecatalog.converter.MovieConverter;
import com.example.moviecatalog.dao.ActorDao;
import com.example.moviecatalog.dao.MovieDao;
import com.example.moviecatalog.dto.MovieDto;
import com.example.moviecatalog.entity.ActorEntity;
import com.example.moviecatalog.entity.MovieEntity;
import com.example.moviecatalog.exception.NotFoundException;
import com.example.moviecatalog.exception.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MovieService {

    private final MovieDao movieDao;
    private final MovieConverter movieConverter;
    private final ActorDao actorDao;

    public List<MovieDto> getMovies() {
        List<MovieEntity> entities = movieDao.findAll();
        return movieConverter.convertEntities(entities);
    }

    public MovieDto addMovie(@NonNull MovieDto movieDto) {
        MovieEntity movieEntity = movieConverter.convertDtoToEntity(movieDto);

        movieEntity.setId(null);
        movieEntity = movieDao.save(movieEntity);

        return movieConverter.convertEntityToDto(movieEntity);
    }

    public void deleteMovie(@NonNull Long id) {
        checkExistingOfMovie(id);
        movieDao.deleteById(id);
    }

    public MovieDto updateMovie(MovieDto dto, Long id) {
        MovieDto result;
        MovieEntity entity;

        checkExistingOfMovie(id);

        entity = movieDao.getReferenceById(id);

        movieConverter.fillEntityFromDto(dto, entity);
        result = movieConverter.convertEntityToDto(movieDao.save(entity));

        return result;
    }

    public MovieDto addActorToMovie(Long movieId, Long actorId) {
        MovieDto result;
        MovieEntity movieEntity;
        ActorEntity actorEntity;

        checkExistingActorMovieLink(actorId, movieId);

        movieEntity = movieDao.getReferenceById(movieId);
        actorEntity = actorDao.getReferenceById(actorId);

        movieEntity.addActor(actorEntity);
        movieEntity = movieDao.save(movieEntity);

        result = movieConverter.convertEntityToDto(movieEntity);

        return result;
    }

    public Set<MovieDto> getMoviesByActor(Long actorId) {
        Set<MovieDto> result;
        ActorEntity actor;

        checkExistingOfActor(actorId);

        actor = actorDao.getReferenceById(actorId);
        result = actor.
                getMovies().
                stream().
                map(movieConverter::convertEntityToDto).
                collect(Collectors.toSet());

        return result;
    }

    private void checkExistingOfActor(Long actorId) {
        if (!actorDao.existsById(actorId)) {
            throw new NotFoundException("Actor not found!");
        }
    }

    private void checkExistingOfMovie(Long movieId) {
        if (!movieDao.existsById(movieId)) {
            throw new NotFoundException("Movie not found!");
        }
    }

    private void checkExistingActorMovieLink(Long actorId, Long movieId) {
        ActorEntity actorEntity;
        MovieEntity movieEntity;

        checkExistingOfActor(actorId);
        checkExistingOfMovie(movieId);

        actorEntity = actorDao.getReferenceById(actorId);
        movieEntity = movieDao.getReferenceById(movieId);

        if (actorEntity.getMovies().contains(movieEntity)) {
            throw new ValidationException("Actor's already play in this movie");
        }
    }
}
