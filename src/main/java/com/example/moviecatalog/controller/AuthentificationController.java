package com.example.moviecatalog.controller;


import com.example.moviecatalog.dto.CredentialsDto;
import com.example.moviecatalog.dto.TokenDto;
import com.example.moviecatalog.service.CredentialsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = {"/api/v1/token"}, produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AuthentificationController {

    private final CredentialsService credentialsService;

    @PostMapping
    public ResponseEntity<TokenDto> getToken(@RequestBody CredentialsDto credentials) {
        return ResponseEntity.ok(credentialsService.getTokenFromCredentials(credentials));
    }
}
