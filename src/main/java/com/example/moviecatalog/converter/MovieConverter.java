package com.example.moviecatalog.converter;

import com.example.moviecatalog.dto.MovieDto;
import com.example.moviecatalog.entity.MovieEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieConverter {

    public MovieDto convertEntityToDto(MovieEntity movieEntity) {
        //TODO make DTOs have a linked actors
        return new MovieDto(movieEntity.getId()
                , movieEntity.getName()
                , movieEntity.getYear()
                , movieEntity.getDescription()
                , movieEntity.getRate());
    }

    public List<MovieDto> convertEntities(List<MovieEntity> movieDtoList) {
        return movieDtoList.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    public MovieEntity convertDtoToEntity(MovieDto movieDto) {
        //TODO make DTOs have a linked actors
        return new MovieEntity(movieDto.getId()
                , movieDto.getName()
                , movieDto.getYear()
                , movieDto.getDescription()
                , movieDto.getRate()
                , new HashSet<>());
    }

    public List<MovieEntity> convertDtos(List<MovieDto> movieDtoList) {
        return movieDtoList.stream()
                .map(this::convertDtoToEntity)
                .collect(Collectors.toList());
    }

    public void fillEntityFromDto(MovieDto dto, MovieEntity entity) {
        entity.setDescription(dto.getDescription());
        entity.setName(dto.getName());
        entity.setRate(dto.getRate());
        entity.setYear(dto.getYear());
    }
}
