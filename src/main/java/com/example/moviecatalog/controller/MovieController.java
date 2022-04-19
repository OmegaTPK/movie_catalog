package com.example.moviecatalog.controller;


import com.example.moviecatalog.dao.MovieDao;
import com.example.moviecatalog.dto.MovieDto;
import com.example.moviecatalog.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class MovieController {

    private MovieService movieService;
    private MovieDao movieDao;

    @Autowired
    public MovieController(MovieService movieService, MovieDao movieDao) {
        this.movieService = movieService;
        this.movieDao = movieDao;
    }

    @GetMapping("/movies")
    public ResponseEntity<List<MovieDto>> findAllMovies() {
        return movieService.getMovies();
    }

    @PostMapping("/movie/add")
    public ResponseEntity<MovieDto> addMovie(@RequestBody MovieDto movieDto) {
        return movieService.addMovie(movieDto);
    }

    @DeleteMapping("/movie/delete")
    public ResponseEntity deleteMovie(@RequestParam Long id) {
        return movieService.deleteMovie(id);
    }
}
