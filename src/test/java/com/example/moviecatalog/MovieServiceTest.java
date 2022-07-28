package com.example.moviecatalog;

import com.example.moviecatalog.converter.MovieConverter;
import com.example.moviecatalog.dao.ActorDao;
import com.example.moviecatalog.dao.MovieDao;
import com.example.moviecatalog.dto.MovieDto;
import com.example.moviecatalog.entity.ActorEntity;
import com.example.moviecatalog.entity.MovieEntity;
import com.example.moviecatalog.exception.NotFoundException;
import com.example.moviecatalog.exception.ValidationException;
import com.example.moviecatalog.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MovieServiceTest {

    private final Long EXISTING_MOVIE_ID = 1L;
    private final Long EXISTING_ACTOR_ID = 2L;
    private final Long NOT_EXISTING_ACTOR_ID = 3L;
    private final Long NOT_EXISTING_MOVIE_ID = 4L;
    private final String MOVIE_NOT_FOUND_MESSAGE = "Movie not found!";
    private final String ACTOR_NOT_FOUND_MESSAGE = "Actor not found!";
    private final String ACTOR_IN_MOVIE_MESSAGE = "Actor's already play in this movie";
    @Mock
    private MovieDao movieDao;
    @Mock
    private MovieConverter movieConverter;
    @Mock
    private ActorDao actorDao;
    @InjectMocks
    private MovieService movieService;
    private MovieEntity existingMovieEntity;
    private ActorEntity existingActorEntity;

    @BeforeEach
    public void prepareToTest() {
        existingActorEntity = ActorEntity
                .builder()
                .id(EXISTING_ACTOR_ID)
                .name("Leonardo")
                .surname("Di Caprio")
                .movies(new HashSet<>())
                .build();
        existingMovieEntity = MovieEntity
                .builder()
                .id(EXISTING_MOVIE_ID)
                .name("Titanic")
                .actors(new HashSet<>())
                .build();
        MockitoAnnotations.openMocks(this);
        doCallRealMethod().when(movieConverter).convertEntityToDto(any());
        doCallRealMethod().when(movieConverter).convertDtoToEntity(any());
        doCallRealMethod().when(movieConverter).convertEntities(any());
        doCallRealMethod().when(movieConverter).fillEntityFromDto(any(), any());
        when(movieDao.getReferenceById(EXISTING_MOVIE_ID)).thenReturn(existingMovieEntity);
        when(actorDao.getReferenceById(EXISTING_ACTOR_ID)).thenReturn(existingActorEntity);
        when(actorDao.existsById(EXISTING_ACTOR_ID)).thenReturn(Boolean.TRUE);
        when(movieDao.existsById(EXISTING_MOVIE_ID)).thenReturn(Boolean.TRUE);
        when(movieDao.existsById(NOT_EXISTING_MOVIE_ID)).thenReturn(Boolean.FALSE);
        when(actorDao.existsById(NOT_EXISTING_ACTOR_ID)).thenReturn(Boolean.FALSE);
    }

    @Test
    public void getMovies_getAllMoviesInCatalog_listMovieDto() {
        List<MovieEntity> entitles = new ArrayList<>();
        entitles.add(existingMovieEntity);
        List<MovieDto> expectedList = movieConverter.convertEntities(entitles);
        when(movieDao.findAll()).thenReturn(entitles);

        List<MovieDto> resultList = movieService.getMovies();

        assertNotNull(resultList);
        assertArrayEquals(expectedList.toArray(), resultList.toArray());
        verify(movieConverter, times(2)).convertEntities(eq(entitles));
        verify(movieDao).findAll();
    }

    @Test
    public void addMovie_incomingDtoIdNull_createdMovieDtoWithNewId() {
        MovieDto incomingDto = MovieDto.builder()
                .id(null)
                .name("Titanic").build();
        MovieEntity expectedToSaveEntity = MovieEntity.builder()
                .id(null)
                .name("Titanic")
                .actors(new HashSet<>()).build();
        MovieDto expectedDto = MovieDto.builder()
                .id(EXISTING_MOVIE_ID)
                .name("Titanic").build();

        when(movieDao.save(eq(expectedToSaveEntity))).thenReturn(existingMovieEntity);

        MovieDto resultDto = movieService.addMovie(incomingDto);

        assertNotNull(resultDto);
        assertEquals(expectedDto, resultDto);
        assertEquals(resultDto.getId(), EXISTING_MOVIE_ID);
        verify(movieConverter).convertDtoToEntity(incomingDto);
        verify(movieDao).save(eq(expectedToSaveEntity));
        verify(movieConverter).convertEntityToDto(eq(existingMovieEntity));
    }

    @Test
    public void addMovie_incomingDtoIdFilled_createdMovieDtoWithNewId() {
        MovieDto incomingDto = MovieDto.builder()
                .id(NOT_EXISTING_MOVIE_ID)
                .name("Titanic").build();
        MovieEntity expectedToSaveEntity = MovieEntity.builder()
                .id(null)
                .name("Titanic")
                .actors(new HashSet<>()).build();
        MovieDto expectedDto = MovieDto.builder()
                .id(EXISTING_MOVIE_ID)
                .name("Titanic").build();

        when(movieDao.save(eq(expectedToSaveEntity))).thenReturn(existingMovieEntity);

        MovieDto resultDto = movieService.addMovie(incomingDto);

        assertNotNull(resultDto);
        assertEquals(expectedDto, resultDto);
        assertEquals(resultDto.getId(), EXISTING_MOVIE_ID);
        verify(movieConverter).convertDtoToEntity(incomingDto);
        verify(movieDao).save(eq(expectedToSaveEntity));
        verify(movieConverter).convertEntityToDto(eq(existingMovieEntity));
    }

    @Test
    public void deleteMovie_existingMovieId_runWithoutExceptionThrows() {
        doNothing().when(movieDao).deleteById(EXISTING_MOVIE_ID);

        movieService.deleteMovie(EXISTING_MOVIE_ID);

        verify(movieDao).existsById(EXISTING_MOVIE_ID);
        verify(movieDao).deleteById(EXISTING_MOVIE_ID);
    }

    @Test
    public void deleteMovie_NotExistingMovieId_movieNotFoundException() {

        NotFoundException exception = assertThrows(NotFoundException.class, () -> movieService.deleteMovie(NOT_EXISTING_MOVIE_ID));

        assertEquals(exception.getMessage(), MOVIE_NOT_FOUND_MESSAGE);
        verify(movieDao).existsById(NOT_EXISTING_MOVIE_ID);
        verify(movieDao, never()).deleteById(any());
    }

    @Test
    public void updateMovie_existingMovieIncomingDtoIdNull_updatedDto() {
        MovieDto incomingDto = MovieDto.builder()
                .id(null)
                .name("Titanic")
                .description("Kate was a bitch").build();
        MovieEntity expectedToSaveEntity = MovieEntity.builder()
                .id(EXISTING_MOVIE_ID)
                .name("Titanic")
                .description("Kate was a bitch")
                .actors(new HashSet<>())
                .build();
        MovieDto expectedDto = MovieDto.builder()
                .id(EXISTING_MOVIE_ID)
                .name("Titanic")
                .description("Kate was a bitch").build();

        when(movieDao.save(eq(expectedToSaveEntity))).thenReturn(expectedToSaveEntity);

        MovieDto resultDto = movieService.updateMovie(incomingDto, EXISTING_MOVIE_ID);
        assertNotNull(resultDto);
        assertEquals(expectedDto, resultDto);
        assertEquals(resultDto.getId(), EXISTING_MOVIE_ID);
        verify(movieDao).existsById(EXISTING_MOVIE_ID);
        verify(movieDao).getReferenceById(EXISTING_MOVIE_ID);
        verify(movieDao).save(eq(expectedToSaveEntity));
    }

    @Test
    public void updateMovie_existingMovieIncomingDtoIdNotSameAsInBase_updatedDtoIdSameAsInBase() {
        MovieDto incomingDto = MovieDto.builder()
                .id(NOT_EXISTING_MOVIE_ID)
                .name("Titanic")
                .description("Kate was a bitch").build();
        MovieEntity expectedToSaveEntity = MovieEntity.builder()
                .id(EXISTING_MOVIE_ID)
                .name("Titanic")
                .description("Kate was a bitch")
                .actors(new HashSet<>())
                .build();
        MovieDto expectedDto = MovieDto.builder()
                .id(EXISTING_MOVIE_ID)
                .name("Titanic")
                .description("Kate was a bitch").build();

        when(movieDao.save(eq(expectedToSaveEntity))).thenReturn(expectedToSaveEntity);

        MovieDto resultDto = movieService.updateMovie(incomingDto, EXISTING_MOVIE_ID);
        assertNotNull(resultDto);
        assertEquals(expectedDto, resultDto);
        assertEquals(resultDto.getId(), EXISTING_MOVIE_ID);
        verify(movieDao).existsById(EXISTING_MOVIE_ID);
        verify(movieDao).getReferenceById(EXISTING_MOVIE_ID);
        verify(movieDao).save(eq(expectedToSaveEntity));
    }

    @Test
    public void updateMovie_NotExistingMovie_movieNotFoundException() {
        MovieDto incomingDto = MovieDto.builder()
                .id(EXISTING_MOVIE_ID)
                .name("Titanic")
                .description("Kate was a bitch").build();

        NotFoundException exception = assertThrows(NotFoundException.class, () -> movieService.updateMovie(incomingDto, NOT_EXISTING_MOVIE_ID));

        assertEquals(exception.getMessage(), MOVIE_NOT_FOUND_MESSAGE);
        verify(movieDao).existsById(NOT_EXISTING_MOVIE_ID);
        verify(movieDao, never()).getReferenceById(any());
        verify(movieDao, never()).save(any());
    }

    @Test
    public void addActorToMovie_existingMovieAndActorNotInTheMovie_updatedMovieDto() {

        when(movieDao.save(existingMovieEntity)).thenReturn(existingMovieEntity);

        MovieDto resultDto = movieService.addActorToMovie(EXISTING_MOVIE_ID, EXISTING_ACTOR_ID);

        assertNotNull(resultDto);
        assertEquals(1, existingMovieEntity.getActors().size());
        verify(movieDao).existsById(EXISTING_MOVIE_ID);
        verify(actorDao).existsById(EXISTING_ACTOR_ID);
        verify(movieDao, times(2)).getReferenceById(EXISTING_MOVIE_ID);
        verify(actorDao, times(2)).getReferenceById(EXISTING_ACTOR_ID);
        verify(movieDao).save(existingMovieEntity);
    }

    @Test
    public void addActorToMovie_existingMovieNotExistingActor_ActorNotFoundException() {

        NotFoundException exception = assertThrows(NotFoundException.class, () -> movieService.addActorToMovie(EXISTING_MOVIE_ID, NOT_EXISTING_ACTOR_ID));

        assertEquals(ACTOR_NOT_FOUND_MESSAGE, exception.getMessage());
        assertTrue(existingMovieEntity.getActors().isEmpty());
        verify(actorDao).existsById(NOT_EXISTING_ACTOR_ID);
        verify(movieDao, never()).existsById(any());
        verify(movieDao, never()).getReferenceById(any());
        verify(actorDao, never()).getReferenceById(any());
        verify(movieDao, never()).save(any());
    }

    @Test
    public void addActorToMovie_NotExistingMovieExistingActor_MovieNotFoundException() {

        NotFoundException exception = assertThrows(NotFoundException.class, () -> movieService.addActorToMovie(NOT_EXISTING_MOVIE_ID, EXISTING_ACTOR_ID));

        assertEquals(MOVIE_NOT_FOUND_MESSAGE, exception.getMessage());
        assertTrue(existingMovieEntity.getActors().isEmpty());
        verify(actorDao).existsById(EXISTING_ACTOR_ID);
        verify(movieDao).existsById(NOT_EXISTING_MOVIE_ID);
        verify(movieDao, never()).getReferenceById(any());
        verify(actorDao, never()).getReferenceById(any());
        verify(movieDao, never()).save(any());
    }

    @Test
    public void addActorToMovie_NotExistingMovieNotExistingActor_ActorNotFoundException() {

        NotFoundException exception = assertThrows(NotFoundException.class, () -> movieService.addActorToMovie(NOT_EXISTING_MOVIE_ID, NOT_EXISTING_ACTOR_ID));

        assertEquals(ACTOR_NOT_FOUND_MESSAGE, exception.getMessage());
        assertTrue(existingMovieEntity.getActors().isEmpty());
        verify(actorDao).existsById(NOT_EXISTING_ACTOR_ID);
        verify(movieDao, never()).existsById(any());
        verify(movieDao, never()).getReferenceById(any());
        verify(actorDao, never()).getReferenceById(any());
        verify(movieDao, never()).save(any());
    }

    @Test
    public void addActorToMovie_ExistingMovieExistingActorAlreadyInMovie_ConflictException() {
        existingMovieEntity.addActor(existingActorEntity);
        existingActorEntity.addMovie(existingMovieEntity);

        ValidationException exception = assertThrows(ValidationException.class, () -> movieService.addActorToMovie(EXISTING_MOVIE_ID, EXISTING_ACTOR_ID));

        assertEquals(ACTOR_IN_MOVIE_MESSAGE, exception.getMessage());
        verify(movieDao).existsById(EXISTING_MOVIE_ID);
        verify(actorDao).existsById(EXISTING_ACTOR_ID);
        verify(movieDao).getReferenceById(EXISTING_MOVIE_ID);
        verify(actorDao).getReferenceById(EXISTING_ACTOR_ID);
        verify(movieDao, never()).save(any());
    }

    @Test
    public void getMoviesByActor_existingActor_setMoviesDto() {
        existingMovieEntity.addActor(existingActorEntity);
        existingActorEntity.addMovie(existingMovieEntity);
        MovieDto movieDto = movieConverter.convertEntityToDto(existingMovieEntity);
        Set<MovieDto> expectedSet = new HashSet<>(Collections.singleton(movieDto));

        Set<MovieDto> resultSet = movieService.getMoviesByActor(EXISTING_ACTOR_ID);

        assertNotNull(resultSet);
        assertArrayEquals(expectedSet.toArray(), resultSet.toArray());
        verify(actorDao).existsById(EXISTING_ACTOR_ID);
        verify(actorDao).getReferenceById(EXISTING_ACTOR_ID);
        verify(movieConverter, times(2)).convertEntityToDto(eq(existingMovieEntity));
    }

    @Test
    public void getMoviesByActor_NotExistingActor_actorNotFoundException() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> movieService.getMoviesByActor(NOT_EXISTING_ACTOR_ID));

        assertEquals(ACTOR_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(actorDao).existsById(NOT_EXISTING_ACTOR_ID);
        verify(actorDao, never()).getReferenceById(any());
        verify(movieConverter, never()).convertEntityToDto(any());
    }
}
