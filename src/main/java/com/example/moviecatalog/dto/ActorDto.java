package com.example.moviecatalog.dto;

import com.example.moviecatalog.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@EqualsAndHashCode
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
}
