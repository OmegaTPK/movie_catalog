package com.example.moviecatalog.converter;

import com.example.moviecatalog.dto.UserDto;
import com.example.moviecatalog.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class UserConverter {

    public UserDto convert(UserEntity userEntity) {
        return new UserDto(userEntity.getId(),
                userEntity.getName(),
                userEntity.getLastname(),
                userEntity.getDescription(),
                userEntity.getActive());
    }

    public UserEntity convert(UserDto userDto) {
        return new UserEntity(userDto.getId(),
                userDto.getName(),
                userDto.getLastname(),
                userDto.getDescription(),
                userDto.getActive(),
                new HashSet<>(),
                null);
    }

    public void fillEntityFromDto(UserEntity entity, UserDto dto) {
        entity.setName(dto.getName());
        entity.setLastname(dto.getLastname());
        entity.setDescription(dto.getDescription());
        entity.setActive(dto.getActive());
    }
}

