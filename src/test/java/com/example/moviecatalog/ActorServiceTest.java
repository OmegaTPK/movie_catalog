package com.example.moviecatalog;

import com.example.moviecatalog.converter.ActorConverter;
import com.example.moviecatalog.dao.ActorDao;
import com.example.moviecatalog.dao.MovieDao;
import com.example.moviecatalog.dto.ActorDto;
import com.example.moviecatalog.entity.ActorEntity;
import com.example.moviecatalog.entity.MovieEntity;
import com.example.moviecatalog.exception.NotFoundException;
import com.example.moviecatalog.exception.ValidationException;
import com.example.moviecatalog.service.ActorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ActorServiceTest {


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
    private ActorConverter actorConverter;
    @Mock
    private ActorDao actorDao;
    @InjectMocks
    private ActorService actorService;
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
        doCallRealMethod().when(actorConverter).convertEntityToDto(any());
        doCallRealMethod().when(actorConverter).convertDtoToEntity(any());
        doCallRealMethod().when(actorConverter).fillEntityFromDto(any(), any());
        when(movieDao.getReferenceById(EXISTING_MOVIE_ID)).thenReturn(existingMovieEntity);
        when(actorDao.getReferenceById(EXISTING_ACTOR_ID)).thenReturn(existingActorEntity);
        when(actorDao.existsById(EXISTING_ACTOR_ID)).thenReturn(Boolean.TRUE);
        when(movieDao.existsById(EXISTING_MOVIE_ID)).thenReturn(Boolean.TRUE);
        when(movieDao.existsById(NOT_EXISTING_MOVIE_ID)).thenReturn(Boolean.FALSE);
        when(actorDao.existsById(NOT_EXISTING_ACTOR_ID)).thenReturn(Boolean.FALSE);
    }

    @Test
    public void addActor_incomingDtoIdNull_createdActorDtoWithNewId() {
        ActorDto incomingDto = ActorDto
                .builder()
                .id(null)
                .name("Leonardo")
                .surname("Di Caprio")
                .build();
        ActorEntity expectedToSaveEntity = ActorEntity
                .builder()
                .id(null)
                .name("Leonardo")
                .surname("Di Caprio")
                .build();
        ActorDto expectedDto = ActorDto
                .builder()
                .id(EXISTING_ACTOR_ID)
                .name("Leonardo")
                .surname("Di Caprio")
                .build();
        when(actorDao.save(eq(expectedToSaveEntity))).thenReturn(existingActorEntity);

        ActorDto resultDto = actorService.addActor(incomingDto);

        assertEquals(expectedDto, resultDto);
        assertNotNull(resultDto);
        assertEquals(expectedDto, resultDto);
        assertEquals(resultDto.getId(), EXISTING_ACTOR_ID);
        verify(actorConverter).convertDtoToEntity(incomingDto);
        verify(actorDao).save(eq(expectedToSaveEntity));
        verify(actorConverter).convertEntityToDto(eq(existingActorEntity));
    }

    @Test
    public void addMovie_incomingDtoIdFilled_createdMovieDtoWithNewId() {
        ActorDto incomingDto = ActorDto
                .builder()
                .id(NOT_EXISTING_ACTOR_ID)
                .name("Leonardo")
                .surname("Di Caprio")
                .build();
        ActorEntity expectedToSaveEntity = ActorEntity
                .builder()
                .id(null)
                .name("Leonardo")
                .surname("Di Caprio")
                .build();
        ActorDto expectedDto = ActorDto
                .builder()
                .id(EXISTING_ACTOR_ID)
                .name("Leonardo")
                .surname("Di Caprio")
                .build();
        when(actorDao.save(eq(expectedToSaveEntity))).thenReturn(existingActorEntity);

        ActorDto resultDto = actorService.addActor(incomingDto);

        assertEquals(expectedDto, resultDto);
        assertNotNull(resultDto);
        assertEquals(expectedDto, resultDto);
        assertEquals(resultDto.getId(), EXISTING_ACTOR_ID);
        verify(actorConverter).convertDtoToEntity(incomingDto);
        verify(actorDao).save(eq(expectedToSaveEntity));
        verify(actorConverter).convertEntityToDto(eq(existingActorEntity));
    }

    @Test
    public void updateActor_existingActorIncomingDtoIdNull_updatedDto() {
        ActorDto incomingDto = ActorDto
                .builder()
                .id(null)
                .name("Leonardo")
                .surname("Di Caprio")
                .description("Very talented")
                .build();
        ActorEntity expectedToSaveEntity = ActorEntity
                .builder()
                .id(EXISTING_ACTOR_ID)
                .name("Leonardo")
                .surname("Di Caprio")
                .description("Very talented")
                .build();
        ActorDto expectedDto = ActorDto
                .builder()
                .id(EXISTING_ACTOR_ID)
                .name("Leonardo")
                .surname("Di Caprio")
                .description("Very talented")
                .build();
        when(actorDao.save(eq(expectedToSaveEntity))).thenReturn(expectedToSaveEntity);

        ActorDto resultDto = actorService.updateActor(incomingDto, EXISTING_ACTOR_ID);

        assertEquals(expectedDto, resultDto);
        assertNotNull(resultDto);
        assertEquals(expectedDto, resultDto);
        assertEquals(resultDto.getId(), EXISTING_ACTOR_ID);
        verify(actorDao).save(eq(expectedToSaveEntity));
        verify(actorConverter).convertEntityToDto(eq(existingActorEntity));
        verify(actorDao).existsById(EXISTING_ACTOR_ID);
        verify(actorDao).getReferenceById(EXISTING_ACTOR_ID);
    }

    @Test
    public void updateActor_existingActorIncomingDtoIdNotSameAsInBase_updatedDtoIdSameAsInBase() {
        ActorDto incomingDto = ActorDto
                .builder()
                .id(NOT_EXISTING_ACTOR_ID)
                .name("Leonardo")
                .surname("Di Caprio")
                .description("Very talented")
                .build();
        ActorEntity expectedToSaveEntity = ActorEntity
                .builder()
                .id(EXISTING_ACTOR_ID)
                .name("Leonardo")
                .surname("Di Caprio")
                .description("Very talented")
                .build();
        ActorDto expectedDto = ActorDto
                .builder()
                .id(EXISTING_ACTOR_ID)
                .name("Leonardo")
                .surname("Di Caprio")
                .description("Very talented")
                .build();
        when(actorDao.save(eq(expectedToSaveEntity))).thenReturn(expectedToSaveEntity);

        ActorDto resultDto = actorService.updateActor(incomingDto, EXISTING_ACTOR_ID);

        assertEquals(expectedDto, resultDto);
        assertNotNull(resultDto);
        assertEquals(expectedDto, resultDto);
        assertEquals(resultDto.getId(), EXISTING_ACTOR_ID);
        verify(actorDao).save(eq(expectedToSaveEntity));
        verify(actorConverter).convertEntityToDto(eq(existingActorEntity));
    }

    @Test
    public void updateActor_NotExistingActor_actorNotFoundException() {
        ActorDto incomingDto = ActorDto
                .builder()
                .id(NOT_EXISTING_ACTOR_ID)
                .name("Leonardo")
                .surname("Di Caprio")
                .description("Very talented")
                .build();

        NotFoundException exception = assertThrows(NotFoundException.class, () -> actorService.updateActor(incomingDto, NOT_EXISTING_ACTOR_ID));

        assertEquals(exception.getMessage(), ACTOR_NOT_FOUND_MESSAGE);
        verify(actorDao).existsById(NOT_EXISTING_ACTOR_ID);
        verify(actorDao, never()).getReferenceById(any());
        verify(actorDao, never()).save(any());
    }

    @Test
    public void getActors_getAllActorsInCatalog_listActorDto() {
        List<ActorEntity> entitles = new ArrayList<>();
        entitles.add(existingActorEntity);
        Set<ActorDto> expectedSet = entitles
                .stream()
                .map(actorConverter::convertEntityToDto)
                .collect(Collectors.toSet());
        when(actorDao.findAll()).thenReturn(entitles);

        Set<ActorDto> resultSet = actorService.getActors();

        assertNotNull(resultSet);
        assertArrayEquals(expectedSet.toArray(), resultSet.toArray());
        verify(actorConverter, times(2)).convertEntityToDto(eq(existingActorEntity));
        verify(actorDao).findAll();
    }

    @Test
    public void addMovieInActorsCareer_existingMovieAndActorNotInTheMovie_updatedActorDto() {

        when(actorDao.save(existingActorEntity)).thenReturn(existingActorEntity);

        ActorDto resultDto = actorService.addMovieInActorsCareer(EXISTING_ACTOR_ID, EXISTING_MOVIE_ID);

        assertNotNull(resultDto);
        assertEquals(1, existingActorEntity.getMovies().size());
        verify(movieDao).existsById(EXISTING_MOVIE_ID);
        verify(actorDao).existsById(EXISTING_ACTOR_ID);
        verify(movieDao, times(2)).getReferenceById(EXISTING_MOVIE_ID);
        verify(actorDao, times(2)).getReferenceById(EXISTING_ACTOR_ID);
        verify(actorDao).save(existingActorEntity);
    }

    @Test
    public void addMovieInActorsCareer_existingMovieNotExistingActor_ActorNotFoundException() {

        NotFoundException exception = assertThrows(NotFoundException.class, () -> actorService.addMovieInActorsCareer(NOT_EXISTING_ACTOR_ID, EXISTING_MOVIE_ID));

        assertEquals(ACTOR_NOT_FOUND_MESSAGE, exception.getMessage());
        assertTrue(existingMovieEntity.getActors().isEmpty());
        verify(actorDao).existsById(NOT_EXISTING_ACTOR_ID);
        verify(movieDao, never()).existsById(any());
        verify(movieDao, never()).getReferenceById(any());
        verify(actorDao, never()).getReferenceById(any());
        verify(actorDao, never()).save(any());
    }

    @Test
    public void addMovieInActorsCareer_NotExistingMovieExistingActor_MovieNotFoundException() {

        NotFoundException exception = assertThrows(NotFoundException.class, () -> actorService.addMovieInActorsCareer(EXISTING_ACTOR_ID, NOT_EXISTING_MOVIE_ID));

        assertEquals(MOVIE_NOT_FOUND_MESSAGE, exception.getMessage());
        assertTrue(existingMovieEntity.getActors().isEmpty());
        verify(actorDao).existsById(EXISTING_ACTOR_ID);
        verify(movieDao).existsById(NOT_EXISTING_MOVIE_ID);
        verify(movieDao, never()).getReferenceById(any());
        verify(actorDao, never()).getReferenceById(any());
        verify(actorDao, never()).save(any());
    }

    @Test
    public void addMovieInActorsCareer_NotExistingMovieNotExistingActor_ActorNotFoundException() {

        NotFoundException exception = assertThrows(NotFoundException.class, () -> actorService.addMovieInActorsCareer(NOT_EXISTING_ACTOR_ID, NOT_EXISTING_MOVIE_ID));

        assertEquals(ACTOR_NOT_FOUND_MESSAGE, exception.getMessage());
        assertTrue(existingMovieEntity.getActors().isEmpty());
        verify(actorDao).existsById(NOT_EXISTING_ACTOR_ID);
        verify(movieDao, never()).existsById(any());
        verify(movieDao, never()).getReferenceById(any());
        verify(actorDao, never()).getReferenceById(any());
        verify(actorDao, never()).save(any());
    }

    @Test
    public void addMovieInActorsCareer_ExistingMovieExistingActorAlreadyInMovie_ConflictException() {
        existingMovieEntity.addActor(existingActorEntity);
        existingActorEntity.addMovie(existingMovieEntity);

        ValidationException exception = assertThrows(ValidationException.class, () -> actorService.addMovieInActorsCareer(EXISTING_ACTOR_ID, EXISTING_MOVIE_ID));

        assertEquals(ACTOR_IN_MOVIE_MESSAGE, exception.getMessage());
        verify(movieDao).existsById(EXISTING_MOVIE_ID);
        verify(actorDao).existsById(EXISTING_ACTOR_ID);
        verify(movieDao).getReferenceById(EXISTING_MOVIE_ID);
        verify(actorDao).getReferenceById(EXISTING_ACTOR_ID);
        verify(actorDao, never()).save(any());
    }

    @Test
    public void getActorsPlayedInMovie_existingMovie_setMoviesDto() {
        existingMovieEntity.addActor(existingActorEntity);
        existingActorEntity.addMovie(existingMovieEntity);
        ActorDto actorDto = actorConverter.convertEntityToDto(existingActorEntity);
        Set<ActorDto> expectedSet = new HashSet<>(Collections.singleton(actorDto));

        Set<ActorDto> resultSet = actorService.getActorsPlayedInMovie(EXISTING_MOVIE_ID);

        assertNotNull(resultSet);
        assertArrayEquals(expectedSet.toArray(), resultSet.toArray());
        verify(movieDao).existsById(EXISTING_MOVIE_ID);
        verify(movieDao).getReferenceById(EXISTING_MOVIE_ID);
        verify(actorConverter, times(2)).convertEntityToDto(eq(existingActorEntity));
    }

    @Test
    public void getActorsPlayedInMovie_NotExistingMovie_movieNotFoundException() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> actorService.getActorsPlayedInMovie(NOT_EXISTING_MOVIE_ID));

        assertEquals(MOVIE_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(movieDao).existsById(NOT_EXISTING_MOVIE_ID);
        verify(movieDao, never()).getReferenceById(any());
        verify(actorConverter, never()).convertEntityToDto(any());
    }

}
