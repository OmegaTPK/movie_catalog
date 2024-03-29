package com.example.moviecatalog.controller;

import com.example.moviecatalog.dto.ActorDto;
import com.example.moviecatalog.dto.MovieDto;
import com.example.moviecatalog.service.ActorService;
import com.example.moviecatalog.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = {"/api/v1/actors"}, produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ActorController {

    private final ActorService actorService;
    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<Set<ActorDto>> getActors() {
        return ResponseEntity.ok(actorService.getActors());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PostMapping
    public ResponseEntity<ActorDto> addActor(@RequestBody ActorDto dto) {
        ActorDto result = actorService.addActor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PutMapping(path = "{id}")
    public ResponseEntity<ActorDto> updateActor(@RequestBody ActorDto dto, @PathVariable Long id) {
        ActorDto responseDto = actorService.updateActor(dto, id);
        return ResponseEntity.ok(responseDto);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PostMapping(path = "{actorId}/movies/{movieId}")
    public ResponseEntity<ActorDto> addMovie(@PathVariable Long actorId, @PathVariable Long movieId) {
        ActorDto responseDto = actorService.addMovieInActorsCareer(actorId, movieId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @GetMapping(path = "{actorId}/movies")
    public ResponseEntity<Set<MovieDto>> getMoviesByActor(@PathVariable Long actorId) {
        Set<MovieDto> result = movieService.getMoviesByActor(actorId);
        return ResponseEntity.ok(result);
    }
}
