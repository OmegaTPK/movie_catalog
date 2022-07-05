package com.example.moviecatalog.service;

import com.example.moviecatalog.converter.RoleConverter;
import com.example.moviecatalog.converter.UserConverter;
import com.example.moviecatalog.dao.CredentialsDao;
import com.example.moviecatalog.dao.RoleDao;
import com.example.moviecatalog.dao.UserDao;
import com.example.moviecatalog.dto.CredentialsDto;
import com.example.moviecatalog.dto.RoleDto;
import com.example.moviecatalog.dto.UserDto;
import com.example.moviecatalog.entity.CredentialsEntity;
import com.example.moviecatalog.entity.RoleEntity;
import com.example.moviecatalog.entity.UserEntity;
import com.example.moviecatalog.exception.CredentialsConflictException;
import com.example.moviecatalog.exception.NotFoundException;
import com.example.moviecatalog.exception.UserValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final CredentialsDao credentialsDao;
    private final UserDao userDao;
    private final RoleDao roleDao;
    private final CredentialsService credentialsService;
    private final UserConverter userConverter;
    private final RoleConverter roleConverter;
    private final Long DEFAULT_ROLE_ID = 0L;

    public UserDto addUser(UserDto userDto) {
        UserDto result;
        UserEntity entity;

        userDto.setId(null);
        entity = userConverter.convertDtoToEntity(userDto);
        entity.addRole(roleDao.getReferenceById(DEFAULT_ROLE_ID));

        result = userConverter.convertEntityToDto(userDao.save(entity));
        return result;
    }

    public UserDto updateUser(Long userId, UserDto userDto) {
        UserDto result;
        UserEntity userEntity;

        checkUserExist(userId);
        userEntity = userDao.getReferenceById(userId);

        userConverter.fillEntityFromDto(userEntity, userDto);

        result = userConverter.convertEntityToDto(userDao.save(userEntity));
        return result;
    }

    public List<UserDto> getUsers() {
        return userDao.findAll()
                .stream()
                .map(userConverter::convertEntityToDto)
                .collect(Collectors.toList());
    }

    public Set<RoleDto> getUsersRoles(Long userId) {
        Set<RoleDto> result;
        UserEntity user;

        checkUserExist(userId);
        user = userDao.getReferenceById(userId);
        result = user.getRoles()
                .stream()
                .map(roleConverter::convert)
                .collect(Collectors.toSet());
        return result;
    }

    public Set<RoleDto> setUsersRoles(Long userId, Set<RoleDto> roles) {
        Set<RoleEntity> rolesEntities;
        Set<RoleDto> result;
        UserEntity user;

        checkUserExist(userId);
        roles.forEach(roleDto -> checkRolesExist(roleDto.getId()));

        user = userDao.getReferenceById(userId);

        rolesEntities = roles.stream()
                .map(roleDto -> roleDao.getReferenceById(roleDto.getId()))
                .collect(Collectors.toSet());
        user.setRoles(rolesEntities);
        validateUsersRoles(user);

        result = userDao.save(user)
                .getRoles()
                .stream()
                .map(roleConverter::convert)
                .collect(Collectors.toSet());

        return result;
    }

    public Set<RoleDto> addRoleToUser(Long userId, RoleDto role) {
        Set<RoleDto> result;
        UserEntity user;

        checkUserExist(userId);
        checkRolesExist(role.getId());

        user = userDao.getReferenceById(userId);
        user.addRole(roleDao.getReferenceById(role.getId()));

        result = userDao.save(user)
                .getRoles()
                .stream()
                .map(roleConverter::convert)
                .collect(Collectors.toSet());

        return result;
    }

    public void setCredentials(Long userId, CredentialsDto credentialsDto) {
        UserEntity user;
        CredentialsEntity credentialsEntity;

        checkUserExist(userId);
        user = userDao.getReferenceById(userId);
        validateCredentials(user, credentialsDto);

        credentialsEntity = credentialsService.getCredentials(user, credentialsDto);
        user.setCredentials(credentialsEntity);
        userDao.save(user);
    }

    private void checkUserExist(Long id) {
        if (!userDao.existsById(id)) {
            throw new NotFoundException("User not found!");
        }
    }

    private void checkRolesExist(Long id) {
        if (!roleDao.existsById(id)) {
            throw new NotFoundException("Role not found!");
        }
    }

    private void validateUsersRoles(UserEntity entity) {
        if (entity.getRoles().isEmpty()) {
            throw new UserValidationException("User must have at list one role!");
        }
    }

    private void validateCredentials(UserEntity user, CredentialsDto dto) {
        CredentialsEntity entity = credentialsDao.findByLogin(dto.getLogin());
        if (entity != null && user.getCredentials() != entity) {
            throw new CredentialsConflictException("User with this user name is already exist");
        }
    }
}
