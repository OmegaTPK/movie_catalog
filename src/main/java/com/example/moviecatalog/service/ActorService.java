package com.example.moviecatalog.service;

import com.example.moviecatalog.converter.ActorConverter;
import com.example.moviecatalog.dao.ActorDao;
import com.example.moviecatalog.dao.MovieDao;
import com.example.moviecatalog.dto.ActorDto;
import com.example.moviecatalog.entity.ActorEntity;
import com.example.moviecatalog.entity.MovieEntity;
import com.example.moviecatalog.exception_handling.checkup.ActorMovieCheckup;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ActorService {

    private ActorConverter actorConverter;
    private ActorDao actorDao;
    private MovieDao movieDao;
    private ActorMovieCheckup checkup;

    public ActorDto addActor(ActorDto dto) {
        ActorEntity entityActor = actorConverter.convert(dto);

        entityActor.setId(null);
        actorDao.save(entityActor);

        return actorConverter.convert(entityActor);
    }

    public ActorDto updateActor(ActorDto dto, Long id) {
        ActorDto result;
        ActorEntity entityActor;

        checkup.checkExistingOfActor(id);

        entityActor = actorDao.getById(id);
        actorConverter.fillEntityFromDto(dto, entityActor);

        result = actorConverter.convert(actorDao.save(entityActor));
        return result;
    }

    public ActorDto addMovieInActorsCareer(Long actorId, Long movieId) {
        ActorDto result;
        MovieEntity movieEntity;
        ActorEntity actorEntity;

        checkup.checkExistingActorMovieLink(actorId, movieId);

        movieEntity = movieDao.getById(movieId);
        actorEntity = actorDao.getById(actorId);

        actorEntity.addMovie(movieEntity);
        actorEntity = actorDao.save(actorEntity);

        result = actorConverter.convert(actorEntity);
        return result;
    }

    public Set<ActorDto> getActorsPlayedInMovie(Long movieId) {
        Set<ActorDto> result;
        MovieEntity movie = movieDao.getById(movieId);

        checkup.checkExistingOfMovie(movieId);

        result = movie.
                getActors().
                stream().
                map(actorConverter::convert).
                collect(Collectors.toSet());

        return result;
    }
}
