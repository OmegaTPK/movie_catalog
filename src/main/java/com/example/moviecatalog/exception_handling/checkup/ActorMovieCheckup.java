package com.example.moviecatalog.exception_handling.checkup;

import com.example.moviecatalog.dao.ActorDao;
import com.example.moviecatalog.dao.MovieDao;
import com.example.moviecatalog.entity.ActorEntity;
import com.example.moviecatalog.entity.MovieEntity;
import com.example.moviecatalog.exception_handling.exceptions.NotFoundException;
import com.example.moviecatalog.exception_handling.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ActorMovieCheckup {

    private MovieDao movieDao;
    private ActorDao actorDao;

    public void checkExistingOfActor(Long actorId) {
        if (!actorDao.existsById(actorId)) {
            throw new NotFoundException("Actor not found!");
        }
    }

    public void checkExistingOfMovie(Long movieId) {
        if (!movieDao.existsById(movieId)) {
            throw new NotFoundException("Movie not found!");
        }
    }

    public void checkExistingActorMovieLink(Long actorId, Long movieId) {
        ActorEntity actorEntity;
        MovieEntity movieEntity;

        checkExistingOfActor(actorId);
        checkExistingOfMovie(movieId);

        actorEntity = actorDao.getById(actorId);
        movieEntity = movieDao.getById(movieId);

        if (actorEntity.getMovies().contains(movieEntity)) {
            throw new ValidationException("Actor's already play in this movie");
        }
    }

}
