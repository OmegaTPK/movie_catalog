package com.example.moviecatalog.controller;


import com.example.moviecatalog.dto.MovieDto;
import com.example.moviecatalog.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = {"/api/v1/movies"}, produces = APPLICATION_JSON_VALUE)
public class MovieController {

    private MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

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
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping(path = "/{movieId}/actors/{actorId}")
    public ResponseEntity<MovieDto> addActor(@PathVariable Long movieId, @PathVariable Long actorId) {
        MovieDto responseDto = movieService.addActor(movieId, actorId);
        return ResponseEntity.ok().body(responseDto);
    }
}
