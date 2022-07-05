package com.example.moviecatalog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class UserDto implements Serializable {
    private Long id;
    private String name;
    private String lastname;
    private String description;
    private Boolean active;
}
