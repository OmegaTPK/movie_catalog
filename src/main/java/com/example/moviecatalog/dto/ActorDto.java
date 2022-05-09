package com.example.moviecatalog.dto;

import com.example.moviecatalog.enums.Gender;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
public class ActorDto implements Serializable {
    private Long id;
    private String name;
    private String surname;
    private String middleName;
    private Gender gender;
    private Instant birthDate;
    private Instant activeStartDate;
    private String birthPlace;
    private String description;

    public ActorDto(Long id,
                    String name,
                    String surname,
                    String middleName,
                    Gender gender,
                    Instant birthDate,
                    Instant activeStartDate,
                    String birthPlace,
                    String description) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.middleName = middleName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.activeStartDate = activeStartDate;
        this.birthPlace = birthPlace;
        this.description = description;
    }
}
