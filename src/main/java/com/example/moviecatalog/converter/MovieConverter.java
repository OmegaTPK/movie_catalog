package com.example.moviecatalog.converter;

import com.example.moviecatalog.dto.MovieDto;
import com.example.moviecatalog.entity.MovieEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieConverter {

    public MovieDto convert(MovieEntity movieEntity) {
        return new MovieDto(movieEntity.getId()
                , movieEntity.getName()
                , movieEntity.getDescription()
                , movieEntity.getRate()
                , movieEntity.getYear());
    }

    public List<MovieDto> convertEntities(List<MovieEntity> movieEntityList) {
        return movieEntityList.stream()
                .map(movieEntity -> convert(movieEntity))
                .collect(Collectors.toList());
    }

    public MovieEntity convert(MovieDto movieDto) {
        return new MovieEntity(movieDto.getId()
                , movieDto.getName()
                , movieDto.getYear()
                , movieDto.getDescription()
                , movieDto.getRate()
        );
    }

    public List<MovieEntity> convertDtos(List<MovieDto> movieDtoList) {
        return movieDtoList.stream()
                .map(movieDto -> convert(movieDto))
                .collect(Collectors.toList());
    }
}



