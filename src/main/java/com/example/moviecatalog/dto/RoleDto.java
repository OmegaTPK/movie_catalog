package com.example.moviecatalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class RoleDto implements Serializable {
    private Long id;
    private String name;
}
