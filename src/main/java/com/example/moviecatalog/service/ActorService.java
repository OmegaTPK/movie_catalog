package com.example.moviecatalog.service;

import com.example.moviecatalog.converter.ActorConverter;
import com.example.moviecatalog.dao.ActorDao;
import com.example.moviecatalog.dao.MovieDao;
import com.example.moviecatalog.dto.ActorDto;
import com.example.moviecatalog.entity.ActorEntity;
import com.example.moviecatalog.entity.MovieEntity;
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

    public ActorDto addActor(ActorDto dto) {
        //TODO redo exception
        ActorEntity entityActor = actorConverter.convert(dto);

        entityActor.setId(null);
        actorDao.save(entityActor);

        return actorConverter.convert(entityActor);
    }

    public ActorDto updateActor(ActorDto dto, Long id) {
        //TODO redo exception
        ActorDto result;
        ActorEntity entityActor = actorDao.getById(id);

        actorConverter.fillEntityFromDto(dto, entityActor);
        result = actorConverter.convert(actorDao.save(entityActor));

        return result;
    }

    public ActorDto addMovieInActorsCareer(Long actorId, Long movieId) {
        //TODO redo exception
        ActorDto result;
        MovieEntity movieEntity = movieDao.getById(movieId);
        ActorEntity actorEntity = actorDao.getById(actorId);

        actorEntity.addMovie(movieEntity);
        actorEntity = actorDao.save(actorEntity);

        result = actorConverter.convert(actorEntity);

        return result;
    }

    public Set<ActorDto> getActorsPlayedInMovie(Long movieId) {
        Set<ActorDto> result;
        MovieEntity movie = movieDao.getById(movieId);

        result = movie.
                getActors().
                stream().
                map(actorConverter::convert).
                collect(Collectors.toSet());

        return result;
    }

    private Boolean actorExistById(Long id) {
        return id != null && actorDao.existsById(id);
    }
}
