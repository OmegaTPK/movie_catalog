package com.example.moviecatalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class UserDto implements Serializable {
    private Long id;
    private String name;
    private String lastname;
    private String description;
    private Boolean active;
}
