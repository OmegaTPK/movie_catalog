package com.example.moviecatalog.service;

import com.example.moviecatalog.converter.ActorConverter;
import com.example.moviecatalog.dao.ActorDao;
import com.example.moviecatalog.dao.MovieDao;
import com.example.moviecatalog.dto.ActorDto;
import com.example.moviecatalog.entity.ActorEntity;
import com.example.moviecatalog.entity.MovieEntity;
import com.example.moviecatalog.exception.NotFoundException;
import com.example.moviecatalog.exception.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ActorService {

    private ActorConverter actorConverter;
    private ActorDao actorDao;
    private MovieDao movieDao;

    public ActorDto addActor(ActorDto dto) {
        ActorEntity entityActor = actorConverter.convertDtoToEntity(dto);

        entityActor.setId(null);
        entityActor = actorDao.save(entityActor);

        return actorConverter.convertEntityToDto(entityActor);
    }

    public ActorDto updateActor(ActorDto dto, Long id) {
        ActorDto result;
        ActorEntity entityActor;

        checkExistingOfActor(id);

        entityActor = actorDao.getReferenceById(id);
        actorConverter.fillEntityFromDto(dto, entityActor);

        result = actorConverter.convertEntityToDto(actorDao.save(entityActor));
        return result;
    }

    public ActorDto addMovieInActorsCareer(Long actorId, Long movieId) {
        ActorDto result;
        MovieEntity movieEntity;
        ActorEntity actorEntity;

        checkExistingActorMovieLink(actorId, movieId);

        movieEntity = movieDao.getReferenceById(movieId);
        actorEntity = actorDao.getReferenceById(actorId);

        actorEntity.addMovie(movieEntity);
        actorEntity = actorDao.save(actorEntity);

        result = actorConverter.convertEntityToDto(actorEntity);
        return result;
    }

    public Set<ActorDto> getActorsPlayedInMovie(Long movieId) {
        Set<ActorDto> result;
        checkExistingOfMovie(movieId);

        MovieEntity movie = movieDao.getReferenceById(movieId);

        result = movie.
                getActors().
                stream().
                map(actorConverter::convertEntityToDto).
                collect(Collectors.toSet());

        return result;
    }

    public Set<ActorDto> getActors() {
        return actorDao
                .findAll()
                .stream()
                .map(actorConverter::convertEntityToDto)
                .collect(Collectors.toSet());
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
