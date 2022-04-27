package com.example.moviecatalog.converter;

import com.example.moviecatalog.dto.ActorDto;
import com.example.moviecatalog.entity.ActorEntity;
import org.springframework.stereotype.Component;

@Component
public class ActorConverter {

    public ActorDto convert(ActorEntity entity) {
        return new ActorDto(entity.getId(),
                entity.getName(),
                entity.getSurname(),
                entity.getMiddleName(),
                entity.getGender(),
                entity.getBirthDate(),
                entity.getActiveStartDate(),
                entity.getBirthPlace());
    }

    public ActorEntity convert(ActorDto dto) {
        return new ActorEntity(dto.getId(),
                dto.getName(),
                dto.getSurname(),
                dto.getMiddleName(),
                dto.getGender(),
                dto.getBirthDate(),
                dto.getActiveStartDate(),
                dto.getBirthPlace());
    }
}
