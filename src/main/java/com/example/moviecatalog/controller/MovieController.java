package com.example.moviecatalog.controller;


import com.example.moviecatalog.dto.ActorDto;
import com.example.moviecatalog.dto.MovieDto;
import com.example.moviecatalog.service.ActorService;
import com.example.moviecatalog.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
@RequestMapping(path = {"/api/v1/movies"}, produces = APPLICATION_JSON_VALUE)
public class MovieController {

    private MovieService movieService;
    private ActorService actorService;

    @GetMapping
    public ResponseEntity<List<MovieDto>> findAllMovies() {
        return ResponseEntity.ok(movieService.getMovies());
    }

    @PostMapping
    public ResponseEntity<MovieDto> addMovie(@RequestBody MovieDto movieDto) {
        MovieDto result = movieService.addMovie(movieDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping
    public ResponseEntity deleteMovie(@RequestParam Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<MovieDto> updateMovie(@RequestBody MovieDto movieDto, @PathVariable Long id) {
        MovieDto responseDto = movieService.updateMovie(movieDto, id);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping(path = "/{movieId}/actors/{actorId}")
    public ResponseEntity<MovieDto> addActor(@PathVariable Long movieId, @PathVariable Long actorId) {
        MovieDto responseDto = movieService.addActorToMovie(movieId, actorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping(path = "/{movieId}/actors")
    public ResponseEntity<Set<ActorDto>> getActorsPlayedInMovie(@PathVariable Long movieId) {
        Set<ActorDto> actors = actorService.getActorsPlayedInMovie(movieId);
        return ResponseEntity.ok(actors);
    }

}
