package com.example.moviecatalog.controller;

import com.example.moviecatalog.dto.ActorDto;
import com.example.moviecatalog.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = {"/api/v1/actors"}, produces = APPLICATION_JSON_VALUE)
public class ActorController {

    private ActorService service;

    @Autowired
    public ActorController(ActorService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ActorDto> addActor(@RequestBody ActorDto dto) {
        ActorDto result = service.addActor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<ActorDto> updateActor(@RequestBody ActorDto dto, @PathVariable Long id) {
        ActorDto responseDto = service.updateActor(dto, id);
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping(path = "{actorId}/movies/{movieId}")
    public ResponseEntity<ActorDto> addMovie(@PathVariable Long actorId, @PathVariable Long movieId) {
        ActorDto responseDto = service.addMovieInActorsCareer(actorId, movieId);
        return ResponseEntity.ok().body(responseDto);
    }
}
