package com.example.moviecatalog.converter;

import com.example.moviecatalog.dto.MovieDto;
import com.example.moviecatalog.entity.MovieEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MovieConverter {

    public MovieDto convert(MovieEntity movieEntity) {

        return new MovieDto(movieEntity.getId(), movieEntity.getName(), movieEntity.getDescription(), movieEntity.getRate(), movieEntity.getYear());

    }
    public List<MovieDto> convert(List<MovieEntity> movieEntityList) {
        List<MovieDto> returnedList= new ArrayList<MovieDto>();
        for(MovieEntity movieEntity: movieEntityList){
            returnedList.add(convert(movieEntity));
        }
        return returnedList;

    }

}



