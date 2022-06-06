package com.example.moviecatalog.controller;

import com.example.moviecatalog.dto.CredentialsDto;
import com.example.moviecatalog.dto.RoleDto;
import com.example.moviecatalog.dto.UserDto;
import com.example.moviecatalog.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
@RequestMapping(path = {"/api/v1/users"}, produces = APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(userDto));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, @PathVariable Long id) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @GetMapping(path = "/{id}/roles")
    public ResponseEntity<Set<RoleDto>> getRoles(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUsersRoles(id));
    }

    @PostMapping(path = "/{id}/roles")
    public ResponseEntity<Set<RoleDto>> addRole(@PathVariable Long id, @RequestBody RoleDto role) {
        return ResponseEntity.ok(userService.addRoleToUser(id, role));
    }

    @PutMapping(path = "/{id}/roles")
    public ResponseEntity<Set<RoleDto>> setRoles(@PathVariable Long id, @RequestBody Set<RoleDto> roles) {
        return ResponseEntity.ok(userService.setUsersRoles(id, roles));
    }

    @PutMapping(path = "/{id}/credentials")
    public ResponseEntity setLoginPass(@PathVariable Long id, @RequestBody CredentialsDto credentials) {
        userService.setCredentials(id, credentials);
        return ResponseEntity.ok().build();
    }
}
