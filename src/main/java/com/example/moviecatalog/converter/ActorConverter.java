package com.example.moviecatalog.converter;

import com.example.moviecatalog.dto.ActorDto;
import com.example.moviecatalog.entity.ActorEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class ActorConverter {

    public ActorDto convert(ActorEntity entity) {
        //TODO make DTOs have a linked movies
        return new ActorDto(entity.getId(),
                entity.getName(),
                entity.getSurname(),
                entity.getMiddleName(),
                entity.getGender(),
                entity.getBirthDate(),
                entity.getActiveStartDate(),
                entity.getBirthPlace(),
                entity.getDescription());
    }

    public ActorEntity convert(ActorDto dto) {
        //TODO make DTOs have a linked movies
        return new ActorEntity(dto.getId(),
                dto.getName(),
                dto.getSurname(),
                dto.getMiddleName(),
                dto.getGender(),
                dto.getBirthDate(),
                dto.getActiveStartDate(),
                dto.getBirthPlace(),
                dto.getDescription(),
                new HashSet<>());
    }

    public void fillEntityFromDto(ActorDto dto, ActorEntity entity) {
        entity.setDescription(dto.getDescription());
        entity.setName(dto.getName());
        entity.setActiveStartDate(dto.getActiveStartDate());
        entity.setBirthDate(dto.getBirthDate());
        entity.setBirthPlace(dto.getBirthPlace());
        entity.setGender(dto.getGender());
        entity.setMiddleName(dto.getMiddleName());
        entity.setSurname(dto.getSurname());
    }
}
