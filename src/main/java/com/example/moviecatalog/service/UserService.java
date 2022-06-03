package com.example.moviecatalog.service;

import com.example.moviecatalog.converter.AuthConverter;
import com.example.moviecatalog.converter.RoleConverter;
import com.example.moviecatalog.converter.UserConverter;
import com.example.moviecatalog.dao.AuthenticationInfoDao;
import com.example.moviecatalog.dao.RoleDao;
import com.example.moviecatalog.dao.UserDao;
import com.example.moviecatalog.dto.LoginPassDto;
import com.example.moviecatalog.dto.RoleDto;
import com.example.moviecatalog.dto.UserDto;
import com.example.moviecatalog.entity.AuthenticationInfoEntity;
import com.example.moviecatalog.entity.RoleEntity;
import com.example.moviecatalog.entity.UserEntity;
import com.example.moviecatalog.exception.NotFoundException;
import com.example.moviecatalog.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserService {

    private final AuthenticationInfoDao authDao;
    private final UserDao userDao;
    private final RoleDao roleDao;
    private final AuthConverter authConverter;
    private final UserConverter userConverter;
    private final RoleConverter roleConverter;

    private final RoleEntity DEFAULT_ROLE;

    @Autowired
    public UserService(UserDao userDao, RoleDao roleDao, UserConverter userConverter, RoleConverter roleConverter, AuthConverter authConverter, AuthenticationInfoDao authDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.userConverter = userConverter;
        this.roleConverter = roleConverter;
        this.authDao = authDao;
        this.authConverter = authConverter;
        this.DEFAULT_ROLE = roleDao.getById(0L);
    }

    public UserDto addUser(UserDto userDto) {
        UserDto result;
        UserEntity entity;

        userDto.setId(null);
        entity = userConverter.convert(userDto);
        entity.addRole(DEFAULT_ROLE);

        result = userConverter.convert(userDao.save(entity));
        return result;
    }

    public UserDto updateUser(Long userId, UserDto userDto) {
        UserDto result;
        UserEntity userEntity;

        checkUserExist(userId);
        userEntity = userDao.getById(userId);

        userConverter.fillEntityFromDto(userEntity, userDto);

        result = userConverter.convert(userDao.save(userEntity));
        return result;
    }

    public List<UserDto> getUsers() {
        return userDao.findAll()
                .stream()
                .map(userConverter::convert)
                .collect(Collectors.toList());
    }

    public Set<RoleDto> getUsersRoles(Long userId) {
        Set<RoleDto> result;
        UserEntity user;

        checkUserExist(userId);
        user = userDao.getById(userId);
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
        user = userDao.getById(userId);
        rolesEntities = roles
                .stream()
                .map(roleDto -> roleDao.getById(roleDto.getId()))
                .collect(Collectors.toSet());
        user.setRoles(rolesEntities);
        checkUsersRoles(user);

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
        user = userDao.getById(userId);
        user.addRole(roleDao.getById(role.getId()));

        result = userDao.save(user)
                .getRoles()
                .stream()
                .map(roleConverter::convert)
                .collect(Collectors.toSet());

        return result;
    }

    public void setLoginPass(Long userId, LoginPassDto loginPassDto) {
        UserEntity user;
        AuthenticationInfoEntity auth;

        checkUserExist(userId);
        user = userDao.getById(userId);

        checkLoginPass(user, loginPassDto);

        auth = authDao.findByUser(user) == null
                ? authConverter.convert(loginPassDto)
                : authDao.findByUser(user);
        authConverter.fillFromDto(loginPassDto, auth);

        auth.setUser(user);
        user.setAuthInfo(auth);
        authDao.save(auth);
        userDao.save(user);
    }

    private void checkUserExist(Long id) {
        if (!userDao.existsById(id)) {
            throw new NotFoundException("User not found!");
        }
    }

    private void checkUsersRoles(UserEntity entity) {
        if (entity.getRoles().isEmpty()) {
            throw new ValidationException("User must have at list one role!");
        }
    }

    private void checkLoginPass(UserEntity user, LoginPassDto dto) {
        AuthenticationInfoEntity entity = authDao.findByLogin(dto.login());
        if (entity != null && entity.getUser() != user) {
            throw new ValidationException("User with this user name is already exist");
        }
    }
}

