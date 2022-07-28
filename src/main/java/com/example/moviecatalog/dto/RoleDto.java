package com.example.moviecatalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class RoleDto implements Serializable {
    private Long id;
    private String name;
}
