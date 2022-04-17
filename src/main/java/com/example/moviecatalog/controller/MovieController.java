package com.example.moviecatalog.controller;


import com.example.moviecatalog.dao.MovieDao;
import com.example.moviecatalog.dto.MovieDto;
import com.example.moviecatalog.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<MovieDto> findAllMovies() {
        return movieService.getMovies();
    }
}
